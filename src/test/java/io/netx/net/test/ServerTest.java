package io.netx.net.test;

import io.netx.net.ChannelHandlerContext;
import io.netx.net.ChannelInBoundHandlerAdapter;
import io.netx.net.Server;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerTest {

    @Test
    public void ServerTest() throws IOException {
        Server server = new Server(2, 8090);
        server.addHandler(new ChannelInBoundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                System.out.println("ChannelActiveTest active");
                ctx.fireChannelActive();
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                System.out.println(msg.toString());
            }
        });
        server.start();
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 8090));
        BufferedOutputStream buf = new BufferedOutputStream(socket.getOutputStream());
        buf.write(26);
        while(true) {

        }
    }

}
