package com.youlexuan.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.sellergoods.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

public class ItemSearchServiceImpl2 implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        //获取查询条件
        String keywords = String.valueOf(searchMap.get("keywords"));
        Integer pageNo = Integer.parseInt(String.valueOf(searchMap.get("pageNo")));
        //获取每页查询条数
        Integer pageSize = Integer.parseInt(String.valueOf(searchMap.get("pageSize")));


        Map<String,Object> resultMap=new HashMap<>();
        //封装查询对象
        Query query=new SimpleQuery();
        //添加查询条件
        Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
        //将查询的条件放入查询的对象
        query.addCriteria(criteria);
        if (pageNo == null || pageNo <= 0){
            pageNo=1;
        }
        //当前页开始是第几条
        Integer start = (pageNo-1)*pageSize;
        //设置第几条开始
        query.setOffset(start);
        //每页查询多少条数据
        query.setRows(pageSize);
        //去solr查询并返回结果
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);
        resultMap.put("rows",page.getContent());
        return resultMap;
    }

}
