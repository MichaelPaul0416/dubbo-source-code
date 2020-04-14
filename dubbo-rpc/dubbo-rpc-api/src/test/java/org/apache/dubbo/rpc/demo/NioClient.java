package org.apache.dubbo.rpc.demo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.Client;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.transport.AbstractClient;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wangqiang20995
 * @Date: 2020/4/13 13:19
 * @Description:
 **/
public class NioClient extends AbstractClient implements Client {

    private SocketChannel socketChannel;
    private Selector selector;

    private final NioWorker[] nioWorkers = new NioWorker[8];
    ;
    private NioChannel nioChannel;

    private static final Logger logger = LoggerFactory.getLogger(NioClient.class);

    public NioClient(URL url, ChannelHandler handler) throws RemotingException {
        super(url, handler);
        for (int i = 0; i < nioWorkers.length; i++) {
            if (this.nioWorkers[i] == null) {
                this.nioWorkers[i] = new NioWorker();
                this.fixedWorkerPool.execute(this.nioWorkers[i]);
            }
        }
    }

    private final ExecutorService fixedWorkerPool = Executors.newFixedThreadPool(8, new ThreadFactory() {

        private final AtomicInteger threadNum = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "nio-client-" + threadNum.getAndIncrement());
            return thread;
        }
    });

    @Override
    protected void doOpen() throws Throwable {
        if (this.socketChannel == null) {
            this.socketChannel = SocketChannel.open();
        }
        this.socketChannel.configureBlocking(false);

        if (this.selector == null) {
            selector = Selector.open();
        }

    }

    @Override
    protected void doClose() throws Throwable {
        this.socketChannel.close();
    }

    @Override
    protected void doConnect() throws Throwable {

        // 构建一个TCP链接
        this.socketChannel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        String host = getUrl().getHost();
        int port = getUrl().getPort();
        boolean success = this.socketChannel.connect(new InetSocketAddress(host, port));
        if (!success) {
            throw new RuntimeException("unConnect to remote:[" + host + ":" + port + "]");
        }
        this.nioChannel = new NioChannel(getChannelHandler(), this.socketChannel, this);
    }

    @Override
    protected void doDisConnect() throws Throwable {
        for (NioWorker nioWorker : this.nioWorkers) {
            nioWorker.shutdownNow();
        }
    }

    @Override
    protected Channel getChannel() {
        return this.nioChannel;
    }
}
