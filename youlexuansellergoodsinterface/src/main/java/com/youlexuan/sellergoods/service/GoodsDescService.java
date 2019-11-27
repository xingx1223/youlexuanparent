package com.youlexuan.sellergoods.service;
import java.util.List;

import com.youlexuan.entity.PageResult;
import com.youlexuan.pojo.good.GoodsDesc;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsDescService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<GoodsDesc> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(GoodsDesc goods_desc);
	
	
	/**
	 * 修改
	 */
	public void update(GoodsDesc goods_desc);
	

	/**
	 * 根据ID获取实体
	 * @param goodsId
	 * @return
	 */
	public GoodsDesc findOne(Long goodsId);
	
	
	/**
	 * 批量删除
	 * @param goodsIds
	 */
	public void delete(Long[] goodsIds);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(GoodsDesc goods_desc, int pageNum, int pageSize);
	
}
