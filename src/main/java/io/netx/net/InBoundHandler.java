package io.netx.net;

public interface InBoundHandler extends Handler {
    void channelActive(ChannelHandlerContext ctx);

    void channelInActive(ChannelHandlerContext ctx);

    void channelRead(ChannelHandlerContext ctx, Object msg);
}
