package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Register implements Runnable {
    private List<ChannelHandler> handlerList;
    private final SocketChannel channel;
    private final int ops;
    private DefaultSelector defaultSelector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Register.class);
    private EventLoopGroup eventLoopGroup;
    public Register(SocketChannel channel, int ops, EventLoopGroup eventLoopGroup) {
        this.channel = channel;
        this.ops = ops;
        this.defaultSelector = eventLoopGroup.getBoss().getSelector();
        this.eventLoopGroup = eventLoopGroup;
        defaultSelector.setLoopGroup(eventLoopGroup);
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
        this.defaultSelector = eventLoopGroup.getBoss().getSelector();
    }

    public List<ChannelHandler> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<ChannelHandler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public void run() {
        try {
            int index = eventLoopGroup.getIndex();
            int size = eventLoopGroup.getSize();
            if (index <= size) {
                index = 0;
            }
            EventLoop eventLoop = eventLoopGroup.children.get(index);
            //目前accept单线程，这个没有必要，不会出现竞争，但如果accept是线程池，是有必要的
            if (!eventLoop.isStartThread()) {
                synchronized (eventLoop) {
                    if (!eventLoop.isStartThread()) {
                        eventLoop.doStartThread();
//                        eventLoop.notify();
                    }
                }
            }
            eventLoop.addChannel(channel);
            ChannelPipeline pipeline = new DefaultChannelPipeline(channel, eventLoop);
            for (ChannelHandler handler : handlerList) {
                pipeline.addLast(handler);
            }
            boolean success = false;
            eventLoop.getSelector().getSelector().wakeup();
            SelectionKey key = channel.register(eventLoop.getSelector().getSelector(), ops);
            key.attach(pipeline);
            pipeline.fireChannelActive();
            eventLoop.addPipeline(pipeline);
            eventLoop.getSelector().getSelector().wakeup();
//            logger.info("pipeline fireChannelActive");
        } catch (ClosedChannelException e) {
            logger.error("Register Failed");
        }
    }
}
