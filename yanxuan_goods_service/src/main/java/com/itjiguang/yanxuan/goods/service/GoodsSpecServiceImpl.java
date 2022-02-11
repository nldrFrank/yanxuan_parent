package com.itjiguang.yanxuan.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.goods.api.IGoodsSpecService;
import com.itjiguang.yanxuan.mapper.GoodsSpecMapper;
import com.itjiguang.yanxuan.mapper.GoodsSpecOptionMapper;
import com.itjiguang.yanxuan.model.GoodsSpec;
import com.itjiguang.yanxuan.model.GoodsSpecExample;
import com.itjiguang.yanxuan.model.GoodsSpecOption;
import com.itjiguang.yanxuan.model.GoodsSpecOptionExample;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Specification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.SpringCglibInfo;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Transactional
public class GoodsSpecServiceImpl implements IGoodsSpecService {
    @Autowired
    private GoodsSpecMapper goodsSpecMapper;
    @Autowired
    private GoodsSpecOptionMapper goodsSpecOptionMapper;
    @Override
    public PageResult<GoodsSpec> pageQuery(Integer currentPage, Integer pageSize, GoodsSpec goodsSpec) {
        // 构建查询条件
        GoodsSpecExample goodsSpecExample = null;
        if (goodsSpec != null) {
            if (goodsSpec.getName() != null && !"".equals(goodsSpec.getName())) {
                goodsSpecExample = new GoodsSpecExample();
                // 存放规格名字
                String specName = null;
                try {
                    specName = new String(goodsSpec.getName().getBytes("ISO-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                goodsSpecExample.createCriteria().andNameLike("%"+specName+"%");
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 开始查询
        Page<GoodsSpec> pageData = (Page<GoodsSpec>) goodsSpecMapper.selectByExample(goodsSpecExample);
        // 构建返回结果
        PageResult<GoodsSpec> pageResult = new PageResult<>();
        pageResult.setResult(pageData.getResult());
        pageResult.setTotal(pageData.getTotal());
        // 返回结果
        return pageResult;
    }

    @Override
    public Integer save(Specification specification) {
        // 设置规格状态为正常
        specification.setStatus("0");
        // 保存规格信息
        int result = goodsSpecMapper.insert(specification);
        // 遍历规格中的每一个规格项信息
        for (GoodsSpecOption option : specification.getOptionList()) {
            // 设置规格项状态为正常
            option.setStatus("0");
            // 设置规格项id值
            option.setSpecId(specification.getId());
            // 保存规格项信息
            goodsSpecOptionMapper.insertSelective(option);
        }
        // 返回插入的规格信息数量
        return result;
    }

    @Override
    public Integer deleteById(Long id) {
        // 逻辑删除规格信息
        GoodsSpec goodsSpec = new GoodsSpec();
        goodsSpec.setId(id);
        goodsSpec.setStatus("1");
        int result = goodsSpecMapper.updateByPrimaryKeySelective(goodsSpec);
        // 逻辑删除与之关联的规格项信息
        GoodsSpecOptionExample goodsSpecOptionExample = new GoodsSpecOptionExample();
        goodsSpecOptionExample.createCriteria().andSpecIdEqualTo(id);
        GoodsSpecOption goodsSpecOption = new GoodsSpecOption();
        goodsSpecOption.setStatus("1");
        goodsSpecOptionMapper.updateByExampleSelective(goodsSpecOption, goodsSpecOptionExample);
        // 返回删除结果
        return result;
    }

    @Override
    public Specification queryById(Long id) {
        // 查询规格信息
        GoodsSpec goodsSpec = goodsSpecMapper.selectByPrimaryKey(id);
        // 查询规格项信息
        GoodsSpecOptionExample goodsSpecOptionExample = new GoodsSpecOptionExample();
        goodsSpecOptionExample.createCriteria().andSpecIdEqualTo(id);
        List<GoodsSpecOption> goodsSpecOptionList = goodsSpecOptionMapper.selectByExample(goodsSpecOptionExample);
        // 使用自定义的Specification类来存储规格、规格项信息
        Specification specification = new Specification();
        BeanUtils.copyProperties(goodsSpec, specification);
        specification.setOptionList(goodsSpecOptionList);
        // 返回结果
        return specification;
    }

    @Override
    public Integer update(Specification specification) {
        // 设置规格信息状态为正常
        specification.setStatus("0");
        int result = goodsSpecMapper.updateByPrimaryKeySelective(specification);
        // 更新规格项信息，先根据specId删除规格项信息，然后保存
        GoodsSpecOptionExample goodsSpecOptionExample = new GoodsSpecOptionExample();
        goodsSpecOptionExample.createCriteria().andSpecIdEqualTo(specification.getId());
        goodsSpecOptionMapper.deleteByExample(goodsSpecOptionExample);
        // 规格中的规格项信息
        List<GoodsSpecOption> optionList = specification.getOptionList();
        // 遍历规规格项信息，并进行保存
        for (GoodsSpecOption option : optionList) {
            option.setStatus("0");
            option.setSpecId(specification.getId());
            goodsSpecOptionMapper.insertSelective(option);
        }
        return result;
    }
}
