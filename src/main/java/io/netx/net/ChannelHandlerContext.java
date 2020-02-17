package io.netx.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public interface ChannelHandlerContext extends ChannelInboundInvoker, ChannelOutboundInvoker {
    @Override
    ChannelHandlerContext fireChannelActive();

    @Override
    ChannelHandlerContext fireChannelInactive();

    @Override
    ChannelHandlerContext fireChannelRead(Object msg) throws Exception;

    @Override
    void bind(SocketAddress localAddress) throws Exception;

    @Override
    void connect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception;

    @Override
    void close() throws Exception;

    @Override
    ChannelHandlerContext read() throws Exception;

    @Override
    void write(Object msg) throws IOException;

    SocketChannel getChannel();

    EventLoop getEventLoop();

    ChannelPipeline getPipeline();

    ChannelHandler handler();
}
