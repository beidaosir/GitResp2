import request from '@/utils/request'

const base_api = '/admin/system/sysUser'

// 角色列表                 这里加入后端参数
export const GetSysUserListByPage = (current,limit,queryDto) => {
  return request({
    //   ``模板字符串  es6写法
    url: `${base_api}/findByPage/${current}/${limit}`,//路径
    method: 'get',//提交方式
    //后端接口没有注解  前端使用params：名称
    //后端 接口@RequestBody  前端data：名称  =》以json格式传递
    params: queryDto,//其他参数
  })
}

//用户添加
export const SaveSysUser = (sysUser)=>{
    return request({
        //   ``模板字符串  es6写法
        url: `${base_api}/saveSysUser`,//路径
        method: 'post',//提交方式
        //后端接口没有注解  前端使用params：名称
        //后端 接口@RequestBody  前端data：名称  =》以json格式传递
        data: sysUser,//其他参数
      })
}

//用户修改
export const UpdateSysUser = (sysUser)=>{
    return request({
        //   ``模板字符串  es6写法
        url: `${base_api}/updateSysUser`,//路径
        method: 'put',//提交方式
        //后端接口没有注解  前端使用params：名称
        //后端 接口@RequestBody  前端data：名称  =》以json格式传递
        data: sysUser,//其他参数
      })
}

//用户删除
export const DeleteSysUser = (userId)=>{
    return request({
        //   ``模板字符串  es6写法
        url: `${base_api}/deleteById/${userId}`,//路径
        method: 'delete',//提交方式
        //后端接口没有注解  前端使用params：名称
        //后端 接口@RequestBody  前端data：名称  =》以json格式传递
      })
}


// 给用户分配角色请求
export const DoAssignRoleToUser = (assginRoleVo) => {
  return request({
      url: "/admin/system/sysUser/doAssign",
      method: 'post',
      data: assginRoleVo
  })
}
