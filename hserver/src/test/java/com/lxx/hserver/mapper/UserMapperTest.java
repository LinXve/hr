package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    void deleteUserRolesByUId() {
        int i = userMapper.deleteUserRolesByUId(4);
        log.info(i+" ");
    }
    @Test
    void selectByKeywords(){
        List<User> a = userMapper.getUserByKeyWords("a");
        for (User user : a) {
            System.out.println(user.toString());
        }
    }

}