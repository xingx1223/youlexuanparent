package com.youlexuan.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.youlexuan.mapper.good.GoodsDescMapper;
import com.youlexuan.mapper.good.GoodsMapper;
import com.youlexuan.mapper.item.ItemCatMapper;
import com.youlexuan.mapper.item.ItemMapper;
import com.youlexuan.pojo.good.Goods;
import com.youlexuan.pojo.good.GoodsDesc;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.pojo.item.ItemCat;
import com.youlexuan.pojo.item.ItemQuery;
import com.youlexuan.sellergoods.service.CmsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取静态页面所需数据
 */
@Service
public class CmsServiceImpl implements CmsService, ServletContextAware {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @Autowired
    private ServletContext servletContext;

    @Override
    public Map<String, Object> findGoodsData(Long goodsId) {
        Map<String,Object> resultMap=new HashMap<>();
        //1.获取商品的数据
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        //2.获取商品的详情数据
        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        //3.获取库存集合的数据
        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemMapper.selectByExample(query);
        //4.获取商品对应的分类数据
        if (goods!=null){
            ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
            ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
            ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
            //封装数据
            resultMap.put("itemCat1",itemCat1);
            resultMap.put("itemCat2",itemCat2);
            resultMap.put("itemCat3",itemCat3);
        }
        //将商品的所有数据封装成Map
        resultMap.put("goods",goods);
        resultMap.put("goodsDesc",goodsDesc);
        resultMap.put("itemList",itemList);
        return resultMap;
    }

    @Override
    public void createStaticPage(Long goodsId, Map<String, Object> rootMap) throws Exception {
        System.out.println("goodsID:"+goodsId);
        //1.获取模板的初始化对象
        Configuration configuration=freeMarkerConfig.getConfiguration();
        //2.获取模板对象
        Template template=configuration.getTemplate("item.ftl");
        //3/创建输出流 指定生成静态页面的位置和名称
        String path=goodsId+".html";
        System.out.println("path:"+path);
        //获取绝对路径
        String realPath=getRealPath(path);
        Writer out=new OutputStreamWriter(new FileOutputStream(new File(realPath)),"utf-8");
        //写入
        template.process(rootMap,out);
        //关流
        out.close();
    }

    private String getRealPath(String path) {
        String realPath=servletContext.getRealPath(path);
        return realPath;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
