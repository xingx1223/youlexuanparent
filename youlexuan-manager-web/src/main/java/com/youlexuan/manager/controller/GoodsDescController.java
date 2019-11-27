package com.youlexuan.manager.controller;
import java.util.List;

import com.youlexuan.pojo.good.GoodsDesc;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.sellergoods.service.GoodsDescService;

import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goodsDesc")
public class GoodsDescController {

	@Reference
	private GoodsDescService goodsDescService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<GoodsDesc> findAll(){
		return goodsDescService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsDescService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody GoodsDesc goodsDesc){
		try {
			goodsDescService.add(goodsDesc);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody GoodsDesc goodsDesc){
		try {
			goodsDescService.update(goodsDesc);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/findOne")
	public GoodsDesc findOne(Long goodsId){
		return goodsDescService.findOne(goodsId);		
	}
	
	/**
	 * 批量删除
	 * @param goodsIds
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] goodsIds){
		try {
			goodsDescService.delete(goodsIds);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goodsDesc
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody GoodsDesc goodsDesc, int page, int rows  ){
		return goodsDescService.findPage(goodsDesc, page, rows);		
	}
	
}
