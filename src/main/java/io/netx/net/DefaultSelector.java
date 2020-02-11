package io.netx.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultSelector {
    private EventLoop loop;

    private Selector selector;

    private List<DefaultChannel> channelList;

    public DefaultSelector(EventLoop loop) throws IOException {
        this.loop = loop;
        selector = Selector.open();
        channelList = new ArrayList<>();
    }

    public EventLoop getLoop() {
        return loop;
    }

    public Selector getSelector() {
        return selector;
    }

    public Set<SelectionKey> select() {
        return selector.selectedKeys();
    }

    public synchronized boolean register(SocketChannel channel, int ops) {
        Register register = new Register(channel, ops, this);
        boolean result = false;
        if (loop.inEventLoop()) {
            register.run();
            result = true;
        }
        result = loop.getTasks().offer(register);

        if (result) {
            // 唤醒Selector
            selector.wakeup();
        }
        return result;
    }


}
