/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netx.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netx.channel.socket.ServerSocketChannel;
import io.netx.channel.socket.SocketChannel;
import io.netty.util.AttributeMap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface Channel extends AttributeMap, ChannelOutboundInvoker, Comparable<Channel> {

    String id();

    EventLoop eventLoop();

    boolean isOpen();

    boolean isRegistered();

    boolean isActive();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    ChannelFuture closeFuture();

    boolean isWritable();

    /**
     * Get how many bytes can be written until {@link #isWritable()} returns {@code false}.
     * This quantity will always be non-negative. If {@link #isWritable()} is {@code false} then 0.
     */
    long bytesBeforeUnwritable();

    long bytesBeforeWritable();

    Unsafe unsafe();

    ChannelPipeline pipeline();

    ByteBufAllocator alloc();

    @Override
    Channel read();

    @Override
    Channel flush();

    interface Unsafe {

        SocketAddress localAddress();

        SocketAddress remoteAddress();

        void register(EventLoop eventLoop, ChannelPromise promise);

        void bind(SocketAddress localAddress, ChannelPromise promise);

        void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise);

        void disconnect(ChannelPromise promise);

        void close(ChannelPromise promise);

        void closeForcibly();

        void deregister(ChannelPromise promise);

        void beginRead();

        void write(Object msg, ChannelPromise promise);

        void flush();

    }
}
