package com.itjiguang.yanxuan.search.api;

import java.util.Map;

public interface ISearchService {
    /**
     * 根据条件查询solr服务器中的信息
     * @param queryParams 查询关键字
     * @return 商品信息
     */
    Map<String,Object> query(Map queryParams);
}
