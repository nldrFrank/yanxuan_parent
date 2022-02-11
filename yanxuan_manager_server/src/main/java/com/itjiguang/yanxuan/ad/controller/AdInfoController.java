package com.itjiguang.yanxuan.ad.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itjiguang.yanxuan.ad.api.IAdInfoService;
import com.itjiguang.yanxuan.model.AdInfo;
import com.itjiguang.yanxuan.result.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adInfo")
public class AdInfoController {

    @Reference
    private IAdInfoService adInfoService;

    /**
     * 根据angularJs传递的查询参数条件进行分页查询
     * @param currentPage 当前页
     * @param pageSize 每页记录数
     * @param adInfo 查询条件
     * @return PageResult<AdInfo>
     */
    @GetMapping
    public ResponseEntity<PageResult<AdInfo>> query(Integer currentPage, Integer pageSize, AdInfo adInfo) {
        // 处理分页参数
        if (currentPage == null || pageSize == null) {
            currentPage = 1;
            pageSize = Integer.MAX_VALUE;
        }
        // 调用远程服务查询广告信息数据
        PageResult<AdInfo> pageResult = adInfoService.pageQuery(currentPage, pageSize, adInfo);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    /**
     * 新增保存广告信息
     * @param adInfo 广告信息
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity save(@RequestBody AdInfo adInfo) {
        int result = adInfoService.save(adInfo);
        return result > 0 ? new ResponseEntity(HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 修改保存广告信息
     * @param adInfo 广告信息
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity update(@RequestBody AdInfo adInfo) {
        int result = adInfoService.update(adInfo);
        return result > 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * 根据广告信息主键删除数据
     * @param id 主键id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        int delete = adInfoService.deleteById(id);
        return delete > 0 ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 根据主键id查找广告信息
     * @param id 主键id
     * @return ResponseEntity<AdInfo>
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdInfo> queryById(@PathVariable("id") Long id) {
        AdInfo adInfo = adInfoService.queryById(id);
        return new ResponseEntity<>(adInfo, HttpStatus.OK);
    }
}
