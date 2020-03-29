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
        ExecutorService executorService = Executors.newFixedThreadPool(2000);
        for (int k = 0; k < 2000; k++) {
            final int ktemp = k;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        SocketChannel socketChannel = SocketChannel.open();
                        socketChannel.configureBlocking(true);
                        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), (9000)));
                        ByteBuffer buffer = ByteBuffer.allocate(1);
                        buffer.put(("1").getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
//                        for (int i = 0; i < 100; i++) {
//                            ByteBuffer buffer = ByteBuffer.allocate(1000);
//                            buffer.put(("hello netx" + i + "第" + ktemp + "个").getBytes());
//                            buffer.flip();
//                            socketChannel.write(buffer);
//                            System.out.println(socketChannel.validOps());
//                            Thread.sleep(10000);
//                        }
                        ByteBuffer buffer1 = ByteBuffer.allocate(8);
                        while (true) {
                            int num = socketChannel.read(buffer1);
                            if (num == -1) {
                                break;
                            }
                            buffer1.flip();
//                            System.out.println(buffer1.position());
//                            System.out.println(buffer1.limit());
                            byte[] bytes = new byte[buffer1.limit()];
                            for (int i = 0; i <bytes.length; i++) {
                                bytes[i] = buffer1.get(i);
                            }
                            System.out.println(new String(bytes));
                        }

                        Thread.sleep(10000);
                        socketChannel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("第 " + ktemp + " 连接失败");
                    }
                }
            };
            executorService.execute(runnable);
        }
        Thread.sleep(1000000);
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
