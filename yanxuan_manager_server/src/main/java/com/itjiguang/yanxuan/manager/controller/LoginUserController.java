package com.itjiguang.yanxuan.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loginUser")
public class LoginUserController {

    /**
     * 获取登录用户名
     * @return
     */
    @GetMapping
    public ResponseEntity<Map> getLoginUsername() {
        // 从springSecurity中得到登录的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 构建返回结果
        Map<String, String> result = new HashMap<>();
        result.put("username", username);
        // 返回结果
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
