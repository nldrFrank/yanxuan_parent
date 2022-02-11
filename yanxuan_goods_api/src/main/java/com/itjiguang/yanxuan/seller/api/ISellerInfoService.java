package com.itjiguang.yanxuan.seller.api;

import com.itjiguang.yanxuan.model.SellerShop;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.SellerInfo;

public interface ISellerInfoService {

    /**
     * 保存商家入驻信息
     * @param sellerInfo 商家信息
     * @return 影响行数
     */
    public abstract Integer save(SellerInfo sellerInfo);

    /**
     * 分页查询
     * @param currentPage 当前页面
     * @param pageSize 每页记录数
     * @param sellerShop 查询条件
     * @return 符合条件的记录数
     */
    public abstract PageResult<SellerShop> pageQuery(Integer currentPage, Integer pageSize, SellerShop sellerShop);

    /**
     * 更新商家信息中的状态
     * @param sellerShop 商家信息
     * @return 影响行数
     */
    public abstract Integer update(SellerShop sellerShop);

}
