package com.xingyun.service.impl;

import com.xingyun.model.User;
import com.xingyun.service.IUserService;

public class UserServiceImpl implements IUserService {
    @Override
    public User getUserById(Integer id) {
        if (id ==1){
            User user1 = new User();
            user1.setId(1);
            user1.setUsername("小红");

            return user1;
        }
        if (id == 2){
            User user1 = new User();
            user1.setId(1);
            user1.setUsername("小蓝");

            return user1;
        }


        return null;
    }
}
