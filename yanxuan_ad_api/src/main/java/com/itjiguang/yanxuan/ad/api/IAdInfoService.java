package com.itjiguang.yanxuan.ad.api;

import com.itjiguang.yanxuan.model.AdInfo;
import com.itjiguang.yanxuan.result.PageResult;

public interface IAdInfoService {
    /**
     * 根据angularJs传递的查询参数条件进行分页查询
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @param adInfo 查询条件
     * @return PageResult<AdInfo>
     */
    PageResult<AdInfo> pageQuery(Integer currentPage, Integer pageSize, AdInfo adInfo);

    /**
     * 根据广告信息主键删除数据
     * @param id 主键id
     * @return ResponseEntity
     */
    int deleteById(Long id);

    /**
     * 新增保存广告信息
     * @param adInfo 广告信息
     * @return ResponseEntity
     */
    int save(AdInfo adInfo);

    /**
     * 修改保存广告信息
     * @param adInfo 广告信息
     * @return ResponseEntity
     */
    int update(AdInfo adInfo);

    /**
     * 根据主键id查找广告信息
     * @param id 主键id
     * @return ResponseEntity<AdInfo>
     */
    AdInfo queryById(Long id);
}
