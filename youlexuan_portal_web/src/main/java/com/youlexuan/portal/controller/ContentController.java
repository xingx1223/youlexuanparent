package com.youlexuan.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.pojo.ad.Content;
import com.youlexuan.sellergoods.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * 根据广告分类id查询列表
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }

}
