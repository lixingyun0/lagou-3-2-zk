package com.xingyun.handler;

import com.alibaba.fastjson.JSON;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;
import com.xingyun.server.NettyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.lang.reflect.Method;

public class RPCServerHandler implements ChannelInboundHandler {
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

        System.out.println("客户端已连接");

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("服务端接受消息："+o);

        RPCRequest rpcRequest = (RPCRequest) o;
        Object target = NettyServer.container.get(rpcRequest.getServiceName());

        Method method = target.getClass().getMethod(rpcRequest.getMethod(), rpcRequest.getParameterTypes());

        RPCResponse rpcResponse = new RPCResponse();
        Object result = null;
        try {
            result = method.invoke(target, rpcRequest.getParams());
            rpcResponse.setSuccessFlag(true);
            rpcResponse.setResult(result);

        } catch (Exception e) {
            rpcResponse.setE(e);
            rpcResponse.setSuccessFlag(false);
        }


        channelHandlerContext.writeAndFlush(rpcResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
