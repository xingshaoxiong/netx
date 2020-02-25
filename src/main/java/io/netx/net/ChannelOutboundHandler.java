package io.netx.net;

import io.netx.concurrent.ChannelFuture;

import java.io.IOException;
import java.net.SocketAddress;

public interface ChannelOutboundHandler extends ChannelHandler {

    void bind(ChannelHandlerContext ctx, SocketAddress localAddress) throws Exception;

    void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress) throws Exception;

    void close(ChannelHandlerContext ctx) throws Exception;

    void read(ChannelHandlerContext ctx) throws Exception;

    void write(ChannelHandlerContext ctx, Object msg) throws IOException;

    ChannelFuture<Void> closeAsyc(ChannelHandlerContext ctx, ChannelFuture<Void> future) throws Exception;
}
