package com.youlexuan.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.pojo.ad.Content;
import com.youlexuan.sellergoods.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Content content,int page,int rows){
        //System.out.println(page+":page");
        PageResult pageResult = contentService.search(content, page, rows);
        //System.out.println(pageResult);
        return pageResult;
    }

    @RequestMapping("/dele")
    public Result delete(long[] ids){
        try{
            contentService.delete(ids);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("/findOne")
    public Content findOne(long id){
        //System.out.println("findOne："+id);
        Content content = contentService.findOne(id);
        //System.out.println("content:"+content);
        return content;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Content content){
        return contentService.add(content);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody  Content content){
        return contentService.update(content);
    }

}
