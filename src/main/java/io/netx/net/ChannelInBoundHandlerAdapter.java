package io.netx.net;

public class ChannelInBoundHandlerAdapter implements ChannelInboundHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInActive(ChannelHandlerContext ctx) {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //对msg进行处理
        ctx.fireChannelRead(msg);
    }
}
