package io.netx.net.test;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientTest {
    @Test
    public void clientTest() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8090));
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.put("hello netx".getBytes());
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) {
            socketChannel.write(buffer);
            System.out.println(socketChannel.validOps());
            Thread.sleep(13009);
        }
        Thread.sleep(10000);
        socketChannel.write(buffer);
    }
}
