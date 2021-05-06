package com.xingyun.codec;

import com.alibaba.fastjson.JSON;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONDecoder extends MessageToMessageDecoder<ByteBuf> {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        RPCResponse rpcResponse = JSON.parseObject(byteBuf.toString(StandardCharsets.UTF_8), RPCResponse.class);

        list.add(rpcResponse);
    }
}
