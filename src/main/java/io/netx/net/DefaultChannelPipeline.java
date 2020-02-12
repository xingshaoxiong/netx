package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

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
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public synchronized ChannelPipeline addLast(ChannelHandler handler) {
        DefaultChannelHandlerContext ctx = new DefaultChannelHandlerContext(channel, eventLoop, this, handler);
        DefaultChannelHandlerContext temp = tail.prev;
        temp.next = ctx;
        ctx.prev = temp;
        tail.prev = ctx;
        return this;
    }

    @Override
    public synchronized ChannelPipeline remove(ChannelHandler handler) {
        DefaultChannelHandlerContext ctx = head.next;
        while (ctx.handler() != handler) {
            ctx = ctx.next;
        }
        if (ctx == tail) {
            return this;
        }
        DefaultChannelHandlerContext prevCtx = ctx.prev;
        DefaultChannelHandlerContext nextCtx = ctx.next;
        ctx.prev = null;
        ctx.next = null;
        prevCtx.next = nextCtx;
        nextCtx.prev = prevCtx;
        return this;
    }

    @Override
    public ChannelPipeline fireChannelActive() {
        boolean result = false;
        if (eventLoop.inEventLoop()) {
            head.fireChannelActive();
        } else {
            result = eventLoop.getTasks().offer(() -> {
                head.fireChannelActive();
            });
        }
        //TODO
        if (result) {
            return this;
        } else {
            return this;
        }
    }

    @Override
    public ChannelPipeline fireChannelInactive() {
        boolean result = false;
        if (eventLoop.inEventLoop()) {
            head.fireChannelInactive();
        } else {
            result = eventLoop.getTasks().offer(() -> {
                head.fireChannelInactive();
            });
        }
        //TODO
        if (result) {
            return this;
        } else {
            return this;
        }
    }

    @Override
    public ChannelPipeline fireChannelRead(Object msg) {
        boolean result = false;
        if (eventLoop.inEventLoop()) {
            head.fireChannelRead(msg);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                head.fireChannelRead(msg);
            });
        }
        //TODO
        if (result) {
            return this;
        } else {
            return this;
        }
    }

    @Override
    public void bind(SocketAddress localAddress) throws Exception {
        tail.bind(localAddress);
    }

    @Override
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        tail.connect(remoteAddress, localAddress);
    }

    @Override
    public void close() throws Exception {
        tail.close();
    }

    @Override
    public ChannelPipeline read() throws Exception {
        tail.read();
        return this;
    }

    @Override
    public void write(Object msg) throws IOException {
        tail.write(msg);
    }


    final class TailContext extends DefaultChannelHandlerContext implements ChannelInboundHandler {
        public TailContext(SocketChannel channel, EventLoop eventLoop, ChannelPipeline pipeline) {
            super(channel, eventLoop, pipeline, null);
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
            super(channel, eventLoop, pipeline, null);
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
            ctx.getChannel().connect(remoteAddress);
        }

        @Override
        public void close(ChannelHandlerContext ctx) throws Exception {
            ctx.getChannel().close();
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            Selector selector = eventLoop.getSelector().getSelector();
            Set<SelectionKey> set = selector.selectedKeys();
            for (SelectionKey key : set) {
                if (key.channel() == channel) {
                    channel.register(selector, key.interestOps() | SelectionKey.OP_READ);
                }
            }
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg) throws IOException {
            if (msg instanceof ByteBuffer) {
                channel.write((ByteBuffer) msg);
            } else {
                return;
            }
        }
    }
}
