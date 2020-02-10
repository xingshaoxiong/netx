package io.netx.net;

public interface ChannelInboundHandler extends Handler {
    void channelActive(ChannelHandlerContext ctx);

    void channelInActive(ChannelHandlerContext ctx);

    void channelRead(ChannelHandlerContext ctx, Object msg);

}
