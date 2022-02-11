package com.itjiguang.yanxuan.goods.api;

import com.itjiguang.yanxuan.model.GoodsCategory;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Category;

/**
 * 商品类目服务接口
 */
public interface IGoodsCategoryService {

    /**
     * 分页查询
     * @param currentPage 当前页面
     * @param pageSize 每页记录数
     * @param goodsCategory 查询条件
     * @return
     */
    public PageResult<GoodsCategory> pageQuery(Integer currentPage, Integer pageSize, GoodsCategory goodsCategory);

    /**
     * 保存类目信息
     * @param category
     * @return
     */
    public Integer save(Category category);

    /**
     * 根据主键删除类目信息（逻辑删除）
     * @param id
     * @return
     */
    public Integer deleteById(Long id);

    /**
     * 根据主键查询类目信息
     * @param id
     * @return
     */
    public Category queryById(Long id);

    /**
     * 更新类目信息
     * @param category
     * @return
     */
    public Integer update(Category category);
}
