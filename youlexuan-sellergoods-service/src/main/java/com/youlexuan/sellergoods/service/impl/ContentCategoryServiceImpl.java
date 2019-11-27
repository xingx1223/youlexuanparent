package com.youlexuan.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.entity.PageResult;
import com.youlexuan.mapper.ad.ContentCategoryMapper;
import com.youlexuan.pojo.ad.ContentCategory;
import com.youlexuan.pojo.ad.ContentCategoryQuery;
import com.youlexuan.sellergoods.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Override
    public PageResult findPage(ContentCategory contentCategory, int pageNum, int pageSize) {
        //System.out.println(pageNum+"---"+"=====");
        PageHelper.startPage(pageNum,pageSize);
        //System.out.println(pageNum+"**********"+pageSize);
        ContentCategoryQuery query=new ContentCategoryQuery();
        ContentCategoryQuery.Criteria criteria = query.createCriteria();
        if (contentCategory !=null){
            if (contentCategory.getName()!=null && contentCategory.getName().length()>0){
                criteria.andNameLike("%"+contentCategory.getName()+"%");
            }
        }

        Page<ContentCategory> pages=(Page<ContentCategory>)contentCategoryMapper.selectByExample(query);
        //System.out.println(pages);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public List<ContentCategory> findAll() {
        return contentCategoryMapper.selectByExample(null);
    }
}
