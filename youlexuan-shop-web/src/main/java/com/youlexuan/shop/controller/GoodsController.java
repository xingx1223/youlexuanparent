package com.youlexuan.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.group.GoodsGroup;
import com.youlexuan.pojo.good.Goods;
import com.youlexuan.sellergoods.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<Goods> findAll(){
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goodsGroup
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody GoodsGroup goodsGroup){
		//获取登录名
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goodsGroup.getGoods().setSellerId(sellerId);//设置商家ID
		System.out.println(goodsGroup.toString());
		try {
			goodsService.add(goodsGroup);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goodsGroup
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody GoodsGroup goodsGroup){
/**
 * 代码解释：出于安全考虑，在商户后台执行的商品修改，必须要校验提交的商品属于该商户
 */

		//校验是否是当前商家的id
		GoodsGroup goodsGroup2=goodsService.findOne(goodsGroup.getGoods().getId());
		//获取当前登录的商家id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//如果传递过来的商家id并不是当前
		if (!goodsGroup2.getGoods().getSellerId().equals(sellerId) || !goodsGroup.getGoods().getSellerId().equals(sellerId)){
			return new Result(false,"操作非法");
		}

		try {
			goodsService.update(goodsGroup);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public GoodsGroup findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody Goods goods, int page, int rows  ){
		//获取商家id
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//添加查询条件
		goods.setSellerId(sellerId);
		return goodsService.findPage(goods, page, rows);		
	}
	
}
