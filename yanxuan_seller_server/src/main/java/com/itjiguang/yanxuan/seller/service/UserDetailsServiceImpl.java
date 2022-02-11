package com.itjiguang.yanxuan.seller.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.model.Account;
import com.itjiguang.yanxuan.seller.api.IAccountService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    // 远程服务
    @Reference
    private IAccountService accountService;

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 根据用户名加载用户信息
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户名未找到异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 调用远程服务，根据用户名查询用户信息
        Account account = accountService.queryByUsername(username);
        // 用户权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 返回结果
        return account == null ? null : new User(account.getLoginName(), account.getPassword(), authorities);
    }
}
