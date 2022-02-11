package com.itjiguang.yanxuan.goods.api;

import com.itjiguang.yanxuan.model.GoodsSpu;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.GoodsInfo;

public interface IGoodsInfoService {

    /**
     * 保存商品信息
     * @param goodsInfo 商品信息
     * @return 影响记录数
     */
    public abstract int save(GoodsInfo goodsInfo);

    /**
     * 根据条件查询店铺的商品列表
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @param goodsSpu 查询条件
     * @return PageResult<GoodsSpu>
     */
    public abstract PageResult<GoodsSpu> pageQuery(Integer currentPage, Integer pageSize, GoodsSpu goodsSpu);

    /**
     * 根据主键查询商品信息
     * @param id 商品主键
     * @return GoodsInfo
     */
    GoodsInfo queryById(Long id);

    /**
     * 修改商品信息
     * @param goodsInfo 商品信息
     * @return 影响记录数
     */
    int update(GoodsInfo goodsInfo);

    /**
     * 审核商品信息
     * @param goodsSpu 商品信息
     * @return 更新记录数
     */
    int audit(GoodsSpu goodsSpu);

    /**
     * 逻辑删除商品信息
     * @param id 商品主键id
     * @return 删除记录数
     */
    int delete(Long id);
}
