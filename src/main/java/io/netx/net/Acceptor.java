package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Acceptor implements Runnable {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Acceptor.class);
    private ServerSocketChannel serverSocketChannel;
    private EventLoopGroup loopGroup;
    private EventLoop loop;

    private List<ChannelHandler> handlerList = null;

    public Acceptor(ServerSocketChannel serverSocketChannel, EventLoopGroup loopGroup) {
        this.serverSocketChannel = serverSocketChannel;
        this.loopGroup = loopGroup;
        this.loop = loopGroup.getBoss();
    }

    public void setHandlerList(List<ChannelHandler> handlerList) {
        this.handlerList = handlerList;
    }

    public void start() {
        if (loop.inEventLoop()) {
            run();
        } else {
//            loop.getExecutors().execute(this);
            loop.getTasks().offer(this);
        }
    }

    @Override
    public void run() {
        while (loop.getFlag().get()) {
            SocketChannel channel = null;
            try {
                channel = serverSocketChannel.accept();
                logger.info("new connection: " + channel.toString());
                channel.configureBlocking(false);
                Register register = new Register(channel, SelectionKey.OP_READ, loopGroup);
                register.setHandlerList(handlerList);
                register.run();
//                boolean result = loop.getTasks().offer(register);
//                if (result) {
//                    loop.getSelector().getSelector().wakeup();
//                }
            } catch (IOException e) {
                logger.error("Accept failed");
            }
        }
    }
}
