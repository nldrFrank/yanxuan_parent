package com.itjiguang.yanxuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.model.SellerShop;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.seller.api.ISellerInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private ISellerInfoService sellerInfoService;

    /**
     *
     * @param currentPage
     * @param pageSize
     * @param sellerShop
     * @return
     */
    @GetMapping
    public ResponseEntity<PageResult> query(Integer currentPage, Integer pageSize, SellerShop sellerShop) {
        // 处理分页参数
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 调用远程服务查询商家信息
        PageResult<SellerShop> pageResult = sellerInfoService.pageQuery(currentPage, pageSize, sellerShop);
        // 构建返回结果
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody SellerShop sellerShop) {
        Integer result = sellerInfoService.update(sellerShop);
        return result > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
