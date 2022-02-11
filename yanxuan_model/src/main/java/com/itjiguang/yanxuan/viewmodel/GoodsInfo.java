package com.itjiguang.yanxuan.viewmodel;

import com.itjiguang.yanxuan.model.GoodsSku;
import com.itjiguang.yanxuan.model.GoodsSpu;

import java.util.List;

/**
 * 封装商品信息 一个goodsSpu包含多个goodsSku
 */
public class GoodsInfo extends GoodsSpu {
    // 商品 sku 列表
    private List<GoodsSku> skuList;

    public List<GoodsSku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<GoodsSku> skuList) {
        this.skuList = skuList;
    }
}
