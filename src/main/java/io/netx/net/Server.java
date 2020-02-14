package io.netx.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    int size = 10;
    EventLoopGroup loopGroup;
    ServerSocketChannel serverSocketChannel;
    List<ChannelHandler> handlerList;
    private int port = 8090;
    AtomicBoolean flag = new AtomicBoolean(false);
    public Server(int size, int port) throws IOException {
        this.size = size;
        this.port = port;
        loopGroup = new EventLoopGroup(size, flag);
        handlerList = new ArrayList<>();
    }

    public  void start() throws IOException, InterruptedException {
        loopGroup.start();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(true);
        Acceptor acceptor = new Acceptor(serverSocketChannel, loopGroup);
        acceptor.setHandlerList(handlerList);
//        acceptor.start();
        loopGroup.getBoss().getExecutors().execute(acceptor);
        flag.compareAndSet(false, true);
//        while(flag.get() == true) {
//            wait();
//        }
//        serverSocketChannel.close();
//        for (EventLoop eventLoop : loopGroup.children) {
//            while(eventLoop.getTasks().size() != 0 )
//            {
//
//            }
//            eventLoop.getExecutors().shutdown();
//        }
//        loopGroup.getBoss().getExecutors().shutdown();
    }

    public synchronized void close() throws IOException {
        flag.compareAndSet(true, false);
//        notify();
        loopGroup.getBoss().getExecutors().shutdown();
        for (EventLoop eventLoop : loopGroup.children) {
//            while(eventLoop.getTasks().size() != 0 )
//            {
//
//            }
            eventLoop.getExecutors().shutdown();
        }
        serverSocketChannel.close();
    }

    public void addHandler(ChannelHandler handler) {
        handlerList.add(handler);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(10, 9000);
        server.start();
        Thread.sleep(10000);
        server.close();
    }
}
