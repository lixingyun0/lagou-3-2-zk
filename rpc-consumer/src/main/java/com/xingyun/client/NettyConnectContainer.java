package com.xingyun.client;

import com.xingyun.common.ZKUtil;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NettyConnectContainer {

    public static Map<String,NettyClient> clientMap;

    static {
        clientMap = new HashMap<>();
        try {
            List<String> serviceList = ZKUtil.getServiceList();

            for (String service : serviceList) {
                String host = service.split(":")[0];
                int port = Integer.valueOf(service.split(":")[1]);
                clientMap.put(service,new NettyClient(host,port));

            }
            ZKUtil.curatorFramework.watchers().add().withMode(AddWatchMode.PERSISTENT).usingWatcher(new CuratorWatcher() {
                @Override
                public void process(WatchedEvent watchedEvent) throws Exception {
                    if (watchedEvent.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)){
                        updateClientMap(ZKUtil.getServiceList());
                    }

                }
            }).forPath("/");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateClientMap(List<String> serviceList){
        System.out.println("更新客户端连接");
        Set<String> strings = clientMap.keySet();

        for (String service : serviceList) {
            if (!strings.contains(service)){
                System.out.println("增加客户端连接");
                String host = service.split(":")[0];
                int port = Integer.valueOf(service.split(":")[1]);
                clientMap.put(service,new NettyClient(host,port));
            }
        }
        for (String string : strings) {
            if (!serviceList.contains(string)){
                System.out.println("剔除客户端连接");
                NettyClient nettyClient = clientMap.get(string);
                nettyClient.close();
                clientMap.remove(string);
            }
        }
    }
}
