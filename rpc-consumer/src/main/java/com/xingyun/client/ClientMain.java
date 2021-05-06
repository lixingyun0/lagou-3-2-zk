package com.xingyun.client;

import com.xingyun.model.User;
import com.xingyun.service.IUserService;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException {

        IUserService userService = RpcProxy.getProxy(IUserService.class);
        User userById = userService.getUserById(1);
        System.out.println(userById);
    }
}
