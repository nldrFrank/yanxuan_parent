package com.itjiguang.yanxuan.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.goods.api.IGoodsInfoService;
import com.itjiguang.yanxuan.model.GoodsSpu;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.GoodsInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private IGoodsInfoService goodsInfoService;
    @PostMapping
    public ResponseEntity save(@RequestBody GoodsInfo goodsInfo) {
        // 获取登录人名称
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsInfo.setCreatePerson(username);
        // 调用远程服务完成商品信息的保存
        int save = goodsInfoService.save(goodsInfo);
        // 返回结果
        return save > 0 ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<PageResult<GoodsSpu>> query(Integer currentPage, Integer pageSize, GoodsSpu goodsSpu) {
        // 处理分页参数
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsSpu.setCreatePerson(username);
        // 调用远程服务进行查询
        PageResult<GoodsSpu> pageResult = goodsInfoService.pageQuery(currentPage, pageSize, goodsSpu);
        // 返回结果
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsInfo> queryById(@PathVariable("id") Long id) {
        // 调用远程服务根据主键id查询商品信息
        GoodsInfo goodsInfo = goodsInfoService.queryById(id);
        // 返回结果
        return goodsInfo == null ? new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR) : new ResponseEntity<>(goodsInfo, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody GoodsInfo goodsInfo) {
        // 获取登录人名称
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsInfo.setUpdatePerson(username);
        // 调用远程服务进行商品信息的修改
        int result = goodsInfoService.update(goodsInfo);
        return result > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        // 调用远程服务根据主键id删除商品信息（逻辑删除）
        int delete = goodsInfoService.delete(id);
        return delete > 0 ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
