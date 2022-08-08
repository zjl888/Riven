package com.riven.service;

import com.riven.util.SecurityUtils;
import com.riven.util.SnowflakeIdWorker;
import com.riven.util.StringUtils;
import com.riven.model.RegisterBody;
import com.riven.model.User;
import com.riven.service.impl.UserServiceImpl;
import com.riven.model.RegisterBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Component
public class RegisterService {
    @Resource
    private UserServiceImpl userService;
    public String register(@RequestBody RegisterBody registerBody){
        String msg;
        //查询并判断用户是否存在
        User user = userService.select(registerBody.getUsername());
        if (StringUtils.isNotNull(user)){
            msg="该用户已存在,注册失败！";
        }
        User user1=new User();
        user1.setUsername(registerBody.getUsername());
        //密码加密
        user1.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
        user1.setId(SnowflakeIdWorker.idWorker.nextId());
        Boolean aBoolean = userService.register(user1);
        if (aBoolean==false){
            msg="注册失败！";
        }else {
            msg="注册成功！";
        }
        return msg;
    }
}
