package io.netx.net;

import java.net.SocketAddress;

public interface ChannelOutboundHandler extends Handler{
    void bind(ChannelHandlerContext ctx, SocketAddress localAddress) throws Exception;

    void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress) throws Exception;

    void close(ChannelHandlerContext ctx) throws Exception;

    void read(ChannelHandlerContext ctx) throws Exception;

    void write(ChannelHandlerContext ctx, Object msg);
}
