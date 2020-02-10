package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

public class DefaultChannelPipeline implements ChannelPipeline {

    static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);

    private final SocketChannel channel;

    private final EventLoop eventLoop;

    private DefaultChannelHandlerContext head;

    private DefaultChannelHandlerContext tail;


    public DefaultChannelPipeline(SocketChannel channel, EventLoop eventLoop) {
        this.channel = channel;
        this.eventLoop = eventLoop;
        head = new HeadContext(channel, eventLoop, this);
        tail = new TailContext(channel, eventLoop, this);
    }




    @Override
    public ChannelPipeline addLast(Handler handler) {
        return null;
    }

    @Override
    public ChannelPipeline remove(Handler handler) {
        return null;
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

    final class TailContext extends DefaultChannelHandlerContext{
        public TailContext(SocketChannel channel, EventLoop eventLoop, ChannelPipeline pipeline) {
            super(channel, eventLoop, pipeline);
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

    final class HeadContext extends DefaultChannelHandlerContext{

        public HeadContext(SocketChannel channel, EventLoop eventLoop, ChannelPipeline pipeline) {
            super(channel, eventLoop, pipeline);
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
}
