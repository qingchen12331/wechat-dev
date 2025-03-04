package com.qingchen.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 初始化器,channel注册后,或执行相应的初始化方法
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过socketChannel获取对应的管道pipeline
        ChannelPipeline pipeline = channel.pipeline();
        /**
         * 通过管道,添加handler处理器
         */
        // HttpServerCodec:netty提供的助手类,此处可以理解为管道中的拦截器
        //当请求到服务端,我们需要进行做解码,相应到客户端,我们需要进行编码
        //websocket基于http协议,所以要有http编解码器
        pipeline.addLast(new HttpServerCodec());
        //添加对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合,聚合成FullHttpRequest或FullHttpResponse
        //几乎在netty的编程中都会用到这个handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //========================以上是用于支持http协议相关的handler=========================
        // ==================== 增加心跳支持 start ====================
        // 针对客户端，如果在1分钟没有向服务端发送读写心跳(ALL)，则主动断开连接
        // 如果是读空闲或者写空间，不做任何处理
        pipeline.addLast(new IdleStateHandler(
                8,
                10,
                300 * 60));
        pipeline.addLast(new HeartBeatHandler());

        // ==================== 增加心跳支持 end ====================
        //========================以下是支持ws,用于处理websocket的handler=========================
        /**
         * WebSocket 服务器处理的协议,用于指定给客户端连接的时候访问的路由:/ws
         * 此Handler 会帮你处理一些繁重的复杂的事情,比如握手,ping/pong,关闭,ping/pong
         * 对于WebSocket来说,数据都是以frames进行传输的,不同的数据类型所对应的frames也都不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //添加自定义的助手类,当请求访问,返回"hello netty"
        pipeline.addLast(new ChatHandler());
    }
}
