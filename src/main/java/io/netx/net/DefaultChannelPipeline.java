package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.SocketAddress;
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


    final class TailContext extends DefaultChannelHandlerContext implements ChannelInboundHandler {
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
    }

    final class HeadContext extends DefaultChannelHandlerContext implements ChannelOutboundHandler, ChannelInboundHandler {

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
        public void bind(ChannelHandlerContext ctx, SocketAddress localAddress) throws Exception {

        }

        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {

        }

        @Override
        public void close(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg) {

        }
    }
}
