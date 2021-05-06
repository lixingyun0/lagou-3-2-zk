package com.xingyun.handler;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

public class RPCClientHandler implements ChannelInboundHandler {

    private ChannelHandlerContext channelHandlerContext;

    private RPCResponse rpcResponse;

    private RPCRequest rpcRequest;


    public RPCClientHandler() {
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    public synchronized RPCResponse invokeRemoteMethod(RPCRequest rpcRequest) throws InterruptedException {
        Thread.sleep(3000L);
        System.out.println("远程调用");
        channelHandlerContext.writeAndFlush(rpcRequest);
        wait();


        return rpcResponse;


    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("已连接到服务器");
        this.channelHandlerContext = channelHandlerContext;
        //notify();

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("服务器返回");
        this.rpcResponse = (RPCResponse) o;
        notify();
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

    public RPCRequest getRpcRequest() {
        return rpcRequest;
    }

    public void setRpcRequest(RPCRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }
}
