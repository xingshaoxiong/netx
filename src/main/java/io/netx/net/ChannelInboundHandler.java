package io.netx.net;

public interface ChannelInboundHandler extends ChannelHandler {
    void channelActive(ChannelHandlerContext ctx);

    void channelInActive(ChannelHandlerContext ctx);

    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

}
