package com.qingchen.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务的启动器
 */
public class ChatServer {
    public static void main(String[] args) throws InterruptedException {
        //定义主从线程池
        //定义主线程池,用于接受客户端的链接,但是不做任何处理,比如老板会谈业务,拉到业务就交给员工去做了
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //定义从线程池,处理主线程池交过来的任务,公司职员开展业务,完成老板交代的任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //构建Netty服务器
        try {
            ServerBootstrap server=new ServerBootstrap();//服务的启动类
            server.group(bossGroup,workerGroup)//把主从线程池组放入启动类中
                    .channel(NioServerSocketChannel.class)//设置Nio的双向通道
                    .childHandler(new HttpServerInitializer());//设置处理器,用于处理workerGroup

            ChannelFuture channelFuture = server.bind(875).sync();//启动server,并且绑定端口号为875,同时启动方式为同步
            //请求http://127.0.0.1:875/
            //监听关闭的channel
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅的关闭线程池组
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
        }
    }

}
