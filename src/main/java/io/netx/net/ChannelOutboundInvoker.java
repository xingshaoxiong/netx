package io.netx.net;

import java.net.SocketAddress;

public interface ChannelOutboundInvoker {
    void bind(SocketAddress localAddress);

    void connect(SocketAddress remoteAddress, SocketAddress localAddress);

    void close();

    ChannelOutboundInvoker read();

    void write(Object msg);

}
