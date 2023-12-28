package com.beidao.mall.manager.controller;

import com.beidao.mall.manager.service.SysMenuService;
import com.beidao.mall.manager.service.SysUserService;
import com.beidao.mall.manager.service.ValidateCodeService;
import com.beidao.mall.model.dto.system.LoginDto;
import com.beidao.mall.model.entity.system.SysUser;
import com.beidao.mall.model.vo.common.Result;
import com.beidao.mall.model.vo.common.ResultCodeEnum;
import com.beidao.mall.model.vo.system.LoginVo;
import com.beidao.mall.model.vo.system.SysMenuVo;
import com.beidao.mall.model.vo.system.ValidateCodeVo;
import com.beidao.mall.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="用户接口")
@RestController //返回json数据格式
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;  //验证码注入

    @Autowired
    private SysMenuService sysMenuService;


    //用户登录
    //所有方法返回result对象  让所有接口返回相同数据
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto) { //@RequestBody 通过json格式传递  LoginDto用于封装表单提交过来的数据
        //调用service中的方法进行实现
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);

    }

    //生成图片验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){

    ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
    return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);

    }

    //获取当前用户登录信息
    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> getUserInfo(){
        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS) ;
    }

    //获取当前用户登录信息
    //被优化咯
/*    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> getUserInfo(@RequestHeader(name = "token") String token) {
        //方法二：@RequestHeader(name = "token") String token
        //方法一：HttpServletRequest request

        // 1.从请求头获取token
        //String token = request.getHeader("token");

        //2.根据token查询redis获取用户信息
        SysUser sysUser = sysUserService.getUserInfo(token) ;
        //3.用户信息返回
        return Result.build(sysUser , ResultCodeEnum.SUCCESS) ;
    }*/

    //用户退出
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }


    //查询用户可以操作的菜单
    @GetMapping("/menus")
    public Result menus(){
        List<SysMenuVo> list = sysMenuService.findMenusByUserId();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }



}
