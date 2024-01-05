package com.beidao.mall.feign.user;


import com.beidao.mall.model.entity.user.UserAddress;
import com.beidao.mall.model.entity.user.UserBrowseHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-user")
public interface UserFeignClient {

    //根据id获取收获地址信息
    @GetMapping("/api/user/userAddress/getUserAddress/{id}")
    public UserAddress getUserAddress(@PathVariable("id") Long id);

    /**
     * @Description: 远程调用获取浏览量最多的商品
     * @param
     */
    @GetMapping("api/user/userInfo/auth/BrowseHistory")
    public UserBrowseHistory getByBrowseHistory();


}
