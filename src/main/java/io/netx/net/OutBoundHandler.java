package io.netx.net;

public interface OutBoundHandler extends Handler{
    void write(ChannelHandlerContext ctx, Object msg);
}
