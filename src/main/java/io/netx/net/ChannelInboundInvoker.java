package io.netx.net;


public interface ChannelInboundInvoker {

    ChannelInboundInvoker fireChannelActive();

    ChannelInboundInvoker fireChannelInactive();

    ChannelInboundInvoker fireChannelRead(Object msg) throws Exception;

}
