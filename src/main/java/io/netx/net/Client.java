package io.netx.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int k = 0; k < 100; k++) {
            final int ktemp = k;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        SocketChannel socketChannel = SocketChannel.open();
                        socketChannel.configureBlocking(true);
                        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), (9000)));
                        if (!socketChannel.isConnected())
                        Thread.sleep(10000);
                        for (int i = 0; i < 100; i++) {
                            ByteBuffer buffer = ByteBuffer.allocate(1000);
                            buffer.put(("hello netx" + i + "第" + ktemp + "个").getBytes());
                            buffer.flip();
                            socketChannel.write(buffer);
//                            System.out.println(socketChannel.validOps());
                            Thread.sleep(10000);
                        }
                        socketChannel.close();
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("第 " + ktemp + " 连接失败");
                    }
                }
            };
            executorService.execute(runnable);
        }
        executorService.shutdown();
//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), (8090)));
//        Thread.sleep(10000);
//        for (int i = 0; i < 100; i++) {
//            ByteBuffer buffer = ByteBuffer.allocate(1000);
//            buffer.put(("hello netx" + i + "第0个").getBytes());
//            buffer.flip();
//            socketChannel.write(buffer);
//            System.out.println(socketChannel.validOps());
//            Thread.sleep(10000);
//        }
//        socketChannel.close();
//        Thread.sleep(10000);

    }
}
