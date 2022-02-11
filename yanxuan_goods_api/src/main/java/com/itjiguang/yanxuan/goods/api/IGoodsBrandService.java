package com.itjiguang.yanxuan.goods.api;

import com.itjiguang.yanxuan.model.GoodsBrand;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.result.Result;

import java.util.List;

/**
 * 商品品牌列表服务接口
 */
public interface IGoodsBrandService {

    /**
     * 查询所有商品品牌信息
     * @return
     */
    public List<GoodsBrand> queryAll();

    /**
     * 分页查询
     * @param currentPage 当前要查询的页面
     * @param pageSize 页面大小
     * @return
     */
    public PageResult<GoodsBrand> pageQuery(Integer currentPage, Integer pageSize);

    /**
     * 根据分页参数、查询条件进行分页查询
     * @param currentPage 当前要查询的页面
     * @param pageSize 页面大小
     * @param goodsBrand 查询条件
     * @return
     */
    public PageResult<GoodsBrand> pageQuery(Integer currentPage, Integer pageSize, GoodsBrand goodsBrand);

    /**
     * 保存商品品牌信息
     * @param goodsBrand
     */
    public int save(GoodsBrand goodsBrand);

    /**
     * 修改商品品牌信息
     * @param goodsBrand
     */
    public int update(GoodsBrand goodsBrand);

    /**
     * 根据商品品牌id进行逻辑删除
     * @param id
     */
    public int delete(Long id);

    /**
     * 根据id查询商品品牌信息
     * @param id
     * @return
     */
    public GoodsBrand queryById(Long id);
}
