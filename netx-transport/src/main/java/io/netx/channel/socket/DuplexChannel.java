/*
 * Copyright 2016 The Netty Project
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
package io.netx.channel.socket;

import io.netx.channel.Channel;
import io.netx.channel.ChannelFuture;
import io.netx.channel.ChannelPromise;

import java.net.Socket;


public interface DuplexChannel extends Channel {

    boolean isInputShutdown();

    /**
     * @see Socket#shutdownInput()
     */
    ChannelFuture shutdownInput();

    /**
     * Will shutdown the input and notify {@link ChannelPromise}.
     *
     * @see Socket#shutdownInput()
     */
    ChannelFuture shutdownInput(ChannelPromise promise);

    /**
     * @see Socket#isOutputShutdown()
     */
    boolean isOutputShutdown();

    /**
     * @see Socket#shutdownOutput()
     */
    ChannelFuture shutdownOutput();

    /**
     * Will shutdown the output and notify {@link ChannelPromise}.
     *
     * @see Socket#shutdownOutput()
     */
    ChannelFuture shutdownOutput(ChannelPromise promise);

    /**
     * Determine if both the input and output of this channel have been shutdown.
     */
    boolean isShutdown();

    /**
     * Will shutdown the input and output sides of this channel.
     * @return will be completed when both shutdown operations complete.
     */
    ChannelFuture shutdown();

    /**
     * Will shutdown the input and output sides of this channel.
     * @param promise will be completed when both shutdown operations complete.
     * @return will be completed when both shutdown operations complete.
     */
    ChannelFuture shutdown(ChannelPromise promise);
}
