package io.netx.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8090));
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(1000);
            buffer.put(("hello netx" + i).getBytes());
            socketChannel.write(buffer);
            System.out.println(socketChannel.validOps());
            Thread.sleep(10000);
        }
        Thread.sleep(10000);
    }
}
