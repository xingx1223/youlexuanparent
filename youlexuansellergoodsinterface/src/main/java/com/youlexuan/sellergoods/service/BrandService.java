package com.youlexuan.sellergoods.service;

import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.pojo.good.Brand;

import java.util.List;
import java.util.Map;

/**
 *  品牌服务层接口
 */
public interface BrandService {
    /**
     * 返回全部列表
     * @return
     */
    public List<Brand> findAll();
    //返回分页列表
    public PageResult findPage(Integer page, Integer rows);
    //添加
    public Result insert(Brand brand);

    //返回一个品牌实体
    public Brand findOne(long id);
    //修改
    public void update(Brand brand);

    //批量删除
    public void delete(Long [] ids);
    /**
     * 分页
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(Brand brand, int pageNum,int pageSize);

    /**
     * 品牌下拉框数据
     */
    public List<Map> selectOptionList();


}
