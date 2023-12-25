package com.beidao.mall.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.manager.mapper.SysUserMapper;
import com.beidao.mall.manager.service.SysUserService;
import com.beidao.mall.model.dto.system.LoginDto;
import com.beidao.mall.model.entity.system.SysUser;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired //寻找一个匹配的bean并将其注入到该字段中。
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    //用户登录
    @Override
    public LoginVo login(LoginDto loginDto) {

        // 1、获取输入验证码和存储到redis的key名称  loginDto获取到
        String captcha = loginDto.getCaptcha();
        String key = loginDto.getCodeKey();

        // 2、根据获取的redis里面的key，查询redis里面存储验证码
        //set 时 加了前缀  "user:validate"+key   取出来的时候也要加前缀
        String redisCode = redisTemplate.opsForValue().get("user:validate" + key);

        // 3、比较输入的验证码和redis存储验证码是否一致
        // 4、如果不一样 提示验证码校验失败
        //hutool 中的工具类 StrUtil判断字符串是否为空     验证码比较忽略大小写
        //为空  或者 不一样 抛出自定义异常---枚举类中的验证码错误提示
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode,captcha)){
            throw new BeidaoException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //5、如果一致 删除redis中的验证码
        redisTemplate.delete("user:validate" + key);
        //1.获取提交过来的用户名  loginDto获取
        String userName = loginDto.getUserName();

        //2.根据用户名username查询数据库表sys_user表
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);

        //3.查询不到用户 用户不存在 返回错误信息
       /* if(sysUser == null){

            throw new RuntimeException("用户名不存在");
        }*/

        //更改成自定义异常处理
        if (sysUser == null){
            throw new BeidaoException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4.查询到信息  用户存在
        //5. 获取输入的密码，比较输入的密码与数据库中是否一致
        String database_password = sysUser.getPassword();

        /*String input_password = loginDto.getPassword();

        //把输入的密码先进行加密再进行比较  -- 数据库中的密码是加密过的  使用md5方式加密
        input_password = DigestUtils.md5DigestAsHex(input_password.getBytes());*/

        //改进一下
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());

        //比较
        /*if (!input_password.equals(database_password)){
            throw new RuntimeException("密码错误");
        }*/

        //自定义异常替换
        if (!input_password.equals(database_password)){
            throw new BeidaoException(ResultCodeEnum.LOGIN_ERROR);
        }


        //6.密码一致 登录成功 反之则失败
        //7.登录成功  生成用户唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-","");


        //8.把登录成功用户信息放到redis中 key:token  value:用户信息  时间  单位

        //将用户信息转成字符串传入到redis中    JSON.toJSONString(sysUser)
        redisTemplate.opsForValue()
                .set("user:login"+token,
                        JSON.toJSONString(sysUser),
                        7,
                        TimeUnit.DAYS);


        //9.返回loginVo对象  ->token
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    //获取当前登录用户信息
    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login"+token);

        //把json格式转成v字符串对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }


    //用户退出
    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login"+token);
    }
}
