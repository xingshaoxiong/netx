package io.netx.concurrent;

import io.netx.net.EventLoop;

import javax.swing.event.ChangeListener;
import java.nio.channels.Channel;
import java.util.concurrent.Future;

public interface ChannelFuture<V> extends Future<V> {
    EventLoop eventLoop();

    V getNow();

    ChannelFuture<V> await() throws InterruptedException;

    Throwable cause();

    ChannelFuture<V> addListener(ChannelFutureListener<V> listener) throws Exception;

    ChannelFuture<V> removeListener(ChannelFutureListener<V> listener);

}
