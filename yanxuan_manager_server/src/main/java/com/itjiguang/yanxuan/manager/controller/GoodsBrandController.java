package com.itjiguang.yanxuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.cluster.merger.SetMerger;
import com.itjiguang.yanxuan.goods.api.IGoodsBrandService;
import com.itjiguang.yanxuan.model.GoodsBrand;
import com.itjiguang.yanxuan.result.PageResult;
import com.itjiguang.yanxuan.result.Result;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class GoodsBrandController {

    private static final Logger LOGGER = Logger.getLogger(GoodsBrandController.class);
    // dubbo引用
    @Reference
    private IGoodsBrandService goodsBrandService;

    /**
     * GET http://localhost:9901/brand/11
     * GET http://localhost:9901/brand?pageNum=1&pageSize=10&name=极光
     *
     * POST http://localhost:9901/brand
     * PUT http://localhost:9901/brand
     * PATCH http://localhost:9901/brand
     *
     * DELETE http://localhost:9901/brand/11
     */

    /**
     * 根据id查询商品品牌信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<GoodsBrand> queryById(@PathVariable("id") Long id) {
        GoodsBrand goodsBrand = goodsBrandService.queryById(id);
        return new ResponseEntity<>(goodsBrand, HttpStatus.OK);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param goodsBrand
     * @return
     */
    @GetMapping
    public ResponseEntity<PageResult<GoodsBrand>> query(Integer currentPage, Integer pageSize, GoodsBrand goodsBrand) {
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        PageResult<GoodsBrand> pageResult = goodsBrandService.pageQuery(currentPage, pageSize, goodsBrand);
        return new ResponseEntity<PageResult<GoodsBrand>>(pageResult, HttpStatus.OK);
    }

    /**
     * 新增保存
     * @param goodsBrand
     */
    @PostMapping
    public ResponseEntity save(@RequestBody GoodsBrand goodsBrand) {
        int result = goodsBrandService.save(goodsBrand);
        if (result > 0) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 更新信息
     * @param goodsBrand
     */
    @PutMapping
    public ResponseEntity update(@RequestBody GoodsBrand goodsBrand) {
        int result = goodsBrandService.update(goodsBrand);
        if (result > 0) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除信息
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        int result = goodsBrandService.delete(id);
        if (result > 0) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    public List<GoodsBrand> queryAll() {
        // 调用远程服务完成商品品牌信息的查询
        List<GoodsBrand> goodsBrandList = goodsBrandService.queryAll();
        // 返回结果
        return goodsBrandList;
    }

    @RequestMapping("/pageQuery")
    public PageResult<GoodsBrand> pageQuery(Integer currentPage, Integer pageSize, GoodsBrand goodsBrand) {
        // 调用远程服务完成商品品牌信息的分页查询
        PageResult<GoodsBrand> pageResult = goodsBrandService.pageQuery(currentPage, pageSize, goodsBrand);
        return pageResult;
    }

    @PostMapping("/save")
    public Result saveBrand(@RequestBody GoodsBrand goodsBrand) {
        // 使用自定义类封装返回结果信息
        Result result = new Result();
        try {
            goodsBrandService.save(goodsBrand);
            // 保存成功
            result.setFlag(true);
            result.setMessage("商品品牌信息保存成功");
        } catch (Exception e) {
            // 保存失败
            result.setFlag(false);
            result.setMessage("商品品牌信息保存失败");
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/update")
    public Result updateBrand(@RequestBody GoodsBrand goodsBrand) {
        // 使用自定义类封装返回结果信息
        Result result = new Result();
        try {
            goodsBrandService.update(goodsBrand);
            // 修改成功
            result.setFlag(true);
            result.setMessage("商品品牌信息修改成功");
        } catch (Exception e) {
            // 修改失败
            result.setFlag(false);
            result.setMessage("商品品牌信息修改失败");
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/delete")
    public Result deleteById(Long id) {
        // 使用自定义类封装返回结果信息
        Result result = new Result();
        try {
            // 调用远程服务进行商品品牌信息的删除
            goodsBrandService.delete(id);
            // 删除成功
            result.setFlag(true);
            result.setMessage("商品品牌信息删除成功");
        } catch (Exception e) {
            // 删除失败
            result.setFlag(false);
            result.setMessage("商品品牌信息删除失败");
            e.printStackTrace();
        }
        return result;
    }
}
