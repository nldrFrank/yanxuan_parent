package com.itjiguang.yanxuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.goods.api.IGoodsCategoryService;
import com.itjiguang.yanxuan.model.GoodsCategory;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class GoodsCategoryController {
    @Reference
    private IGoodsCategoryService goodsCategoryService;
    @GetMapping
    public ResponseEntity<PageResult> query(Integer currentPage, Integer pageSize, GoodsCategory goodsCategory) {
        // 分页参数处理
        if (currentPage == null || pageSize ==null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 查询商品类目信息
        PageResult<GoodsCategory> pageResult = goodsCategoryService.pageQuery(currentPage, pageSize, goodsCategory);
        // 返回结果
        return new ResponseEntity<PageResult>(pageResult, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity save(@RequestBody Category category) {
        Integer result = goodsCategoryService.save(category);
        return result > 0 ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Integer result = goodsCategoryService.deleteById(id);
        return result > 0 ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> queryById(@PathVariable("id") Long id) {
        Category category = goodsCategoryService.queryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity update(@RequestBody Category category) {
        Integer result = goodsCategoryService.update(category);
        return result > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
