package io.netx.net;

public interface ChannelPipeline extends InBoundHandler, OutBoundHandler {
    ChannelPipeline addLast(Handler handler);
    ChannelPipeline remove(Handler handler);
}
