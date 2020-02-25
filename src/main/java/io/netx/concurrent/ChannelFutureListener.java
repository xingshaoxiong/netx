package io.netx.concurrent;

import java.util.concurrent.Future;

public interface ChannelFutureListener<V> {
    void operationComplete(ChannelFuture<V> future) throws Exception;
}
