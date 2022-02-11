package com.itjiguang.yanxuan.ad.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.ad.api.IAdInfoService;
import com.itjiguang.yanxuan.mapper.AdInfoMapper;
import com.itjiguang.yanxuan.model.AdInfo;
import com.itjiguang.yanxuan.model.AdInfoExample;
import com.itjiguang.yanxuan.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class AdInfoServiceImpl implements IAdInfoService {

    @Autowired
    private AdInfoMapper adInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult<AdInfo> pageQuery(Integer currentPage, Integer pageSize, AdInfo adInfo) {

        if (adInfo != null && adInfo.getTypeId() != null && !"".equals(adInfo.getTypeId())) {
            // 从Redis中获取数据
            PageResult pageResult = (PageResult)redisTemplate.boundHashOps("adInfo").get(adInfo.getTypeId().toString());
            if (pageResult != null) {
                System.out.println("------------------Redis-------------");
                return pageResult;
            }
        }
        // 构建查询条件
        AdInfoExample adInfoExample = new AdInfoExample();
        // 查询条件非空
        if (adInfo != null) {
            // 广告信息名称非空
            if (adInfo.getName() != null && !"".equals(adInfo.getName())) {
                // 广告信息名称
                String adInfoName = new String(adInfo.getName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                adInfoExample.createCriteria().andNameLike("%" + adInfoName + "%");
            }
            // 广告类型
            if (adInfo.getTypeId() != null && !"".equals(adInfo.getTypeId())) {
                adInfoExample.createCriteria().andTypeIdEqualTo(adInfo.getTypeId());
            }
            // 广告状态
            if (adInfo.getStatus() != null && !"".equals(adInfo.getStatus())) {
                adInfoExample.createCriteria().andStatusEqualTo(adInfo.getStatus());
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 查询数据
        Page<AdInfo> pageData =  (Page<AdInfo>) adInfoMapper.selectByExampleWithBLOBs(adInfoExample);
        // 构建返回结果
        PageResult<AdInfo> pageResult = new PageResult<>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setResult(pageData.getResult());
        // 保存数据到Redis
        if (adInfo != null && adInfo.getTypeId() != null && !"".equals(adInfo.getTypeId())) {
            redisTemplate.boundHashOps("adInfo").put(adInfo.getTypeId().toString(), pageResult);
        }
        return pageResult;
    }

    @Override
    public int deleteById(Long id) {
        // 根据主键查询到广告信息
        AdInfo adInfo1 = adInfoMapper.selectByPrimaryKey(id);
        // 清空缓存
        redisTemplate.boundHashOps("adInfo").delete(adInfo1.getTypeId().toString());
        AdInfo adInfo = new AdInfo();
        adInfo.setId(id);
        adInfo.setStatus("1");
        return adInfoMapper.updateByPrimaryKeySelective(adInfo);
    }

    @Override
    public int save(AdInfo adInfo) {
        // 清空缓存
        redisTemplate.boundHashOps("adInfo").delete(adInfo.getTypeId().toString());
        return adInfoMapper.insert(adInfo);
    }

    @Override
    public int update(AdInfo adInfo) {
        return adInfoMapper.updateByPrimaryKeyWithBLOBs(adInfo);
    }

    @Override
    public AdInfo queryById(Long id) {
        return adInfoMapper.selectByPrimaryKey(id);
    }
}
