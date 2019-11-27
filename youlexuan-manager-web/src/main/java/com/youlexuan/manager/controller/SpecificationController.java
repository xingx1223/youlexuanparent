package com.youlexuan.manager.controller;
import java.util.Arrays;
import java.util.List;

import com.youlexuan.group.SpecificationGroup;
import com.youlexuan.pojo.specification.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.youlexuan.sellergoods.service.SpecificationService;

import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<Specification> findAll(){
		List<Specification> specificationList = specificationService.findAll();
		//System.out.println("-------------------------------------------"+specificationList);
		return specificationList;
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return specificationService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param specificationGroup
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody SpecificationGroup specificationGroup){
		System.out.println(specificationGroup);
		try {
			specificationService.add(specificationGroup);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param specificationGroup
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody SpecificationGroup specificationGroup){
		try {
			specificationService.update(specificationGroup);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取组合列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public SpecificationGroup findOne(Long id){
		return specificationService.findOne(id);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
//			System.out.println("controller ids : "+ Arrays.toString(ids));
			specificationService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody Specification specification, int page, int rows  ){
		return specificationService.findPage(specification, page, rows);		
	}

	//
	@RequestMapping("/selectSpecificationList")
	public List<Specification> selectSpecificationList(){
		return specificationService.selectSpecificationList();

	}
	
}
