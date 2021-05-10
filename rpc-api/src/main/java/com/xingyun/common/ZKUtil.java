package com.xingyun.common;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ZKUtil {

    public static CuratorFramework curatorFramework;

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000,3);


        curatorFramework = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(retryPolicy).namespace("provider").build();
        curatorFramework.start();
    }

    public static void registerService(String host,int port) throws Exception {
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/"+host+":"+port, (host+":"+port).getBytes(StandardCharsets.UTF_8));
    }

    public static List<String> getServiceList() throws Exception {
        List<String> pathList = curatorFramework.getChildren().forPath("/");

        List<String> serviceList =  new ArrayList<>();

        for (String path : pathList) {
            //System.out.println(path);
            byte[] bytes = curatorFramework.getData().forPath("/"+path);
            serviceList.add(new String(bytes));
        }

        return serviceList;
    }






}
