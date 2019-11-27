package com.youlexuan.sellergoods.service.impl;
import java.util.List;

import com.youlexuan.mapper.specification.SpecificationOptionMapper;
import com.youlexuan.pojo.specification.SpecificationOption;
import com.youlexuan.pojo.specification.SpecificationOptionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.SpecificationOptionService;

import com.youlexuan.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationOptionServiceImpl implements SpecificationOptionService {

	@Autowired
	private SpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<SpecificationOption> findAll() {
		return specificationOptionMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<SpecificationOption> page=   (Page<SpecificationOption>) specificationOptionMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(SpecificationOption specificationOption) {
		specificationOptionMapper.insert(specificationOption);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(SpecificationOption specificationOption){
		specificationOptionMapper.updateByPrimaryKey(specificationOption);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public SpecificationOption findOne(Long id){
		return specificationOptionMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationOptionMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(SpecificationOption specificationOption, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		SpecificationOptionQuery query=new SpecificationOptionQuery();
		SpecificationOptionQuery.Criteria criteria = query.createCriteria();

		if(specificationOption!=null){
			if(specificationOption.getOptionName()!=null && specificationOption.getOptionName().length()>0){
				criteria.andOptionNameLike("%"+specificationOption.getOptionName()+"%");
			}
		}
		
		Page<SpecificationOption> page= (Page<SpecificationOption>)specificationOptionMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
