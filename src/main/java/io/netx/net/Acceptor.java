package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Acceptor implements Runnable {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Acceptor.class);
    private ServerSocketChannel serverSocketChannel;
    private EventLoopGroup loopGroup;
    private EventLoop loop;

    private ExecutorService executorService;

    private List<ChannelHandler> handlerList = null;

    public Acceptor(ServerSocketChannel serverSocketChannel, EventLoopGroup loopGroup) {
        this.serverSocketChannel = serverSocketChannel;
        this.loopGroup = loopGroup;
        this.loop = loopGroup.getBoss();
        executorService = Executors.newFixedThreadPool(20);
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
        try {
            serverSocketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (loop.getFlag().get()) {
            final SocketChannel channel;
            try {
                channel = serverSocketChannel.accept();
                //为了验证之前连接失败的原因，怀疑是缓冲区用完，类似全连接队列大小，验证成功，全连接队列50
//                Thread.sleep(100000);
                if (channel != null) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
//                            logger.info("new connection: " + channel.toString());
                            try {
                                channel.configureBlocking(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Register register = new Register(channel, SelectionKey.OP_READ, loopGroup);
                            register.setHandlerList(handlerList);
                            register.run();
                        }
                    };
                    executorService.execute(runnable);
//                    logger.info("Register run success");
//                    boolean result = loop.getTasks().offer(register);
//                    if (result) {
//                        loop.getSelector().getSelector().wakeup();
//                    }
                }

            } catch (IOException e) {
                logger.error("Accept failed");
            }
        }
    }
}
