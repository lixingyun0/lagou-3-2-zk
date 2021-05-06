package com.xingyun.codec;

import com.alibaba.fastjson.JSON;
import com.xingyun.common.RPCRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONEncoder extends MessageToMessageEncoder<RPCRequest> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest, List<Object> list) throws Exception {
        String s = JSON.toJSONString(rpcRequest);
        list.add(Unpooled.copiedBuffer(s.getBytes(StandardCharsets.UTF_8)));
    }
}
