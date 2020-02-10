package io.netx.net;

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
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelInActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg) {

    }
}
