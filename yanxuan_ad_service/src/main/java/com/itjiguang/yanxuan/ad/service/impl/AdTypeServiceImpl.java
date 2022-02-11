package com.itjiguang.yanxuan.ad.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.ad.api.IAdTypeService;
import com.itjiguang.yanxuan.mapper.AdTypeMapper;
import com.itjiguang.yanxuan.model.AdType;
import com.itjiguang.yanxuan.model.AdTypeExample;
import com.itjiguang.yanxuan.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class AdTypeServiceImpl implements IAdTypeService {
    @Autowired
    private AdTypeMapper adTypeMapper;
    @Override
    public PageResult<AdType> pageQuery(Integer currentPage, Integer pageSize, AdType adType) {
        // 构建查询条件
        AdTypeExample adTypeExample = null;
        // 查询条件非空
        if (adType != null) {
            // 根据广告类型名称进行查询
            if (adType.getName() != null && !"".equals(adType.getName())) {
                adTypeExample = new AdTypeExample();
                // 广告类型名称
                String adTypeName = new String(adType.getName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                adTypeExample.createCriteria().andNameLike("%" + adTypeName + "%");
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 查询数据
        Page<AdType> pageData =  (Page<AdType>) adTypeMapper.selectByExample(adTypeExample);
        // 构建返回结果
        PageResult<AdType> pageResult = new PageResult<>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setResult(pageData.getResult());
        return pageResult;
    }

    @Override
    public int save(AdType adType) {
        return adTypeMapper.insert(adType);
    }

    @Override
    public int deleteById(Long id) {
         return adTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(AdType adType) {
        return adTypeMapper.updateByPrimaryKey(adType);
    }

}
