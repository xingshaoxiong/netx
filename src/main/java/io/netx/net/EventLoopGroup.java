package io.netx.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventLoopGroup {
    private final EventLoop boss;

    List<EventLoop> children;
    private final int size;
    private int index = 0;

    private AtomicBoolean flag ;
    Register register;

    public int getSize() {
        return size;
    }

    public EventLoop getBoss() {
        return boss;
    }

    public int getIndex() {
        return index;
    }

    public EventLoopGroup(int size, AtomicBoolean flag) throws IOException {
        this(null, size, flag);
    }

    public EventLoopGroup(EventLoop boss, int size, AtomicBoolean flag) throws IOException {
        this.flag = flag;
        if (boss == null) {
            boss = new EventLoop(100,Executors.newSingleThreadExecutor(), flag, null, null);
        }
//        boss.doStartThread();
        this.boss = boss;
        this.size = size;
        children = new ArrayList<>(size);
//        boss.start();
    }

    public AtomicBoolean getFlag() {
        return flag;
    }

    public void start() throws IOException {
        for (int i = 0; i < size; i++) {
            EventLoop eventLoop = new EventLoop(100, Executors.newSingleThreadExecutor(), flag, null, null);
            eventLoop.start();
            children.add(eventLoop);
        }

    }
}
