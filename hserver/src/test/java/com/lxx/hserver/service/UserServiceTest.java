package com.lxx.hserver.service;

import com.lxx.hserver.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void findByName() {
        User admin = userService.findByName("user");
        System.out.println(admin.toString());
    }

    @Test
    void selectUser() {
        User select = userService.select(1);
        System.out.println(select.toString());
    }
    void updateUser(){
    }
    @Test
    void delUser() {
        boolean b = userService.deleteById(3);
        System.out.println(b);
    }
    @Test
    public void register(){
        userService.register("qwe", "123qwe");

    }
}