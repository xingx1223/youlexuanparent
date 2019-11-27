package com.youlexuan.manager.controller;

import com.youlexuan.entity.Result;
import com.youlexuan.util.FastDFSClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private String FILE_SERVER_URL = "http://192.168.200.128/";

    @RequestMapping("/uploadFile")
    public Result upload(MultipartFile file){
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //1.获取文件扩展名
        String exName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        try {
            //2.创建一个fastDFSC客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //3.执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), exName);
            //4.拼接返回的url和ip地址，拼接成完整的url
            String url=FILE_SERVER_URL+path;
            //5.返回url
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            //6.上传失败处理
            return new Result(false,"上传失败");
        }
    }
}
