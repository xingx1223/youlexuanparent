package com.youlexuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.entity.PageResult;
import com.youlexuan.pojo.ad.ContentCategory;
import com.youlexuan.sellergoods.service.ContentCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/findAll")
    public List<ContentCategory> findAll(){
        List<ContentCategory> contentCategoryList = contentCategoryService.findAll();
        System.out.println(contentCategoryList);
        return contentCategoryList;
    }

    /**
     * 分页+查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody ContentCategory contentCategory, int page, int rows){
        System.out.println(page+"+++"+rows);
        PageResult pageResult = contentCategoryService.findPage(contentCategory, page, rows);
        //System.out.println(pageResult+"---");
        return pageResult;
    }
}
