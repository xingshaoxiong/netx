package io.netx.net.test;

import io.netx.net.ChannelHandlerContext;
import io.netx.net.ChannelInBoundHandlerAdapter;
import io.netx.net.Server;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerTest {

    @Test
    public void ServerTest() throws IOException, InterruptedException {
        Server server = new Server(2, 8090);
        server.addHandler(new ChannelInBoundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                System.out.println("ChannelActiveTest active");
                ctx.fireChannelActive();
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Charset charset = Charset.forName("utf-8");
                String msg0 = charset.decode((ByteBuffer) msg).toString();
                System.out.println("直接打印msg ：" + msg.toString());
                System.out.println(msg0);
            }
        });
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    server.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
        server.start();
        Thread.sleep(100000);
//        Socket socket = new Socket();
//        socket.connect(new InetSocketAddress("localhost", 8090));
//        BufferedOutputStream buf = new BufferedOutputStream(socket.getOutputStream());
//        buf.write("hello netx".toString().getBytes());
//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8090));
//        ByteBuffer buffer = ByteBuffer.allocate(1000);
//        buffer.put("hello netx".getBytes());
//        socketChannel.write(buffer);
//        Thread.sleep(1000000);
//        server.close();
        server.close();
    }

}
