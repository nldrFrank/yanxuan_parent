package com.itjiguang.yanxuan.seller.api;

import com.itjiguang.yanxuan.model.Account;

public interface IAccountService {
    /**
     * 根据登录用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    public abstract Account queryByUsername(String username);
}
