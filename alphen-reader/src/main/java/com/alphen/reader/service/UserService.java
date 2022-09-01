package com.alphen.reader.service;

import com.alphen.reader.entity.User;

public interface UserService {

    /*
    * 检查登录信息
    * @param username 用户名
    * @param password 用户密码
    * @return user 用户对象
    * */
    public User checkLogin(String username,String password);

    /*
    * 注册管理员信息
    * @param username 用户名
    * @param password 密码
    * @return 用户对象
    * */
    public User createUser(String username,String password);
}
