package com.youlexuan.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.entity.PageResult;
import com.youlexuan.sellergoods.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemsearch")
public class SearchController {

    /*@Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map searchMap ){
        System.out.println(searchMap);
        return  itemSearchService.search(searchMap);
    }*/

    @Reference
    private  ItemSearchService itemSearchService;
    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody  Map searchMap){
        return itemSearchService.search(searchMap);
    }
}
