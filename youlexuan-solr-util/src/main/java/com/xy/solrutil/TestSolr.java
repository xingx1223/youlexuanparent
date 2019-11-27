package com.xy.solrutil;

import com.youlexuan.pojo.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class TestSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testSave(){
        ArrayList<Item> items = new ArrayList<>();
        for (long i=1;i<100;i++){
            Item item=new Item();
            item.setTitle("三星手机"+i);
            item.setId(i);
            item.setCategory("手机");
            item.setPrice(new BigDecimal("1111"));
            item.setBrand("三星");
            items.add(item);
        }
        //保存
        solrTemplate.saveBeans(items);
        //提交
        solrTemplate.commit();
    }

    @Test
    public void testIndexDelete(){
        //根据主键域 id
       // solrTemplate.deleteById("1");
        //创建查询对象
        SimpleQuery query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
