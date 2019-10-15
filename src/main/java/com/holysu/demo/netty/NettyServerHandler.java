package com.holysu.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 读取请求
        ByteBuf requestBuffer = (ByteBuf) msg;
        byte[] requestBytes = new byte[requestBuffer.readableBytes()];
        requestBuffer.readBytes(requestBytes);
        String request = new String(requestBytes, "UTF-8");
        System.out.println("收到请求：\"" + request + "\"");

        // 输出响应
        String response = "你好，俺收到你的响应了";
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuffer);

        //  ChannelHandlerAdapter 类似分布式文件存储系统的 Processor 线程，负责读取请求和发送响应
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 读取完毕
        System.out.println("channelReadComplete......");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
