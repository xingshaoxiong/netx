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
    private Thread thread;

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
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                this.run();
//            }
//        };
//        threadId = thread.getId();

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
//        executors.execute(this);
        //下面这种也可以，因为使用了阻塞队列，其实，对于之前的单线程线程池而言，阻塞队列完全是多余的，之所以出现这个情况
        //是因为一开始没有想好技术方案，随写随改，后续可以对这一部分进行修改。
        //TODO
        Thread thread = new Thread(this);
        this.threadId = thread.getId();
        thread.start();
        this.thread = thread;
    }

    public void close() {
        tasks.add(CLOSE);
    }

    @Override
    public void run() {
        try {
            logger.info("EventLoop started, Thread: " + Thread.currentThread().toString());
            while (flag.get()) {
                while(tasks.size() > 0) {
                    Runnable task = tasks.poll(3, TimeUnit.SECONDS);
                    if (task == CLOSE) {
                        break;
                    }
                    if (task != null) {
                        try {
                            task.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                            flag.compareAndSet(true, false);
                            break;
                        }
                    }
                }
                if (!flag.get()) {
                    break;
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
                            logger.info("Has goto inEventLoop()");
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            ((SocketChannel)key.channel()).read(buffer);
                            System.out.println("Buffer.position: " + buffer.position());
                            System.out.println("Buffer.limit(): " + buffer.limit());
                            buffer.flip();
                            System.out.println("Buffer has fliped");
                            System.out.println("Buffer.position: " + buffer.position());
                            System.out.println("Buffer.limit(): " + buffer.limit());
                            pipeline.fireChannelRead(buffer);
                        } else {
                            tasks.offer(() -> {
                                try {
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                                    ((SocketChannel)key.channel()).read(buffer);
                                    System.out.println("Buffer.position: " + buffer.position());
                                    System.out.println("Buffer.limit(): " + buffer.limit());
                                    buffer.flip();
                                    System.out.println("Buffer has fliped");
                                    System.out.println("Buffer.position: " + buffer.position());
                                    System.out.println("Buffer.limit(): " + buffer.limit());
                                    pipeline.fireChannelRead(buffer);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    flag.compareAndSet(true, false);
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
//        return threadId == Thread.currentThread().getId();
        return Thread.currentThread() == thread;
    }
}
