package com.holysu.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();  // 线程组 ->  Accepter 线程
        EventLoopGroup childGroup = new NioEventLoopGroup(); // 线程组 -> Processor / Handler

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap(); // 相当于 netty 服务器

            serverBootstrap
                    .group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)  // 监听端口的 ServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler()); // 针对网络请求
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(50070).sync(); // 同步等待服务器监听端口
            channelFuture.channel().closeFuture().sync();  // 同步等待关闭启动服务器的结果

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
