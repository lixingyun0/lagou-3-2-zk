package com.xingyun.server;

import com.xingyun.codec.JSONDecoder;
import com.xingyun.codec.JSONEncoder;
import com.xingyun.common.ZKUtil;
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

import java.util.HashMap;
import java.util.Map;

public class NettyServer {

    public static Map<String,Object> container;

    public static ChannelFuture channelFuture;

    public static NioEventLoopGroup bossGroup;

    public static NioEventLoopGroup workGroup;

    static {
        container = new HashMap<>();
        IUserService userService =  new UserServiceImpl();
        container.put(userService.getClass().getInterfaces()[0].getName(),userService);
    }



    public static void main(String[] args) {

        try {
            bossGroup = new NioEventLoopGroup(1);

            workGroup = new NioEventLoopGroup();

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

            channelFuture = serverBootstrap.bind(Integer.valueOf(args[0])).sync();
            ZKUtil.registerService("127.0.0.1",Integer.valueOf(args[0]));
            System.out.println("提供者已启动");
        } catch (Exception e) {
            e.printStackTrace();

            if (channelFuture != null){
                ChannelFuture sync = channelFuture.channel().close();
            }
            if (bossGroup !=null){
                bossGroup.shutdownGracefully();
            }
            if (workGroup !=null){
                workGroup.shutdownGracefully();
            }
        }

    }

}
