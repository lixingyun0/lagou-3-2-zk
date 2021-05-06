package com.xingyun.server;

import com.xingyun.codec.JSONDecoder;
import com.xingyun.codec.JSONEncoder;
import com.xingyun.handler.RPCServerHandler;
import com.xingyun.service.IUserService;
import com.xingyun.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;

public class NettyServer {

    public static Map<String,Object> container;

    static {
        container = new HashMap<>();
        IUserService userService =  new UserServiceImpl();
        container.put(userService.getClass().getInterfaces()[0].getName(),userService);
    }

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new JSONDecoder());
                        socketChannel.pipeline().addLast(new JSONEncoder());

                        socketChannel.pipeline().addLast(new RPCServerHandler());

                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
        System.out.println("提供者已启动");

        ChannelFuture sync = channelFuture.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();

    }
}
