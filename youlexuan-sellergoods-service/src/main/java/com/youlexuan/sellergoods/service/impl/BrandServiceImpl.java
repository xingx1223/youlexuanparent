package com.youlexuan.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.mapper.good.BrandMapper;
import com.youlexuan.pojo.good.BrandQuery;
import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.pojo.good.Brand;
import com.youlexuan.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    //mapper
    @Autowired
    private BrandMapper brandMapper;

    //查询全部
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectByExample(null);
    }

    //分页查询 ** 手写一个分页信息
    @Override
    public PageResult findPage(Integer pageNum, Integer rows) {
        PageHelper.startPage(pageNum,rows);
        Page<Brand> page=(Page<Brand>)brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //添加
    @Override
    public Result insert(Brand brand) {
        int insert = brandMapper.insert(brand);
        if (insert>0){
            return new Result(true,"添加成功");
        }
        return null;
    }

    //根据id查询一个
    @Override
    public Brand findOne(long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    //修改
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 品牌条件查询
     * @param brand
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(Brand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        BrandQuery query=new BrandQuery();
        ////建立查询条件
        BrandQuery.Criteria criteria = query.createCriteria();
        if (brand!=null){
            if (brand.getName()!=null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<Brand> page= (Page<Brand>)brandMapper.selectByExample(query);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 列表数据:用在模板管理
     */
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
