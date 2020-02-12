package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class DefaultChannelHandlerContext implements ChannelHandlerContext {


    DefaultChannelHandlerContext next;

    DefaultChannelHandlerContext prev;

    private SocketChannel channel;

    private EventLoop eventLoop;

    private ChannelPipeline pipeline;

    private ChannelHandler handler;

    boolean isInbound;

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelHandlerContext.class);

    public DefaultChannelHandlerContext(SocketChannel channel, EventLoop eventLoop, ChannelPipeline pipeline, ChannelHandler handler) {
        this.channel = channel;
        this.eventLoop = eventLoop;
        this.pipeline = pipeline;
        this.handler = handler;
        if (handler instanceof ChannelInboundHandler) {
            this.isInbound = true;
        } else {
            this.isInbound = false;
        }
    }


    @Override
    public ChannelHandlerContext fireChannelActive() {
        final DefaultChannelHandlerContext ctx = findContextInbound();
        if (ctx == null) {
            return this;
        }
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelInboundHandler) ctx.handler).channelActive(ctx);
        } else {
            result = eventLoop.getTasks().offer(new Runnable() {
                @Override
                public void run() {
                    ((ChannelInboundHandler) ctx.handler).channelActive(ctx);
                }
            });
        }

        if (result) {
            return this;
        } else {
            //TODO
            return this;
        }
    }

    @Override
    public ChannelHandlerContext fireChannelInactive() {
        final DefaultChannelHandlerContext ctx = findContextInbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelInboundHandler) ctx.handler).channelInActive(ctx);
        } else {
            result = eventLoop.getTasks().offer(new Runnable() {
                @Override
                public void run() {
                    ((ChannelInboundHandler) ctx.handler).channelInActive(ctx);
                }
            });
        }
        if (result) {
            return this;
        } else {
            //TODO
            return this;
        }
    }

    @Override
    public ChannelHandlerContext fireChannelRead(Object msg) {
        final DefaultChannelHandlerContext ctx = findContextInbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelInboundHandler) ctx.handler).channelRead(ctx, msg);
        } else {
            result = eventLoop.getTasks().offer(() -> ((ChannelInboundHandler) ctx.handler).channelRead(ctx, msg));
        }
        if (result) {
            return this;
        } else {
            //TODO
            return this;
        }
    }

    private DefaultChannelHandlerContext findContextInbound() {
        DefaultChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (ctx!= null && !ctx.isInbound);
        return ctx;
    }

    private DefaultChannelHandlerContext findContextOutbound() {
        DefaultChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (ctx.isInbound);
        return ctx;
    }

    @Override
    public void bind(SocketAddress localAddress) throws Exception {
        final DefaultChannelHandlerContext ctx = findContextOutbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelOutboundHandler) ctx.handler()).bind(ctx, localAddress);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                try {
                    ((ChannelOutboundHandler) ctx.handler()).bind(ctx, localAddress);
                } catch (Exception e) {
                    logger.error("bind failed");
                }
            });
        }

        if (result) {
            //TODO
        }
    }

    @Override
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        final DefaultChannelHandlerContext ctx = findContextOutbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelOutboundHandler) ctx.handler()).connect(ctx, remoteAddress, localAddress);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                try {
                    ((ChannelOutboundHandler) ctx.handler()).connect(ctx, remoteAddress, localAddress);
                } catch (Exception e) {
                    logger.error("connect failed");
                }
            });
        }

        if (result) {
            //TODO
        }
    }

    @Override
    public void close() throws Exception {
        final DefaultChannelHandlerContext ctx = findContextOutbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelOutboundHandler) ctx.handler()).close(ctx);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                try {
                    ((ChannelOutboundHandler) ctx.handler()).close(ctx);
                } catch (Exception e) {
                    logger.error("close failed");
                }
            });
        }

        if (result) {
            //TODO
        }
    }

    @Override
    public ChannelHandlerContext read() throws Exception {
        final DefaultChannelHandlerContext ctx = findContextOutbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelOutboundHandler) ctx.handler()).read(ctx);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                try {
                    ((ChannelOutboundHandler) ctx.handler()).read(ctx);
                } catch (Exception e) {
                    logger.error("read failed");
                }
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
    public void write(Object msg) throws IOException {
        //TODO
        final DefaultChannelHandlerContext ctx = findContextOutbound();
        boolean result = true;
        if (eventLoop.inEventLoop()) {
            ((ChannelOutboundHandler) ctx.handler()).write(ctx, msg);
        } else {
            result = eventLoop.getTasks().offer(() -> {
                try {
                    ((ChannelOutboundHandler) ctx.handler()).write(ctx, msg);
                } catch (Exception e) {
                    logger.error("read failed");
                }
            });
        }
        //TODO
        if (result) {

        } else {

        }
    }

    @Override
    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public EventLoop getEventLoop() {
        return eventLoop;
    }

    @Override
    public ChannelPipeline getPipeline() {
        return pipeline;
    }

    @Override
    public ChannelHandler handler() {
        return handler;
    }
}
