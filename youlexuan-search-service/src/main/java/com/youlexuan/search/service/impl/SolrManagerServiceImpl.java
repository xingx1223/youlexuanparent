package com.youlexuan.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.youlexuan.mapper.item.ItemMapper;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.pojo.item.ItemQuery;
import com.youlexuan.sellergoods.service.SolrManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SolrManagerServiceImpl implements SolrManagerService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void saveItemToSolr(Long id) {
        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        //查询指定的商品的库存数据
       // criteria.andGoodsIdIn(Arrays.asList(ids));
        criteria.andGoodsIdEqualTo(id);
        List<Item> itemList = itemMapper.selectByExample(query);
        if (itemList != null){
            for (Item item : itemList) {
                String spec = item.getSpec();
                Map specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();
        }
    }

    @Override
    public void deleteItemFromSolr(Long id) {
        if (id != null){
            //for (Long id : ids) {
                SimpleQuery query=new SimpleQuery();
                Criteria criteria=new Criteria("item_goodsid").is(id);
                query.addCriteria(criteria);
                solrTemplate.delete(query);
                solrTemplate.commit();
            //}
        }
    }
}
