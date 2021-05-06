package com.xingyun.client;

import com.xingyun.codec.JSONDecoder;
import com.xingyun.codec.JSONEncoder;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;
import com.xingyun.handler.RPCClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class NettyClient {

    private RPCClientHandler rpcClientHandler = new RPCClientHandler();

    private NioEventLoopGroup group;

    private ChannelFuture channelFuture;

    public NettyClient() {

        try {
            group = new NioEventLoopGroup();


            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new JSONEncoder());
                            socketChannel.pipeline().addLast(new JSONDecoder());
                            socketChannel.pipeline().addLast(rpcClientHandler);
                        }
                    });

            channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
        } catch (Exception e) {
            if (null != channelFuture){
                channelFuture.channel().close();
            }


            if (group != null){
                group.shutdownGracefully();
            }

        }


    }

    public void close(){
        if (null != channelFuture){
            channelFuture.channel().close();
        }


        if (group != null){
            group.shutdownGracefully();
        }
    }

    public RPCResponse invokeRemoteMethod(RPCRequest rpcRequest) throws InterruptedException {
        //rpcClientHandler.setRpcRequest(rpcRequest);
        RPCResponse rpcResponse = rpcClientHandler.invokeRemoteMethod(rpcRequest);
        return rpcResponse;
    }
}
