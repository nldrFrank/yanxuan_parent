package com.itjiguang.yanxuan.viewmodel;

import com.itjiguang.yanxuan.model.GoodsCategory;
import com.itjiguang.yanxuan.model.GoodsCategoryBrandSpec;

import java.util.List;
import java.util.Map;

public class Category extends GoodsCategory {
    // 类目和品牌、规格信息的关联
    private GoodsCategoryBrandSpec relation;
    // 存放与类目关联的规格和规格项信息
    private List<Map> specList;

    public GoodsCategoryBrandSpec getRelation() {
        return relation;
    }

    public void setRelation(GoodsCategoryBrandSpec relation) {
        this.relation = relation;
    }

    public List<Map> getSpecList() {
        return specList;
    }

    public void setSpecList(List<Map> specList) {
        this.specList = specList;
    }
}
