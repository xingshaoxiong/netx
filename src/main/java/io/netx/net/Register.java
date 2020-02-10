package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Register implements Runnable {
    private final SocketChannel channel;
    private final int ops;
    private DefaultSelector defaultSelector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Register.class);
    private EventLoopGroup eventLoopGroup;
    public Register(SocketChannel channel, int ops, DefaultSelector defaultSelector) {
        this.channel = channel;
        this.ops = ops;
        this.defaultSelector = defaultSelector;
        eventLoopGroup = null;
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
        this.defaultSelector = eventLoopGroup.getBoss().getSelector();
    }

    @Override
    public void run() {
        try {
            SelectionKey key = channel.register(defaultSelector.getSelector(), ops);
            int index = eventLoopGroup.getIndex();
            int size = eventLoopGroup.getSize();
            if (index <= size) {
                index = 0;
            }
            EventLoop eventLoop = eventLoopGroup.children.get(index);
            eventLoop.addChannel(channel);
            key.attach(eventLoop);
        } catch (ClosedChannelException e) {
            logger.error("Register Failed");
        }
    }
}
