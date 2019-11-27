package com.youlexuan.sellergoods.service.impl;
import java.util.List;

import com.youlexuan.mapper.item.ItemCatMapper;
import com.youlexuan.pojo.item.ItemCat;
import com.youlexuan.pojo.item.ItemCatQuery;
import com.youlexuan.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.ItemCatService;

import com.youlexuan.entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 商品类目服务实现层
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private ItemCatMapper itemCatMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 根据上级ID查询列表
	 */
	@Override
	public List<ItemCat> findByParentId(Long parentId) {
		ItemCatQuery query=new ItemCatQuery();
		ItemCatQuery.Criteria criteria = query.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		/**
		 * 每次执行查询的时候，一次性读取缓存进行存储（因为每次增删改都要执行此方法！！！）
		 */
		List<ItemCat> itemCatList = findAll();
		for (ItemCat itemCat : itemCatList) {
			redisTemplate.boundHashOps("categoryList").put(itemCat.getName(),itemCat.getTypeId());
		}
		System.out.println("更新缓存:商品分类表");

		return  itemCatMapper.selectByExample(query);
	}

	/**
	 * 查询全部
	 */
	@Override
	public List<ItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<ItemCat> page=   (Page<ItemCat>) itemCatMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(ItemCat itemCat) {
		itemCatMapper.insert(itemCat);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(ItemCat itemCat){
		itemCatMapper.updateByPrimaryKey(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public ItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			itemCatMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
	@Override
	public PageResult findPage(ItemCat itemCat, int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		//ItemQuery query=new ItemQuery();
		ItemCatQuery query=new ItemCatQuery();
		ItemCatQuery.Criteria criteria = query.createCriteria();

        if(itemCat!=null){
            if(itemCat.getName()!=null && itemCat.getName().length()>0){
                criteria.andNameLike("%"+itemCat.getName()+"%");
            }
		}
		
		Page<ItemCat> page= (Page<ItemCat>)itemCatMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
