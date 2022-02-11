package com.itjiguang.yanxuan.seller.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.goods.api.IGoodsCategoryService;
import com.itjiguang.yanxuan.model.GoodsCategory;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class GoodsCategoryController {
    // 类目远程服务
    @Reference
    private IGoodsCategoryService goodsCategoryService;

    /**
     *
     * @param currentPage 当前页面
     * @param pageSize 每页记录数
     * @param goodsCategory 商品类目
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<PageResult<GoodsCategory>> query(Integer currentPage, Integer pageSize, GoodsCategory goodsCategory) {
        // 分页参数处理
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 调用远程服务查询类目信息
        PageResult<GoodsCategory> pageResult = goodsCategoryService.pageQuery(currentPage, pageSize, goodsCategory);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    /**
     * 根据 id 查询类目信息
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> queryById(@PathVariable("id") Long id) {
        Category category = goodsCategoryService.queryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
