package com.beidao.mall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.beidao.mall.common.exception.BeidaoException;
import com.beidao.mall.model.dto.h5.UserLoginDto;
import com.beidao.mall.model.dto.h5.UserRegisterDto;
import com.beidao.mall.model.entity.user.UserInfo;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.h5.UserInfoVo;
import com.beidao.mall.user.mapper.UserInfoMapper;
import com.beidao.mall.user.service.UserInfoService;
import com.beidao.mall.utils.AuthContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    //注册
    @Override
    public void register(UserRegisterDto userRegisterDto) {

        //1、userRegisterDto获取数据
        String username = userRegisterDto.getUsername();//用户名就是手机号
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        //校验参数
        if(StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(code)) {
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }

        //2、验证码校验
        //2.1、从redis中获取发送验证码
        String redisCode = redisTemplate.opsForValue().get(username);

        //2.2、获取输入的验证码  进行比对
        if (!redisCode.equals(code)){

            throw new BeidaoException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //3、检验用户名不能重复
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        if (userInfo != null)
        {
            //存在相同用户名
            throw new BeidaoException(ResultCodeEnum.USER_NAME_IS_EXISTS);

        }


        //4、封装添加数据  调用方法添加数据库
        //不等于空  new一个对象
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setNickName(nickName);
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        userInfoMapper.save(userInfo);

        //5、从redis中删除发送的验证码
        redisTemplate.delete(username);

    }


    //用户登录
    @Override
    public Object login(UserLoginDto userLoginDto) {

        //1、userLoginDto获取用户名和密码
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        //校验参数
        if(StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password)) {
            throw new BeidaoException(ResultCodeEnum.DATA_ERROR);
        }

        //2、根据用户名查询数据库  得到用户信息
        UserInfo userInfo = userInfoMapper.selectByUserName(username);

        if(null == userInfo) {
            throw new BeidaoException(ResultCodeEnum.LOGIN_ERROR);
        }

        //3、比较密码是否一致
        String database_password = userInfo.getPassword();
        String md5_password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!database_password.equals(md5_password)){
            throw new BeidaoException(ResultCodeEnum.LOGIN_ERROR);

        }

        //4、校验用户是否禁用
        if(userInfo.getStatus() == 0) {
            throw new BeidaoException(ResultCodeEnum.ACCOUNT_STOP);
        }

        //5、生成token
        String token = UUID.randomUUID().toString().replaceAll("-","");

        //6、把用户信息放到redis中

        redisTemplate.opsForValue().set("user:mall"+token,JSON.toJSONString(userInfo),
                30, TimeUnit.DAYS);


        return token;
    }


    //获取当前登录的用户信息
    @Override
    public UserInfoVo getCurrentUserInfo(String token) {

        //从redis根据token获取用户信息
//        String userJson = redisTemplate.opsForValue().get("user:mall"+token);
//        if(!StringUtils.hasText(userJson)){
//
//            throw new BeidaoException(ResultCodeEnum.LOGIN_AUTH);
//        }
//
//        UserInfo userInfo = JSON.parseObject(userJson,UserInfo.class);


        //从threadLocal中获取当前信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();

        //userInfo--->userInfoVO
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);

        return userInfoVo;
    }
}
