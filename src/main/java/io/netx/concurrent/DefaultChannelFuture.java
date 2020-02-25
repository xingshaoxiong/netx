package io.netx.concurrent;

import io.netx.net.DefaultChannel;
import io.netx.net.EventLoop;

public class DefaultChannelFuture<V> extends AbstractChannelFuture<V> {

    public DefaultChannelFuture() {
        super(null);
    }

    public DefaultChannelFuture(EventLoop eventLoop) {
        super(eventLoop);
    }
    @Override
    public ChannelFuture addListener(ChannelFutureListener listener) throws Exception {
        return super.addListener(listener);
    }

    @Override
    public V call() throws Exception {
        return super.call();
    }
}
