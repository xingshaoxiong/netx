package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoop implements Runnable{
//    private boolean looping;
      private long threadId;
//    private ThreadLocal<EventLoop> loopInThread;
//    private DefaultSelector selector;
    private final ExecutorService executors;
    private final DefaultSelector selector;
    private ServerSocketChannel serverSocketChannel;
    private List<SocketChannel> channelList;
    private int maxChannels = 100;

    private AtomicBoolean flag;

    public DefaultSelector getSelector() {
        return selector;
    }

    public ExecutorService getExecutors() {
        return executors;
    }

    public BlockingQueue<Runnable> getTasks() {
        return tasks;
    }

    private BlockingQueue<Runnable> tasks;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(EventLoop.class);
    private static final Runnable CLOSE = () -> {};

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }



    public EventLoop(int maxtasks, ExecutorService executors, AtomicBoolean flag, ServerSocketChannel serverSocketChannel, SocketChannel socketChannel) throws IOException {
//        looping = false;
//        threadId = Thread.currentThread().getId();
//        if (loopInThread.get() != null) {
//            throw new ExceptionInInitializerError("currentThead has created an EventLoop");
//        } else {
//            loopInThread.set(this);
//        }
        this.tasks = new ArrayBlockingQueue<Runnable>(maxtasks);
        this.executors = executors;
        this.flag = flag;
        this.selector = new DefaultSelector(this);
        this.serverSocketChannel = serverSocketChannel;
        channelList = new ArrayList<>();
        executors.execute(new Runnable() {
            @Override
            public void run() {
                threadId = Thread.currentThread().getId();
            }
        });
    }

    public AtomicBoolean getFlag() {
        return flag;
    }

    public boolean addChannel(SocketChannel channel) {
        if (channelList.size() < maxChannels) {
            return channelList.add(channel);
        } else {
            return false;
        }
    }

//    boolean isInLoopThread() {
//        return threadId == Thread.currentThread().getId();
//    }
//
//    public void loop(){
//
//    }
//
//    public void updateChannel(DefaultChannel channel) {
//        selector.updateChannel(channel);
//    }

    public boolean submit(Runnable task) {
        return tasks.offer(task);
    }
    public void start() {
        executors.execute(this);
    }

    public void close() {
        tasks.add(CLOSE);
    }

    @Override
    public void run() {
        try {
            logger.info("EventLoop started, Thread: " + Thread.currentThread().toString());
            while (flag.get()) {
                Runnable task = tasks.poll(3, TimeUnit.SECONDS);
                if (task == CLOSE) {
                    break;
                }
                if (task != null) {
                    task.run();
                    continue;
                }
//                logger.info("goto selctor successfully");
                Selector selector = getSelector().getSelector();
//                int nums = selector.select(2);
                int nums = selector.select();
                if (nums == 0) {
                    System.out.println("nums == 0");
                    continue;
                }
                Set keySet = selector.selectedKeys();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    logger.info("find SelectionKey");
                    SelectionKey key = it.next();
                    Object att = key.attachment();
                    ChannelPipeline pipeline;
                    if (att instanceof ChannelPipeline) {
                        pipeline = (ChannelPipeline)att;
                    } else {
                        continue;
                    }
                    if (key.isReadable()) {
                        if (inEventLoop()) {
                            ByteBuffer buffer = ByteBuffer.allocate(1000);
                            ((SocketChannel)key.channel()).read(buffer);
                            pipeline.fireChannelRead(buffer);
                        } else {
                            tasks.offer(new Runnable() {
                                @Override
                                public void run() {
                                    pipeline.fireChannelRead(null);
                                }
                            });
                        }
                    }
                    it.remove();
                }
            }
        } catch (Exception e) {
            logger.error("Thread was interrupted.");
        }
    }

    boolean inEventLoop() {
        return threadId == Thread.currentThread().getId();
    }
}
