package io.netx.net;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class EventLoop implements Runnable{
//    private boolean looping;
//    private long threadId;
//    private ThreadLocal<EventLoop> loopInThread;
//    private DefaultSelector selector;
    private final ExecutorService executors;
    private final DefaultSelector selector;
    private ServerSocketChannel serverSocketChannel;
    private List<SocketChannel> channelList;
    private int maxChannels = 100;

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



    public EventLoop(int maxtasks, ExecutorService executors, ServerSocketChannel serverSocketChannel, SocketChannel socketChannel) throws IOException {
//        looping = false;
//        threadId = Thread.currentThread().getId();
//        if (loopInThread.get() != null) {
//            throw new ExceptionInInitializerError("currentThead has created an EventLoop");
//        } else {
//            loopInThread.set(this);
//        }
        this.tasks = new ArrayBlockingQueue<Runnable>(maxtasks);
        this.executors = executors;
        this.selector = new DefaultSelector(this);
        this.serverSocketChannel = serverSocketChannel;
        channelList = new ArrayList<>();
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
            while (true) {
                Runnable task = tasks.take();
                if (task == CLOSE) {
                    break;
                }
                task.run();
            }
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted.");
        }
    }
}
