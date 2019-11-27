package com.youlexuan.sellergoods.service;
import java.util.List;

import com.youlexuan.entity.PageResult;
import com.youlexuan.pojo.item.ItemCat;

/**
 * 商品类目服务层接口
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 根据上级ID返回列表
	 * @return
	 */
	public List<ItemCat> findByParentId(Long parentId);


	/**
	 * 返回全部列表
	 * @return
	 */
	public List<ItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(ItemCat item_cat);
	
	
	/**
	 * 修改
	 */
	public void update(ItemCat item_cat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public ItemCat findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(ItemCat item_cat, int pageNum, int pageSize);
	
}
