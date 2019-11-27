package com.xy.solrutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youlexuan.mapper.item.ItemMapper;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 实现商品数据的查询(已审核商品)
 */
@Component
public class SolrUtil {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 导入商品数据
     */
    public void importItemData(){
        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andStatusEqualTo("1");//已审核
        List<Item> items = itemMapper.selectByExample(query);
        System.out.println("======商品列表-=======");
        for (Item item : items) {

            ////将spec字段中的json字符串转换为map
            Map specMap = JSON.parseObject(item.getSpec());
            //给带注解的字段赋值
            item.setSpecMap(specMap);
            //System.out.println(item.getTitle());
        }
        //
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
        System.out.println("==结束");
    }

    public static void main(String[] args) {
        /*applicationContext*.xml*/
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil =(SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }
}
