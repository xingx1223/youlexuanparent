package com.youlexuan.manager.controller;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.youlexuan.group.GoodsGroup;
import com.youlexuan.pojo.good.Goods;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.sellergoods.service.CmsService;
import com.youlexuan.sellergoods.service.SolrManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.sellergoods.service.GoodsService;

import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;

import javax.jms.Destination;


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
	@Reference
	private SolrManagerService solrManagerService;
	@Reference
	private CmsService cmsService;
	//用于发送solr导入的消息
	@Autowired
	private Destination queueSolrDestination;
	@Autowired
	private JmsTemplate jmsTemplate;

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
			/*for (Long id : ids) {
				solrManagerService.deleteItemFromSolr(id);
			}*/
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
		return goodsService.findPage(goods, page, rows);		
	}

	/**
	 * 更新状态
	 * @param ids
	 * @param status
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			if (ids != null) {
				goodsService.updateStatus(ids, status);
				//按照SPU ID查询 SKU列表(状态为1)
				/*if ("1".equals(status)) {//审核通过
					solrManagerService.saveItemToSolr(ids);
					for (Long id : ids) {
						//根据商品的id获取商品的详情数据，并且根据详情数据和模板生成详情的页面
						Map<String,Object> goodsData =cmsService.findGoodsData(id);
						cmsService.createStaticPage(id,goodsData);
					}
				}*/
			}
			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}
	}

	//测试生成静态页面
	@RequestMapping("/testPage")
	public Boolean testCreatePage(Long goodsId){
		try{
			System.out.println(1+"goodid:"+goodsId);
			Map<String,Object> goodsData = cmsService.findGoodsData(goodsId);
			System.out.println("gooData"+goodsData);
			cmsService.createStaticPage(goodsId,goodsData);
			System.out.println(3);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
