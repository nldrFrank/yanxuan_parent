package com.itjiguang.yanxuan.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.goods.api.IGoodsBrandService;
import com.itjiguang.yanxuan.mapper.GoodsBrandMapper;
import com.itjiguang.yanxuan.model.GoodsBrand;
import com.itjiguang.yanxuan.model.GoodsBrandExample;
import com.itjiguang.yanxuan.result.PageResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 使用dubbo的service注解
 */
@Service
@Transactional
public class GoodsBrandServiceImpl implements IGoodsBrandService {
    // log4j日志
    private final static Logger LOGGER = Logger.getLogger(GoodsBrandServiceImpl.class);
    // 注入商品品牌DAO
    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    /**
     * 返回所有商品品牌信息
     * @return
     */
    @Override
    public List<GoodsBrand> queryAll() {
        // 调用商品品牌DAO完成查询并返回
        return goodsBrandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     * @param currentPage 当前要查询的页面
     * @param pageSize 页面大小
     * @return
     */
    @Override
    public PageResult<GoodsBrand> pageQuery(Integer currentPage, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 将查询的数据封装到Page中
        Page<GoodsBrand> pageData = (Page<GoodsBrand>) goodsBrandMapper.selectByExample(null);
        // 构建返回结果
        PageResult<GoodsBrand> pageResult = new PageResult<GoodsBrand>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setResult(pageData.getResult());
        // 返回结果
        return pageResult;
    }

    @Override
    public PageResult<GoodsBrand> pageQuery(Integer currentPage, Integer pageSize, GoodsBrand goodsBrand) {
        // 输出查询条件到控制台
        LOGGER.info(goodsBrand.getName());
        // 当传递过来的查询条件非空时，构建查询条件
        GoodsBrandExample goodsBrandExample = null;
        if (goodsBrand != null && goodsBrand.getName() != null && !"".equals(goodsBrand.getName())) {
            goodsBrandExample = new GoodsBrandExample();
            // 商品品牌中文名称
            String brandName = null;
            try {
                brandName = new String(goodsBrand.getName().getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            goodsBrandExample.createCriteria().andNameLike("%"+ brandName +"%");
        }
        /*if (goodsBrand != null && goodsBrand.getEnglishName() != null && !"".equals(goodsBrand.getEnglishName())) {
            goodsBrandExample = new GoodsBrandExample();
            goodsBrandExample.createCriteria().andEnglishNameLike("%"+ goodsBrand.getEnglishName() +"%");
        }*/
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 根据查询条件查询，并将查询到的数据封装到Page中
        Page<GoodsBrand> pageData = (Page<GoodsBrand>) goodsBrandMapper.selectByExample(goodsBrandExample);
        // 构建返回结果
        PageResult<GoodsBrand> pageResult = new PageResult<>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setResult(pageData.getResult());
        // 返回结果
        return pageResult;
    }

    @Override
    public int save(GoodsBrand goodsBrand) {
        // 商品状态
        goodsBrand.setIsDelete("0");
        int result = goodsBrandMapper.insert(goodsBrand);
        return result;
    }

    @Override
    public int update(GoodsBrand goodsBrand) {
        int result = goodsBrandMapper.updateByPrimaryKeySelective(goodsBrand);
        return result;
    }

    @Override
    public int delete(Long id) {
        // 根据id查询商品品牌信息
        GoodsBrand brandInfo = goodsBrandMapper.selectByPrimaryKey(id);
        // 修改商品状态信息
        brandInfo.setIsDelete("1");
        // 更新商品品牌信息
        int result = goodsBrandMapper.updateByPrimaryKeySelective(brandInfo);
        return result;
    }

    @Override
    public GoodsBrand queryById(Long id) {
        GoodsBrand goodsBrand = goodsBrandMapper.selectByPrimaryKey(id);
        return goodsBrand;
    }
}
