package io.netx.net;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class DefaultChannelHandlerContext implements ChannelHandlerContext {
    private SocketChannel channel;
    private EventLoop eventLoop;
    private ChannelPipeline pipeline;

    public DefaultChannelHandlerContext(SocketChannel channel, EventLoop eventLoop, ChannelPipeline pipeline) {
        this.channel = channel;
        this.eventLoop = eventLoop;
        this.pipeline = pipeline;
    }


    @Override
    public ChannelInboundInvoker fireChannelActive() {
        return null;
    }

    @Override
    public ChannelInboundInvoker fireChannelInactive() {
        return null;
    }

    @Override
    public ChannelInboundInvoker fireChannelRead(Object msg) {
        return null;
    }

    @Override
    public void bind(SocketAddress localAddress) {

    }

    @Override
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress) {

    }

    @Override
    public void close() {

    }

    @Override
    public ChannelOutboundInvoker read() {
        return null;
    }

    @Override
    public void write(Object msg) {

    }
}
