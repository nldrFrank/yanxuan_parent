package com.itjiguang.yanxuan.seller.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itjiguang.yanxuan.mapper.AccountMapper;
import com.itjiguang.yanxuan.mapper.SellerShopMapper;
import com.itjiguang.yanxuan.model.Account;
import com.itjiguang.yanxuan.model.SellerShop;
import com.itjiguang.yanxuan.model.SellerShopExample;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.seller.api.ISellerInfoService;
import com.itjiguang.yanxuan.viewmodel.SellerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

@Service
@Transactional
public class SellerInfoServiceImpl implements ISellerInfoService {
    // 注入 AccountMapper 用于操作 account 数据库
    @Autowired
    private AccountMapper accountMapper;
    // 注入 SellerShopMapper 用户操作 seller_shop 数据库
    @Autowired
    private SellerShopMapper sellerShopMapper;
    /**
     * 保存商家信息
     * @param sellerInfo
     * @return
     */
    @Override
    public Integer save(SellerInfo sellerInfo) {
        // 从商家信息中获取系统登录名称、登陆密码
        String loginName = sellerInfo.getLoginName();
        String password = sellerInfo.getPassword();
        // 创建账户对象
        Account account = new Account();
        // 设置账户名
        account.setLoginName(loginName);
        // 使用springSecurity进行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        // 设置密码
        account.setPassword(encodePassword);
        // 从商家信息中获取电话、邮箱
        String phone = sellerInfo.getLinkmanPhone();
        String email = sellerInfo.getLinkmanEmail();
        // 设置电话
        account.setPhone(phone);
        // 设置邮箱
        account.setEmail(email);
        // 设置创建时间
        account.setCreateDate(new Date());
        // 设置账号状态为有效
        account.setStatus("0");
        // 使用 accountMapper 进行账号信息的保存
        accountMapper.insert(account);
        // 关联账户主键ID
        sellerInfo.setAccountId(account.getId());
        // 设置商家状态 0-待审核；1-审核通过；2-审核退回；3-已关闭
        sellerInfo.setStatus("0");
        // 使用 sellerShopMapper 进行商家信息的保存,并返回结果
        return sellerShopMapper.insert(sellerInfo);
    }

    /**
     * 根据传入参数查询商家信息
     * @param currentPage
     * @param pageSize
     * @param sellerShop
     * @return
     */
    @Override
    public PageResult<SellerShop> pageQuery(Integer currentPage, Integer pageSize, SellerShop sellerShop) {
        // 创建查询条件
        SellerShopExample sellerShopExample = new SellerShopExample();
        // 查询条件是否存在
        if (sellerShop != null) {
            // 商家名称
            if (sellerShop.getName() != null && !"".equals(sellerShop.getName())) {
                // 商家店铺名
                String sellerShopName = null;
                try {
                    // 转换成utf-8编码
                    sellerShopName = new String(sellerShop.getName().getBytes("ISO-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 添加查询项
                sellerShopExample.createCriteria().andNameLike("%"+sellerShopName+"%");
            }
            // 商家状态
            if (sellerShop.getStatus() != null && !"".equals(sellerShop.getStatus())) {
                // 添加查询项
                sellerShopExample.createCriteria().andStatusEqualTo(sellerShop.getStatus());
            }
        }
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        // 根据查询条件查询，并将查询到的数据封装到Page中
        Page<SellerShop> pageData = (Page<SellerShop>)sellerShopMapper.selectByExample(sellerShopExample);
        // 构建返回结果并返回
        PageResult<SellerShop> pageResult = new PageResult<>();
        pageResult.setResult(pageData.getResult());
        pageResult.setTotal(pageData.getTotal());
        return pageResult;
    }

    @Override
    public Integer update(SellerShop sellerShop) {
        // 设置更新时间
        sellerShop.setUpdateDate(new Date());
        // 返回受影响的结果数
        return sellerShopMapper.updateByPrimaryKeySelective(sellerShop);
    }
}
