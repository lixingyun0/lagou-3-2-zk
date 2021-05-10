package com.xingyun.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xingyun.common.RPCRequest;
import com.xingyun.common.RPCResponse;
import com.xingyun.common.ZKUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.CreateMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RpcProxy {

    public static <T>T getProxy(Class<T> clazz){

        return (T) Proxy.newProxyInstance(RpcProxy.class.getClassLoader(),  new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //负载均衡
                Set<Map.Entry<String, NettyClient>> entries = NettyConnectContainer.clientMap.entrySet();

                List<NettyClient> nettyClientList = new ArrayList<>();

                for (Map.Entry<String, NettyClient> entry : entries) {
                    nettyClientList.add(entry.getValue());

                }

                int i = RandomUtils.nextInt(nettyClientList.size());
                NettyClient nettyClient = nettyClientList.get(i);
                System.out.println("选择了客户端：" + nettyClient.getPort() + "进行请求");

                RPCRequest rpcRequest = new RPCRequest();
                rpcRequest.setServiceName(clazz.getName());
                rpcRequest.setMethod(method.getName());

                rpcRequest.setParameterTypes(method.getParameterTypes());
                rpcRequest.setParams(args);

                RPCResponse rpcResponse = nettyClient.invokeRemoteMethod(rpcRequest);

                //记录最后请求时间
                //ZKUtil.curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/"+service+"/time",String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
                //nettyClient.close();

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
