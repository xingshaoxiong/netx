package io.netx.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class DefaultChannel {
    private SelectableChannel selectableChannel;
    private SelectionKey selectionKey;
    private volatile SocketAddress localAddress;
    private volatile SocketAddress remoteAddress;
    private volatile EventLoop eventLoop;
    private ChannelHandler handler;
    private ByteBuffer byteBuffer;

    public DefaultChannel(SelectableChannel selectableChannel, EventLoop eventLoop) {
        this.selectableChannel = selectableChannel;
        this.eventLoop = eventLoop;
    }

    public SelectableChannel getChannel() {
        return selectableChannel;
    }

    public EventLoop getEventLoop() {
        return eventLoop;
    }

    void handleEvent() throws IOException {
        if (selectionKey.isReadable()) {
            if (selectableChannel instanceof SocketChannel) {
                ((SocketChannel) selectableChannel).read(byteBuffer);
            }
        }
        if (selectionKey.isAcceptable()) {
            if (selectableChannel instanceof ServerSocketChannel) {
                ((ServerSocketChannel) selectableChannel).accept();
            }
        }
    }
}
