package io.netx.net;

public interface ChannelPipeline extends ChannelInboundInvoker, ChannelOutboundInvoker {
    ChannelPipeline addLast(Handler handler);
    ChannelPipeline remove(Handler handler);
}
