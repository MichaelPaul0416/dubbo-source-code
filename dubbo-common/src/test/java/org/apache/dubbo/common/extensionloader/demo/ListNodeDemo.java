package org.apache.dubbo.common.extensionloader.demo;

/**
 * @Author: wangqiang20995
 * @Date: 2020/2/24 16:52
 * @Description:先加入的元素，加在队列的首部
 **/
public class ListNodeDemo {

    private Node node;

    private static class Node {
        private int value;
        private Node next;

        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    '}';
        }
    }

    public void add(int value) {
        node = new Node(value, node);
    }

    public Node reverse() {
        Node reverse = null;
        Node start = node;
        while (start != null) {
            Node tmp = start;
            start = start.next;
            tmp.next = reverse;
            reverse = tmp;
        }
        return reverse;
    }

    public Node getStart(){
        return node;
    }

    public static void printListNode(Node start) {
        StringBuilder builder = new StringBuilder();

        while (start != null) {
            builder.append(start.value);
            builder.append("->");
            start = start.next;
        }
        // 删除的char 包含begin参数，但是不包含end参数指定的char
        builder.delete(builder.length() - 2, builder.length());
        System.out.println("list:" + builder.toString());
    }

    public static void main(String args[]){
        ListNodeDemo lnd = new ListNodeDemo();
        lnd.add(1);
        lnd.add(2);
        lnd.add(3);
        lnd.add(4);
        lnd.add(5);
        printListNode(lnd.getStart());

        printListNode(lnd.reverse());
    }
}
