package com.alphen.reader.service.impl;

import com.alphen.reader.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceImplTest {

    @Resource
    private UserService userService;

    @Test
    public void checkLogin() {
    }

    @Test
    public void createUser() {
        userService.createUser("123","123");
    }
}