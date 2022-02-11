package com.itjiguang.yanxuan.viewmodel;

import com.itjiguang.yanxuan.model.GoodsSpec;
import com.itjiguang.yanxuan.model.GoodsSpecOption;

import java.util.List;

/**
 * 封装规格信息
 */
public class Specification extends GoodsSpec {
    // 存放各规格选项信息
    private List<GoodsSpecOption> optionList;

    public List<GoodsSpecOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<GoodsSpecOption> optionList) {
        this.optionList = optionList;
    }
}
