package com.youlexuan.sellergoods.service;

import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.pojo.ad.Content;

import java.util.List;

public interface ContentService {


    public  PageResult search(Content content, int page, int rows);

    public void delete(long[] ids);

    Content findOne(long id);

    Result add(Content content);

    Result update(Content content);
    /**
     * 根据广告类型ID查询列表
     * @param categoryId
     * @return
     */
    public List<Content> findByCategoryId(Long categoryId);

}
