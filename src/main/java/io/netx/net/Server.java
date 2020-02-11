package io.netx.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Server {
    int size = 10;
    EventLoopGroup loopGroup;
    ServerSocketChannel serverSocketChannel;
    List<ChannelHandler> handlerList;
    private int port = 8090;
    public Server(int size, int port) throws IOException {
        this.size = size;
        this.port = port;
        loopGroup = new EventLoopGroup(size);
        handlerList = new ArrayList<>();
    }

    public void start() throws IOException {
        loopGroup.start();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(true);
        Acceptor acceptor = new Acceptor(serverSocketChannel, loopGroup);
        acceptor.setHandlerList(handlerList);
        acceptor.start();
    }

    public void addHandler(ChannelHandler handler) {
        handlerList.add(handler);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(10, 9000);
        server.start();
    }
}
