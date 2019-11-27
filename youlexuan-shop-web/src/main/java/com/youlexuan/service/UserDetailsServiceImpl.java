package com.youlexuan.service;

import com.youlexuan.pojo.seller.Seller;
import com.youlexuan.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

// 自定义验证类UserDetailsService    实现Security框架UserDetailsService的接口
public class UserDetailsServiceImpl implements UserDetailsService {
    //修改UserDetailsServiceImpl.java ，添加属性和setter方法 ，修改loadUserByUsername方法

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
    * 认证类
    * @author Administrator
    *
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("经过了UserDetailsServiceImpl");

        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();

        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //得到商家对象
        Seller seller = sellerService.findOne(username);
        if(seller!=null){
            if("1".equals(seller.getStatus())){
                return new User(username,seller.getPassword(),grantedAuths);
            }
        }
        return null;
    }
}
