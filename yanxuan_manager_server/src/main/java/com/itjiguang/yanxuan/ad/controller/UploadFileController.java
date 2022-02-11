package com.itjiguang.yanxuan.ad.controller;

import com.itjiguang.yanxuan.utils.FastDFSClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadFileController {

    @PostMapping
    public ResponseEntity uploadFile(MultipartFile[] file) {
        // 封装结果返回给前端
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        // 创建FastDFS工具类
        FastDFSClient fastDFSClient = new FastDFSClient("classpath:/fastdfs/client.properties");
        // 遍历文件
        for (MultipartFile mpf : file) {
            // 获得文件名
            String filename = mpf.getOriginalFilename();
            // 文件扩展名
            String extName = null;
            if (filename != null && !"".equals(filename)) {
                extName = filename.substring(filename.lastIndexOf(".") + 1);
            }
            try {
                // 返回格式 ： "http://"+hostName+"/"+result;
                String path = fastDFSClient.uploadFile(mpf.getBytes(), extName);
                list.add(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("errno", "0");
        map.put("data", list);

        return new ResponseEntity(map, HttpStatus.OK);
    }
}
