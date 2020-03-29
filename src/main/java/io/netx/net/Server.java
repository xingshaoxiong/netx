package io.netx.net;

import io.netx.concurrent.ChannelFuture;
import io.netx.concurrent.ChannelFutureListener;
import io.netx.concurrent.DefaultChannelFuture;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    int size = 10;
    EventLoopGroup loopGroup;
    ServerSocketChannel serverSocketChannel;
    List<ChannelHandler> handlerList;
    private int port = 8090;
    AtomicBoolean flag = new AtomicBoolean(true);

    public Server(int size, int port) throws IOException {
        this.size = size;
        this.port = port;
        loopGroup = new EventLoopGroup(size, flag);
        handlerList = new ArrayList<>();
    }

    public void start() throws IOException, InterruptedException {
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

    public synchronized void close() throws IOException, InterruptedException {
        flag.compareAndSet(true, false);
//        notify();
        loopGroup.getBoss().getExecutors().shutdown();
        for (EventLoop eventLoop : loopGroup.children) {
//            while(eventLoop.getTasks().size() != 0 )
//            {
//
//            }
            eventLoop.getExecutors().shutdown();
            eventLoop.getSelector().getSelector().wakeup();
        }
        //Debug
        Thread.sleep(10000);
        serverSocketChannel.close();
    }

    public void addHandler(ChannelHandler handler) {
        handlerList.add(handler);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(30, 9000);
        server.addHandler(new ChannelInBoundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) {
//                System.out.println("ChannelActiveTest active");
                ctx.fireChannelActive();
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                Charset charset = Charset.forName("utf-8");
//                String msg0 = charset.decode((ByteBuffer) msg).toString();
                if (msg instanceof ByteBuffer) {
                    ByteBuffer buffer = (ByteBuffer) msg;
                    if (buffer.position() != 0) {
                        buffer.flip();
                    }
                    int cap = buffer.limit();
//                    System.out.println("position: " + buffer.position());
//                    System.out.println("limit: " + buffer.limit());
                    byte[] bytes = new byte[cap];
                    for (int i = 0; i < cap; i++) {
                        bytes[i] = buffer.get(i);
                    }
                    System.out.println("直接打印msg ：" + new String(bytes));
                    String s = "HTTP/1.1 200\r\nStatus: 200 OK\r\nContent-Length: 100\r\nConnection: close\r\nContent-Type: text/html; charset=utf-8\r\n\r\n<h1>Test</h1>";
                    ByteBuffer buffer1 = ByteBuffer.allocate(1000);
                    buffer1.put(s.getBytes());
                    buffer1.flip();
                    ctx.write(buffer1);
                    buffer1 = null;
//                    Iterator it = ctx.getEventLoop().getSelector().getSelector().keys().iterator();
//                    while (it.hasNext()) {
//                        SelectionKey key = (SelectionKey)it.next();
//                        if(key.channel() == ctx.getChannel()) {
//                            key.cancel();
                            ctx.getChannel().close();
//                        }
//                    }
//                    //为了观察垃圾回收
//                    String s = new String(bytes);
//                    for (int i = 0; i < 15; i++) {
//                        s += s;
//                    }
//                    s.intern();
//                    System.out.println("观察垃圾回收 ： " + s);
                }
//                System.out.println(msg0);
            }
        });
        server.addHandler(new ChannelOutboundHandlerAdapter() {
            @Override
            public ChannelFuture<Void> closeAsyc(ChannelHandlerContext ctx, ChannelFuture<Void> future) throws Exception {
//                DefaultChannelFuture<Void> future0 = new DefaultChannelFuture<Void>(){
//                    @Override
//                    public Void call() throws Exception {
//                        ctx.closeAsyc(this);
//                        return null;
//                    }
//                };
//                future = future0;
//                future.addListener(new ChannelFutureListener<Void>() {
//                    @Override
//                    public void operationComplete(ChannelFuture<Void> future) throws Exception {
//                        System.out.println("Server Close asyc");
//                    }
//                });
//                return future;
                return ctx.closeAsyc(future);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg) throws IOException {
                super.write(ctx, msg);
            }
        });
        server.start();
        Thread.sleep(6000000);
        server.close();

    }
}
