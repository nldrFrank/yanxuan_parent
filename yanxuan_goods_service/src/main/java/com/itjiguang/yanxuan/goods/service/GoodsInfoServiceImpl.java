package com.itjiguang.yanxuan.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.goods.api.IGoodsInfoService;
import com.itjiguang.yanxuan.mapper.*;
import com.itjiguang.yanxuan.model.*;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.viewmodel.GoodsInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
public class GoodsInfoServiceImpl implements IGoodsInfoService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private SellerShopMapper sellerShopMapper;
    @Autowired
    private GoodsSpuMapper goodsSpuMapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private GoodsBrandMapper goodsBrandMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Override
    public int save(GoodsInfo goodsInfo) {
        // 获取店铺信息
        SellerShop sellerShop = this.getSellerShop(goodsInfo.getCreatePerson());
        // 设置店铺信息
        goodsInfo.setSellerId(sellerShop.getId());
        goodsInfo.setSellerName(sellerShop.getName());
        goodsInfo.setStatus("0");
        // 通过goodsInfo中的brandId查询goods_brand数据库对应的记录
        GoodsBrand goodsBrand = goodsBrandMapper.selectByPrimaryKey(goodsInfo.getBrandId());
        // 设置品牌名
        goodsInfo.setBrandName(goodsBrand.getName());
        // 通过goodsInfo中的category3Id查询goods_category数据库对应的记录
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goodsInfo.getCategory3Id());
        // 设置类目名
        goodsInfo.setCategoryName(goodsCategory.getStructName() + ">" + goodsCategory.getName());
        // [商品品牌] 商品名称 商品标题
        String label = "[" + goodsInfo.getBrandName() + "] " + goodsInfo.getName() + " " + goodsInfo.getLabel();
        // 设置商品label
        goodsInfo.setLabel(label);
        // 坑一 保存商品信息
        int insert = goodsSpuMapper.insert(goodsInfo);
        // 保存SKU
        this.saveSkuList(goodsInfo);
        // 返回结果
        return insert;
    }

    /**
     * 保存SPU关联的SKU信息
     * @param goodsInfo 商品信息
     */
    private void saveSkuList(GoodsInfo goodsInfo) {
        // sku信息
        List<GoodsSku> skuList = goodsInfo.getSkuList();
        for (GoodsSku goodsSku : skuList) {
            // 遇到的坑：只有当goodsSpu插入后才会生成goodsId
            // 设置goods_id
            goodsSku.setGoodsId(goodsInfo.getId());
            // 设置label
            goodsSku.setLabel(goodsInfo.getLabel());
            // 设置brand_name
//            goodsSku.setBrandName(goodsBrand.getName());
            goodsSku.setBrandName(goodsInfo.getBrandName());
            // 设置category_name
//            goodsSku.setCategoryName(goodsCategory.getStructName() + ">" + goodsCategory.getName());
            goodsSku.setCategoryName(goodsInfo.getCategoryName());
            // 设置seller_name
            goodsSku.setSellerName(goodsInfo.getSellerName());
            // 设置seller_id
            goodsSku.setSellerId(goodsInfo.getSellerId());
            // 设置on_sale
            goodsSku.setOnSale("0");
            // 设置status
            goodsSku.setStatus("0");
            // 设置create_person
            // 设置create_date
            // 设置update_person
            // 设置update_date
            // 保存规格信息，插入到goods_sku数据库中
            goodsSkuMapper.insert(goodsSku);
        }
    }

    /**
     * 根据登陆用户名查询到关联的店铺信息
     * @param username 登录用户名
     * @return SellerShop
     */
    private SellerShop getSellerShop(String username) {
        // 根据登录的用户名得到店铺信息
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andLoginNameEqualTo(username);
        // 查询得到账户信息
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        Account account = null;
        if (accounts != null && accounts.size() > 0) {
            account = accounts.get(0);
        }
        // 查询商家店铺信息
        SellerShopExample sellerShopExample = new SellerShopExample();
        // 构建条件，商家店铺信息中的accountId等于account的id
        sellerShopExample.createCriteria().andAccountIdEqualTo(account.getId());
        List<SellerShop> sellerShops = sellerShopMapper.selectByExample(sellerShopExample);
        SellerShop sellerShop = null;
        if (sellerShops != null && sellerShops.size() > 0) {
            sellerShop = sellerShops.get(0);
        }
        return sellerShop;
    }

    @Override
    public PageResult<GoodsSpu> pageQuery(Integer currentPage, Integer pageSize, GoodsSpu goodsSpu) {
        // 创建查询条件
        GoodsSpuExample goodsSpuExample = new GoodsSpuExample();
        GoodsSpuExample.Criteria criteria = goodsSpuExample.createCriteria();
        // 根据商品的名称进行模糊查询
        if (goodsSpu.getName() != null && !"".equals(goodsSpu.getName())) {
            String spuName = new String(goodsSpu.getName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            criteria.andNameLike("%" + spuName + "%");
        }
        // 根据商品状态进行查询
        if (goodsSpu.getStatus() != null && !"".equals(goodsSpu.getStatus())) {
            criteria.andStatusEqualTo(goodsSpu.getStatus());
        }
        if (goodsSpu.getCreatePerson() != null && !"".equals(goodsSpu.getCreatePerson())) {
            // 获取当前店铺的信息，也就是createPerson所关联的店铺信息
            SellerShop sellerShop = this.getSellerShop(goodsSpu.getCreatePerson());
            if (sellerShop != null) {
                // 根据店铺Id进行等值查询
                criteria.andSellerIdEqualTo(sellerShop.getId());
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 根据创建的条件查询数据
        Page<GoodsSpu> pageData = (Page<GoodsSpu>) goodsSpuMapper.selectByExample(goodsSpuExample);
        // 创建返回结果
        PageResult<GoodsSpu> pageResult = new PageResult<>();
        // 设置返回结果
        pageResult.setResult(pageData.getResult());
        // 设置总记录数
        pageResult.setTotal(pageData.getTotal());
        // 返回结果
        return pageResult;
    }

    @Override
    public GoodsInfo queryById(Long id) {
        // 查询spu信息
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(id);
        // 构建查询sku信息
        GoodsSkuExample goodsSkuExample = new GoodsSkuExample();
        goodsSkuExample.createCriteria().andGoodsIdEqualTo(id);
        List<GoodsSku> skuList = goodsSkuMapper.selectByExample(goodsSkuExample);
        // 构建返回结果
        GoodsInfo goodsInfo = new GoodsInfo();
        BeanUtils.copyProperties(goodsSpu, goodsInfo);
        goodsInfo.setSkuList(skuList);
        return goodsInfo;
    }

    /**
     * 原始数据中包含SPU、SKU信息
     * 1.根据主键更新SPU信息
     * 2.删除所有SKU信息
     * 3.保存SKU信息
     * @param goodsInfo 商品信息
     * @return 影响记录数
     */
    @Override
    public int update(GoodsInfo goodsInfo) {
        // 更新时重新设置商品label
        if (!goodsInfo.getLabel().contains("[")) {
            // [商品品牌] 商品名称 商品标题
            String label = "[" + goodsInfo.getBrandName() + "] " + goodsInfo.getName() + " " + goodsInfo.getLabel();
            goodsInfo.setLabel(label);
        }
        // 根据主键更新SPU信息
        int update = goodsSpuMapper.updateByPrimaryKey(goodsInfo);
        // 删除当前商品关联的SKU信息
        GoodsSkuExample goodsSkuExample = new GoodsSkuExample();
        goodsSkuExample.createCriteria().andGoodsIdEqualTo(goodsInfo.getId());
        goodsSkuMapper.deleteByExample(goodsSkuExample);
        // 保存新的SKU信息
        this.saveSkuList(goodsInfo);
        return update;
    }

    /**
     * 更改商品状态
     * @param goodsSpu 商品信息
     * @return
     */
    @Override
    public int audit(GoodsSpu goodsSpu) {
        // 更新SPU的信息
        int update = goodsSpuMapper.updateByPrimaryKeySelective(goodsSpu);
        // 构建SKU的条件
        GoodsSkuExample goodsSkuExample = new GoodsSkuExample();
        goodsSkuExample.createCriteria().andGoodsIdEqualTo(goodsSpu.getId());
        // SKU
        GoodsSku goodsSku = new GoodsSku();
        // 设置SKU的状态
        goodsSku.setStatus(goodsSpu.getStatus());
        // 根据构建的SKU条件更新SKU的信息
        goodsSkuMapper.updateByExampleSelective(goodsSku, goodsSkuExample);
        return update;
    }

    /**
     * 逻辑删除商品信息
     * // 1.goods_sku的status设置为3（删除）
     * // 2.根据goods_sku表中的goods_id查询goods_spu信息
     * 3.goods_spu的status设置为3（删除）
     * @param id 商品主键id
     * @return 记录数
     */
    @Override
    public int delete(Long id) {
        // 根据id查询sku信息
        // GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(id);
        // 设置status为3
        // goodsSku.setStatus("3");
        // 根据goodsSku中的goods_id查询得到goods_spu信息
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(id);
        // 设置status为3
        goodsSpu.setStatus("3");
        // 更新SKU信息
        // int result = goodsSkuMapper.updateByPrimaryKeySelective(goodsSku);
        // 更新SPU信息
        int result = goodsSpuMapper.updateByPrimaryKeySelective(goodsSpu);
        return result;
    }
}
