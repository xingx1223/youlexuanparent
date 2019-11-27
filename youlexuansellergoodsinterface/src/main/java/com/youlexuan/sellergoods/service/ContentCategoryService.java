package com.youlexuan.sellergoods.service;

import com.youlexuan.entity.PageResult;
import com.youlexuan.pojo.ad.ContentCategory;

import java.util.List;

public interface ContentCategoryService {

    public PageResult findPage(ContentCategory contentCategory,int page, int rows);

    public List<ContentCategory> findAll();
}
