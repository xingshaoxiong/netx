package io.netx.net;

import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;

public interface ChannelPipeline extends ChannelInboundInvoker, ChannelOutboundInvoker {

    ChannelPipeline addLast(ChannelHandler handler);

    ChannelPipeline remove(ChannelHandler handler);

    @Override
    ChannelPipeline fireChannelActive();

    @Override
    ChannelPipeline fireChannelInactive();

    @Override
    ChannelPipeline fireChannelRead(Object msg);

    @Override
    void bind(SocketAddress localAddress);

    @Override
    void connect(SocketAddress remoteAddress, SocketAddress localAddress);

    @Override
    void close();

    @Override
    ChannelPipeline read() throws ClosedChannelException;

    @Override
    void write(Object msg);
}
