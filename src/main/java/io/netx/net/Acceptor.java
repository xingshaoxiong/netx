package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Acceptor.class);
    private ServerSocketChannel serverSocketChannel;
    private EventLoop loop;

    public Acceptor(ServerSocketChannel serverSocketChannel, EventLoop loop) {
        this.serverSocketChannel = serverSocketChannel;
        this.loop = loop;
    }
    @Override
    public void run() {
        SocketChannel channel = null;
        try {
            channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            boolean result = loop.getTasks().offer(new Register(channel, SelectionKey.OP_READ, loop.getSelector()));
            if (result) {
                loop.getSelector().getSelector().wakeup();
            }
        } catch (IOException e) {
            logger.error("Accept failed");
        }
    }
}
