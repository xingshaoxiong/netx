package io.netx.net;

import java.io.IOException;
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
    void bind(SocketAddress localAddress) throws Exception;

    @Override
    void connect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception;

    @Override
    void close() throws Exception;

    @Override
    ChannelPipeline read() throws Exception;

    @Override
    void write(Object msg) throws IOException;
}
