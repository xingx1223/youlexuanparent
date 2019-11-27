package com.youlexuan.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.sellergoods.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
//import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;


import java.util.*;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    //redis缓存数据库
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 实现接口的方法
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map searchMap) {
        Map resultMap = new HashMap();
        //关键字空格处理
        String  keywords =(String) searchMap.get("keywords");
        System.out.println("key:"+keywords);
        searchMap.put("keywords",keywords.replace(" ",""));

        System.out.println("keywords:"+keywords);
        //
        List<String> categoryList = findGroupCategoryList(searchMap);
        resultMap.put("categoryList",categoryList);//categoryList
        System.out.println("categorylist"+categoryList);

        //1、根据关键字查询（高亮显示） 高亮显示处理....
        resultMap = highLightSearch(searchMap);

        //3.查询品牌和规格列表
        String categoryName=(String)searchMap.get("category");
        if(!"".equals(categoryName)){//如果有分类名称
            resultMap.putAll(findSpecListAndBrandList(categoryName));
        }else{//如果没有分类名称，按照第一个查询
            if(categoryList.size()>0){
                resultMap.putAll(findSpecListAndBrandList(categoryList.get(0)));
            }
        }
        return resultMap;
    }

    /**根据参数关键字
     * 到solr中查询（分页）总页数 总条数
     * 高光显示搜索数据
     * @param paramMap
     * @return
     */
    private Map<String,Object> highLightSearch(Map paramMap) {
        //获取关键字
        String keywords = String.valueOf(paramMap.get("keywords"));
        //当前页
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        //每页查询条数
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //封装查询对象
        SimpleHighlightQuery simpleHighlightQuery = new SimpleHighlightQuery();


        //查询的条件对象
        //Criteria criteria = simpleHighlightQuery.getCriteria();
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //将查询的条件对象放入
        simpleHighlightQuery.addCriteria(criteria);
        //计算从第几条开是查询
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        Integer start = (pageNo - 1) * pageSize;
        //设置从第几页查询
        simpleHighlightQuery.setOffset(start);
        //设置每页查询几条
        simpleHighlightQuery.setRows(pageSize);

        //创建高亮显示对象
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置哪个域需要高亮显示
        highlightOptions.addField("item_title");

        //高亮的前缀
        highlightOptions.setSimplePrefix("<em style=\"color:red\">");
        //高亮的后缀
        highlightOptions.setSimplePostfix("</em>");
        //将高亮加入到查询对象中
        simpleHighlightQuery.setHighlightOptions(highlightOptions);

        //1.2按分类筛选
        if(!"".equals(paramMap.get("category"))){
            Criteria filterCriteria=new Criteria("item_category").is(paramMap.get("category"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            simpleHighlightQuery.addFilterQuery(filterQuery);
        }
        //1.3按品牌筛选
        if(!"".equals(paramMap.get("brand"))){
            Criteria filterCriteria=new Criteria("item_brand").is(paramMap.get("brand"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            simpleHighlightQuery.addFilterQuery(filterQuery);
        }
        // 获取页面点击规格的过滤条件
        String spec = String.valueOf(paramMap.get("spec"));
        //1.4过滤规格
        if(spec!=null && !"".equals(spec)){
            //Map<String,Object> specMap= (Map) paramMap.get("spec");
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            if (specMap != null && specMap.size() >0){
                Set<Map.Entry<String, String>> entries = specMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    //
                    FilterQuery filterQuery=new SimpleFilterQuery();
                    Criteria filterCriteria=new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
                    filterQuery.addCriteria(filterCriteria);
                    simpleHighlightQuery.addFilterQuery(filterQuery);
                }
            }
        }

        //对价格进行过滤
        String price=String.valueOf(paramMap.get("price"));
        if (price !=null && !"".equals(price)){
            System.out.println("price!=null:"+price);
            String[] split = price.split("-");
            System.out.println("split[]:"+Arrays.toString(split));
            if (split != null && split.length == 2) {
                //价格判断
                if (!"*".equals(split[1])) {
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    Criteria filterCriteria = new Criteria("item_price").between(split[0], split[1]);
                    filterQuery.addCriteria(filterCriteria);
                    simpleHighlightQuery.addFilterQuery(filterQuery);
                }else {
                    FilterQuery filterQuery = new SimpleFilterQuery();
                    Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(split[0]);
                    filterQuery.addCriteria(filterCriteria);
                    simpleHighlightQuery.addFilterQuery(filterQuery);
                }
            }
        }

        //价格排序
        String sortType = String.valueOf(paramMap.get("sort"));//asc desc
        String sortField = String.valueOf(paramMap.get("sortField"));//排序字段
        System.out.println(sortField+":");
        //添加排序条件
        if (sortType !=null  &&  !sortType.equals("") && sortField!=null && !"".equals(sortField)){
            //if (sortType.equals("ASC")) {

            if ("ASC".equals(sortType)) {
                //创建排序对象，枚举 一组常量的值  价格域    从页面传回来的价格
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                // 将排序对象 放入查询对象中
                simpleHighlightQuery.addSort(sort);
            }
            if ("DESC".equals(sortType)) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                simpleHighlightQuery.addSort(sort);
            }
        }
        System.out.println("sss");


        //查询并返回结果
        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(simpleHighlightQuery, Item.class);

        //获取带高亮的集合
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        List<Item> itemList =new ArrayList<>();

        //遍历高亮集合
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            Item item = itemHighlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if (highlights != null && highlighted.size() > 0) {
                //获取高亮的标题集合
                List<String> highlightTitle = highlights.get(0).getSnipplets();
                if (highlightTitle != null && highlightTitle.size() > 0) {
                    //获取高亮标题
                    String title = highlightTitle.get(0);
                    item.setTitle(title);
                }
            }
            itemList.add(item);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("rows",itemList);
        resultMap.put("totalPage",items.getTotalPages());
        resultMap.put("total",items.getTotalElements());
        return resultMap;
    }

    /**
     * 查询分类列表
     *根据查询的参数，到solr中获取对应的分类结果，
     * 因为分类有重复  按分组的方式去重复
     * @param paramMap
     * @return
     */
    private List<String> findGroupCategoryList(Map paramMap){
        List<String> list=new ArrayList<>();
        //获取关键字
        String keywords = String.valueOf(paramMap.get("keywords"));
        //System.out.println("keywords:"+keywords);

        SimpleQuery query=new SimpleQuery();

        Criteria criteria=new Criteria("item_keywords").is(keywords);

        query.addCriteria(criteria);
        //创建分组对象
        GroupOptions groupOptions=new GroupOptions();
        //设置根据分类域进行分组
        groupOptions.addGroupByField("item_category");
        //将分组对象放入查询对象中
        query.setGroupOptions(groupOptions);

        //得到分组页
        GroupPage<Item> itemGroupPage = solrTemplate.queryForGroupPage(query, Item.class);
        //根据列分组得到结果集
        GroupResult<Item> groupResult = itemGroupPage.getGroupResult("item_category");
        ///得到分组入口集合
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        System.out.println("groupEntries:");
        for (GroupEntry<Item> groupEntry : groupEntries) {
            list.add(groupEntry.getGroupValue());//将分组结果的名称封装到返回值中
            //System.out.println("groupEntry:"+groupEntry);
        }
        //System.out.println("list:");
        return list;
    }

    /**
     * 查询品牌和规格列表
     * 根据分类名称查询对应的品牌集合和规格集合
     * @param categoryName 分类名称
     * @return
     */
    private Map findSpecListAndBrandList(String categoryName){
        Map map=new HashMap();
        //根据分类名称到redis中查询对应的模板id//Constants.CATEGORY_LIST_REDIS
        Long templateId = (Long) redisTemplate.boundHashOps("categoryList").get(categoryName);
        //System.out.println("templateId"+templateId);
        //根据模板id去redis中查询对应的品牌集合
        if (templateId !=null){
            //根据模板ID查询品牌列表
            List<Map> brandList = (List<Map>)redisTemplate.boundHashOps("brandList").get(templateId);
            //System.out.println("brandList");
            map.put("brandList",brandList);
            //根据模板ID查询规格列表
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList",specList);
            //System.out.println("specList");
        }
        return map;
    };




    /**
     * 根据关键字搜索列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        SimpleQuery query=new SimpleQuery();

        Map<String,Object> resultMap=new HashMap<>();

        //1.1关键字查询....../
        List<String> categoryList = findGroupCategoryList(searchMap);
        resultMap.put("categoryList",categoryList);

        //1.2按分类筛选
        if(!"".equals(searchMap.get("category"))){
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.3按品牌筛选
        if(!"".equals(searchMap.get("brand"))){
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.4过滤规格
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map) searchMap.get("spec");
            for(String key:specMap.keySet() ){
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //.......

        //1、根据关键字查询（高亮显示） 高亮显示处理....
        resultMap = highLightSearch(searchMap);

        //3.查询品牌和规格列表
        String categoryName=(String)searchMap.get("category");
        if(!"".equals(categoryName)){//如果有分类名称
            resultMap.putAll(findSpecListAndBrandList(categoryName));
        }else{//如果没有分类名称，按照第一个查询
            if(categoryList.size()>0){
                resultMap.putAll(findSpecListAndBrandList(categoryList.get(0)));
            }
        }


        return resultMap;
    }
}
