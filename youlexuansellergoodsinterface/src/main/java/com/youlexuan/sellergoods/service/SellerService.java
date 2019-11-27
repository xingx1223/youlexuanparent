package com.youlexuan.sellergoods.service;
import java.util.List;

import com.youlexuan.entity.PageResult;
import com.youlexuan.pojo.seller.Seller;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<Seller> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Seller seller);
	
	
	/**
	 * 修改
	 */
	public void update(Seller seller);
	

	/**
	 * 根据ID获取实体
	 * @param sellerId
	 * @return
	 */
	public Seller findOne(String sellerId);
	
	
	/**
	 * 批量删除
	 * @param sellerIds
	 */
	public void delete(String[] sellerIds);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(Seller seller, int pageNum, int pageSize);

	/**
	 * 更改状态
	 * @param sellerId
	 * @param status
	 */
    void updateStatus(String sellerId, String status);
}
