package com.itjiguang.yanxuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.goods.api.IGoodsSpecService;
import com.itjiguang.yanxuan.model.GoodsSpec;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spec")
public class GoodsSpecController {
    // dubbo引用
    @Reference
    private IGoodsSpecService goodsSpecService;
    @GetMapping
    public ResponseEntity<PageResult> query(Integer currentPage, Integer pageSize, GoodsSpec goodsSpec) {
        // 分页参数处理
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 使用dubbo服务进行查询
        PageResult<GoodsSpec> pageResult = goodsSpecService.pageQuery(currentPage, pageSize, goodsSpec);
        // 返回结果
        return new ResponseEntity<PageResult>(pageResult, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity save(@RequestBody Specification specification) {
        Integer result = goodsSpecService.save(specification);
        return result > 0 ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Integer result = goodsSpecService.deleteById(id);
        return result > 0 ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Specification> queryById(@PathVariable("id") Long id) {
        Specification specification = goodsSpecService.queryById(id);
        return new ResponseEntity<>(specification, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity update(@RequestBody Specification specification) {
        Integer update = goodsSpecService.update(specification);
        return update > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
