package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.Server;
import org.apache.dubbo.remoting.transport.AbstractServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/10 13:50
 * @Description:
 **/
public class NioServer extends AbstractServer implements Server {

    private final Map<String, Channel> clientChannels = new ConcurrentHashMap<>(8);

    private ServerSocketChannel ssc;

    private Selector selector;

    private final NioWorker[] workers = new NioWorker[8];

    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);

    private final ExecutorService fixedWorkerPool = Executors.newFixedThreadPool(8, new ThreadFactory() {

        private final AtomicInteger threadNum = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "nio-server-" + threadNum.getAndIncrement());
            return thread;
        }
    });

    private final AtomicInteger counter = new AtomicInteger(0);

    public NioServer(URL url, ChannelHandler handler) throws RemotingException {
        super(url, handler);
        // 构造runnable 并且启动
        for (int i = 0; i < workers.length; i++) {
            this.workers[i] = new NioWorker();
            this.fixedWorkerPool.execute(this.workers[i]);
        }
    }

    @Override
    protected void doOpen() throws Throwable {
        this.selector = Selector.open();
        this.ssc = ServerSocketChannel.open();
        this.ssc.configureBlocking(false);
        this.ssc.bind(getBindAddress());

        // server thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        serverSelect();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }).start();
        this.ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void serverSelect() throws IOException {
        int ready = this.selector.select(100);
        if (ready <= 0) {
            return;
        }

        Set<SelectionKey> set = this.selector.selectedKeys();
        Iterator<SelectionKey> iterator = set.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            int index = this.counter.getAndAdd(1);

            /**
             * {@link ServerSocketChannel}的感兴趣事件是ACCEPT
             * 其他的{@link SocketChannel}的感兴趣事件是READ
             */
            // 新链接
            if (key.isAcceptable()) {
                acceptClient(key, this.workers[index]);
            }
            // 进来这里的必定是client发送到server的数据,所以这里出发可读
            if (key.isReadable()) {
                if (readFromChannel(key, index)) {
                    continue;
                }
            }
        }
    }

    private boolean readFromChannel(SelectionKey key, int index) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        // 先找找看是否有存在的NioChannel,有的话直接发送事件,没的话说明channel可能被移除了,所以下一步需要判断,socketChannel是否还链接,连着的话再构建channel然后可读事件
        String cacheKey = address((InetSocketAddress) channel.getRemoteAddress());
        NioChannel nChannel;
        if (clientChannels.containsKey(cacheKey)) {
            nChannel = (NioChannel) clientChannels.get(cacheKey);
        } else {
            if (channel.isConnected()) {
                nChannel = new NioChannel(NioServer.this.getChannelHandler(), channel, NioServer.this);
            } else {
                logger.info("channel:" + channel.getRemoteAddress() + " closed");
                channel.close();
                return true;
            }
        }
        if (nChannel != null) {
            NioChannel finalNChannel = nChannel;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                if (channel.read(buffer) < 0) {
                    channel.close();
                    key.cancel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = buffer.array();
            this.workers[index].execute(() -> {
                try {
                    finalNChannel.getChannelHandler().received(finalNChannel, data);
                } catch (RemotingException e) {
                    e.printStackTrace();
                }
            });
        }
        return false;
    }

    private void acceptClient(SelectionKey key, NioWorker worker) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        NioChannel nioChannel = new NioChannel(NioServer.this.getChannelHandler(), socketChannel, NioServer.this);
        NioServer.this.clientChannels.put(address((InetSocketAddress) socketChannel.getRemoteAddress()), nioChannel);

        // register event
        socketChannel.register(this.selector, SelectionKey.OP_READ);
        // 执行链接建立的事件
        worker.execute(new Runnable() {
            @Override
            public void run() {
                // fire
                try {
                    // 进来这里的必定是客户端链接,所以这里的runnable直接触发链接事件
                    nioChannel.getChannelHandler().connected(nioChannel);
                } catch (RemotingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void doClose() throws Throwable {
        this.clientChannels.forEach((k, v) -> {
            if (v.isConnected()) {
                try {
                    v.close();
                } catch (Throwable e) {
                    logger.error(e.getMessage(),e);
                }
            }
        });

        for (NioWorker worker : this.workers) {
            worker.shutdownNow();
        }

        this.clientChannels.clear();
        this.ssc.close();
    }

    @Override
    public boolean isBound() {
        return this.ssc.isOpen();
    }

    @Override
    public Collection<Channel> getChannels() {
        return this.clientChannels.values();
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        String ip = remoteAddress.getHostName();
        int port = remoteAddress.getPort();
        return this.clientChannels.get(ip + ":" + port);
    }

    public static String address(InetSocketAddress socketAddress) {
        return socketAddress.getHostName() + ":" + socketAddress.getPort();
    }
}
