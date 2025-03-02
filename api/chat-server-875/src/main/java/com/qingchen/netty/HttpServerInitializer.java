package com.qingchen.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器,channel注册后,或执行相应的初始化方法
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过socketChannel获取对应的管道pipeline
        ChannelPipeline pipeline = channel.pipeline();
        /**
         * 通过管道,添加handler处理器
         */
        // HttpServerCodec:netty提供的助手类,此处可以理解为管道中的拦截器
        //当请求到服务端,我们需要进行做解码,相应到客户端,我们需要进行编码
        pipeline.addLast("httpServerCodec",new HttpServerCodec());
        //添加自定义的助手类,当请求访问,返回"hello netty"
        pipeline.addLast("HttpHandler",new HttpHandler());
    }
}
