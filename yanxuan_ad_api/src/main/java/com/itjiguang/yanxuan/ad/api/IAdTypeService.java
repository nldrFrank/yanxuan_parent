package com.itjiguang.yanxuan.ad.api;

import com.itjiguang.yanxuan.model.AdType;
import com.itjiguang.yanxuan.result.PageResult;

public interface IAdTypeService {
    /**
     * 根据条件进行分页查询
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @param adType 查询条件
     * @return PageResult<AdType>
     */
    PageResult<AdType> pageQuery(Integer currentPage, Integer pageSize, AdType adType);

    /**
     * 保存广告类型
     * @param adType 广告类型
     * @return 记录数
     */
    int save(AdType adType);

    /**
     * 根据广告类型主键删除数据（物理删除）
     * @param id 广告类型主键id
     * @return 记录数
     */
    int deleteById(Long id);

    /**
     * 更新广告类型
     * @param adType 广告类型信息
     * @return 记录数
     */
    int update(AdType adType);
}
