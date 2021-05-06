package com.xingyun.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {

    public static <T>T getProxy(Class<T> clazz){

        return (T) Proxy.newProxyInstance(RpcProxy.class.getClassLoader(),  new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                NettyClient nettyClient = new NettyClient();

                RPCRequest rpcRequest = new RPCRequest();
                rpcRequest.setServiceName(clazz.getName());
                rpcRequest.setMethod(method.getName());

                rpcRequest.setParameterTypes(method.getParameterTypes());
                rpcRequest.setParams(args);

                RPCResponse rpcResponse = nettyClient.invokeRemoteMethod(rpcRequest);

                nettyClient.close();

                if (rpcResponse.getSuccessFlag()){
                    Class<?> returnType = method.getReturnType();
                    JSONObject jsonObject = (JSONObject) rpcResponse.getResult();

                    //System.out.println("==="+ rpcResponse.getResult());
                    return JSON.parseObject(jsonObject.toJSONString(),returnType);
                }else {
                    throw rpcResponse.getE();
                }

            }
        });
    }


}
