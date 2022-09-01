package com.alphen.reader.service.impl;

import com.alphen.reader.entity.User;
import com.alphen.reader.exception.BussinessException;
import com.alphen.reader.mapper.UserMapper;
import com.alphen.reader.service.UserService;
import com.alphen.reader.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    /*
     * 检查登录信息
     * @param username 用户名
     * @param password 用户密码
     * @return user 用户对象
     * */
    @Override
    public User checkLogin(String username, String password) {
//        通过用户名查看该用户名的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            throw new BussinessException("L01","用户名不存在");
        }
//        假如用户名正确，找到MD5后的密码进行比对
//        String md5 = MD5Utils.md5Digest(password,user.getSalt());
        if(!password.equals(user.getPassword())){
            throw new BussinessException("L02","密码错误");
        }
        return user;
    }

    /*
     * 注册管理员信息
     * @param username 用户名
     * @param password 密码
     * @return 用户对象
     * */
    @Override
    public User createUser(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        List<User> userList = userMapper.selectList(queryWrapper);
        if(userList.size()>0){
            throw new BussinessException("C01","用户已存在");
        }
//        int salt = new Random().nextInt(1000)+1000;
//        String saltPassword = MD5Utils.md5Digest(password,salt);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setSalt(11);
        userMapper.insert(newUser);
        return newUser;
    }
}
