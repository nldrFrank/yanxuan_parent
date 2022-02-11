package com.itjiguang.yanxuan.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.goods.api.IGoodsCategoryService;
import com.itjiguang.yanxuan.mapper.GoodsCategoryBrandSpecMapper;
import com.itjiguang.yanxuan.mapper.GoodsCategoryMapper;
import com.itjiguang.yanxuan.mapper.GoodsSpecOptionMapper;
import com.itjiguang.yanxuan.model.*;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsCategoryServiceImpl implements IGoodsCategoryService {
    // log4j日志
//    private final static Logger LOGGER = Logger.getLogger(GoodsCategoryServiceImpl.class);
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    private GoodsCategoryBrandSpecMapper goodsCategoryBrandSpecMapper;
    @Autowired
    private GoodsSpecOptionMapper goodsSpecOptionMapper;
    @Override
    public PageResult<GoodsCategory> pageQuery(Integer currentPage, Integer pageSize, GoodsCategory goodsCategory) {
        // 构建查询条件
        GoodsCategoryExample goodsCategoryExample = null;
        // 是否存在查询条件
        if (goodsCategory != null) {
            // 根据商品类目名称进行查询
            if (goodsCategory.getName() != null && !"".equals(goodsCategory.getName())) {
                goodsCategoryExample = new GoodsCategoryExample();
                // 存放类目名
                String categoryName = new String(goodsCategory.getName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//                try {
//                    categoryName = new String(goodsCategory.getName().getBytes("ISO_8859_1"), "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                goodsCategoryExample.createCriteria().andNameLike("%"+categoryName+"%");
            } else {
                // 当没有根据商品类目名称进行查询时，根据 parentId 进行查询
                if (goodsCategory.getParentId() != null) {
                    goodsCategoryExample = new GoodsCategoryExample();
                    goodsCategoryExample.or().andParentIdEqualTo(goodsCategory.getParentId());
                }
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 查询数据
        Page<GoodsCategory> pageData =  (Page<GoodsCategory>) goodsCategoryMapper.selectByExample(goodsCategoryExample);
        // 构建返回结果
        PageResult<GoodsCategory> pageResult = new PageResult<>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setResult(pageData.getResult());
        return pageResult;
    }

    @Override
    public Integer save(Category category) {
        // 设置信息为正常
        category.setStatus("0");
        // 查询当前子目录存在的数量
        GoodsCategoryExample goodsCategoryExample = new GoodsCategoryExample();
        goodsCategoryExample.or().andParentIdEqualTo(category.getParentId());
        int count = (int) goodsCategoryMapper.countByExample(goodsCategoryExample);
        category.setSortNo(count+1);
        // 保存类目信息
        int result = goodsCategoryMapper.insert(category);
        // 保存关联的品牌、规格信息
        GoodsCategoryBrandSpec relation = category.getRelation();
        relation.setCategoryId(category.getId());
        goodsCategoryBrandSpecMapper.insert(relation);
        return result;
    }

    @Override
    public Integer deleteById(Long id) {
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setId(id);
        // 逻辑删除类目信息
        goodsCategory.setStatus("1");
        // 返回删除结果
        return goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory);
    }

    @Override
    public Category queryById(Long id) {
        // 根据主键查询类目信息
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(id);
        // 查询类目关联信息
        GoodsCategoryBrandSpecExample goodsCategoryBrandSpecExample = new GoodsCategoryBrandSpecExample();
        goodsCategoryBrandSpecExample.or().andCategoryIdEqualTo(id);
        List<GoodsCategoryBrandSpec> relationList = goodsCategoryBrandSpecMapper.selectByExample(goodsCategoryBrandSpecExample);
        // 使用自定义类构建结果
        Category category = new Category();
        BeanUtils.copyProperties(goodsCategory, category);
        if (relationList.size() > 0) {
            category.setRelation(relationList.get(0));
            // 获取规格信息 [{"id":1,"name":"屏幕尺寸"},{"id":4,"name":"运行内存"},{"id":14,"name":"价格"},{"id":18,"name":"颜色"}]
            String specIds = relationList.get(0).getSpecIds();
            List<Map> mapList = JSON.parseArray(specIds, Map.class);
            for (Map tmpMap : mapList) {
                // 获取规格id
                Long specId = new Long ((Integer) tmpMap.get("id"));
                // 创建查询条件
                GoodsSpecOptionExample goodsSpecOptionExample = new GoodsSpecOptionExample();
                goodsSpecOptionExample.createCriteria().andSpecIdEqualTo(specId);
                // 根据查询条件得到查询结果
                List<GoodsSpecOption> optionList = goodsSpecOptionMapper.selectByExample(goodsSpecOptionExample);
                // 存放到map中
                tmpMap.put("optionList", optionList);
            }
            // 设置规格和规格项信息
            category.setSpecList(mapList);
        }
        return category;
    }

    @Override
    public Integer update(Category category) {
        // 更新类目信息
        int result = goodsCategoryMapper.updateByPrimaryKey(category);
        // 更新关联信息
        GoodsCategoryBrandSpec relation = category.getRelation();
        goodsCategoryBrandSpecMapper.updateByPrimaryKey(relation);
        return result;
    }
}
