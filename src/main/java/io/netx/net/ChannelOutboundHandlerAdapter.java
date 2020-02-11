package io.netx.net;

import java.io.IOException;
import java.net.SocketAddress;

public class ChannelOutboundHandlerAdapter implements ChannelOutboundHandler {

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress) throws Exception {
        ctx.bind(localAddress);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        ctx.connect(remoteAddress, localAddress);
    }

    @Override
    public void close(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg) throws IOException {
        ctx.write(msg);
    }
}
