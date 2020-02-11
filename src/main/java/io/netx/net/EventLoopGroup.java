package io.netx.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventLoopGroup {
    private final EventLoop boss;

    List<EventLoop> children;
    private final int size;
    private int index = 0;

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

    public EventLoopGroup(int size) throws IOException {
        this(null, size);
    }

    public EventLoopGroup(EventLoop boss, int size) throws IOException {
        if (boss == null) {
            boss = new EventLoop(100,Executors.newSingleThreadExecutor(), null, null);
        }
        this.boss = boss;
        this.size = size;
        children = new ArrayList<>(size);
    }

    public void start() throws IOException {
        for (int i = 0; i < size; i++) {
            EventLoop eventLoop = new EventLoop(100, Executors.newSingleThreadExecutor(), null, null);
            children.add(eventLoop);
        }

    }
}
