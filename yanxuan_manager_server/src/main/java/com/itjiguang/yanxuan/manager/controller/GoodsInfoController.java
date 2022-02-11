package com.itjiguang.yanxuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.goods.api.IGoodsInfoService;
import com.itjiguang.yanxuan.model.GoodsSpu;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.GoodsInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsInfoController {
    @Reference
    private IGoodsInfoService goodsInfoService;
    @GetMapping
    public ResponseEntity<PageResult<GoodsSpu>> query(Integer currentPage, Integer pageSize, GoodsSpu goodsSpu) {
        // 处理分页参数
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 调用远程服务进行分页查询
        PageResult<GoodsSpu> pageResult = goodsInfoService.pageQuery(currentPage, pageSize, goodsSpu);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GoodsInfo> queryById(@PathVariable("id") Long id){
        // 调用远程的服务进行商品的查询
        GoodsInfo goodsInfo = goodsInfoService.queryById(id);
        return new ResponseEntity<>(goodsInfo, HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity audit(@RequestBody GoodsSpu goodsSpu){
        // 调用远程服务更新状态
        int result = goodsInfoService.audit(goodsSpu);
        return result > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
