package io.netx.concurrent;

import io.netx.net.EventLoop;
import sun.misc.Unsafe;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class AbstractChannelFuture<V> implements ChannelFuture<V>, Callable<V> , Runnable{
    private EventLoop eventLoop;

    private volatile Object result;

    private static final Object SUCCESS = new Object();

    private List<ChannelFutureListener<V>> listeners = new ArrayList<>();

    public AbstractChannelFuture(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    @Override
    public EventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public V getNow() {
        if (result == null) {
            return null;
        }
        return (V) result;
    }

    @Override
    public Throwable cause() {
        if (result != null && result instanceof CauseHolder) {
            return ((CauseHolder) result).cause;
        }
        return null;
    }

    @Override
    public ChannelFuture<V> await() throws InterruptedException {
        return await0(true);
    }

    private ChannelFuture<V> await0(boolean interruptable) throws InterruptedException {
        if (isDone()) {
            return this;
        }

        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(toString());
        }

        boolean interrupted = false;
        synchronized (this) {
            while (!isDone()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    if (interruptable) {
                        throw e;
                    } else {
                        interrupted = true;
                    }
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return this;
    }

    @Override
    public ChannelFuture<V> addListener(ChannelFutureListener<V> listener) throws Exception {
        synchronized (this) {
            listeners.add(listener);
        }
        if (isDone()) {
            notifyListeners();
        }
        return this;
    }

    @Override
    public ChannelFuture<V> removeListener(ChannelFutureListener<V> listener) {
        if (isDone()) {
            return this;
        }
        synchronized (this) {
            listeners.remove(listener);
        }
        return this;
    }

    @Override
    public V call() throws Exception {
        return null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        }

        synchronized (this) {
            if (isDone()) {
                return false;
            }
            result = new CauseHolder(new CancellationException("Canceled"));
            notifyAll();
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return result != null && result instanceof CauseHolder && ((CauseHolder) result).cause instanceof CancellationException;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        await();
        Throwable cause = cause();
        if (cause == null) {
            return getNow();
        }
        if (cause instanceof CancellationException) {
            throw (CancellationException) cause;
        }
        throw new ExecutionException(cause);
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public ChannelFuture<V> setSuccess(V result) throws Exception {
        if (setSuccess0(result)) {
            notifyListeners();
            return this;
        }
        throw new Exception("SetSuccess failed");
    }

    private boolean setSuccess0(V result) {
        return setValue0(result == null ? SUCCESS : result);
    }

    private boolean setValue0(Object objResult) {
        if(result != null) {
            return false;
        }
        synchronized (this) {
            if (result != null) {
                return false;
            }
            result = objResult;
            notifyAll();
        }
        return true;
    }

    public ChannelFuture<V> setFailure(Throwable cause) throws Exception {
        if (setFailure0(cause)) {
            notifyListeners();
            return this;
        }
        throw new Exception("SetFailure failed");
    }

    private boolean setFailure0(Throwable cause) {
        if (isDone()) {
            return false;
        }
        synchronized (this) {
            if (isDone()) {
                return false;
            }
            result = new CauseHolder(cause);
            notifyAll();

        }
        return true;
    }

    private static final class CauseHolder {
        final Throwable cause;

        CauseHolder(Throwable cause) {
            this.cause = cause;
        }
    }

    protected void notifyListeners() throws Exception {
        for (ChannelFutureListener<V> listener : listeners) {
            notifyListener(listener);
        }

    }

    protected void notifyListener(ChannelFutureListener<V> listener) throws Exception {
        listener.operationComplete(this);
    }

    @Override
    public void run() {
        try {
            V res = call();
            System.out.println("call() is called");
            setSuccess(res);
        } catch (Exception e) {
            try {
                setFailure(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public EventLoop getEventLoop() {
        return eventLoop;
    }
}
