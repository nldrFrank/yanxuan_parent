package com.itjiguang.yanxuan.goods.api;

import com.itjiguang.yanxuan.model.GoodsSpec;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Specification;

/**
 * 商品规格列表服务接口
 */
public interface IGoodsSpecService {
    /**
     * 根据条件进行分页查询
     * @param currentPage
     * @param pageSize
     * @param goodsSpec
     * @return
     */
    public PageResult<GoodsSpec> pageQuery(Integer currentPage, Integer pageSize, GoodsSpec goodsSpec);

    /**
     * 保存规格、规格项信息
     * @param specification
     * @return
     */
    public Integer save(Specification specification);

    /**
     * 根据id删除规格信息以及与之关联的规格项信息(逻辑删除)
     * @param id
     * @return
     */
    public Integer deleteById(Long id);

    /**
     * 根据id查询规格信息
     * @param id
     * @return
     */
    public Specification queryById(Long id);

    /**
     * 更新规格和规格项信息
     * @param specification
     * @return
     */
    public Integer update(Specification specification);
}
