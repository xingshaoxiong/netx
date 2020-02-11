package io.netx.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;

public interface ChannelOutboundInvoker {
    void bind(SocketAddress localAddress) throws Exception;

    void connect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception;

    void close() throws Exception;

    ChannelOutboundInvoker read() throws Exception;

    void write(Object msg) throws IOException;

}
