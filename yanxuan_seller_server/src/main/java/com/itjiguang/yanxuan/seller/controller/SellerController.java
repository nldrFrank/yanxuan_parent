package com.itjiguang.yanxuan.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.seller.api.ISellerInfoService;
import com.itjiguang.yanxuan.viewmodel.SellerInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    // 引用 yanxuan_goods_api 中的接口完成商家信息的保存
    @Reference
    private ISellerInfoService sellerInfoService;
    /**
     * 保存提交的商家信息
     * @param sellerInfo 前端请求过来的商家信息
     * @return
     */
    @PostMapping
    public ResponseEntity save(@RequestBody SellerInfo sellerInfo) {
        // 调用远程服务完成数据保存
        Integer save = sellerInfoService.save(sellerInfo);
        // 返回结果
        return save > 0 ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
