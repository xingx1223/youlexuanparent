package com.youlexuan.sellergoods.service.impl;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.youlexuan.group.SpecificationGroup;
import com.youlexuan.mapper.specification.SpecificationMapper;
import com.youlexuan.mapper.specification.SpecificationOptionMapper;
import com.youlexuan.pojo.specification.Specification;
import com.youlexuan.pojo.specification.SpecificationOption;
import com.youlexuan.pojo.specification.SpecificationOptionQuery;
import com.youlexuan.pojo.specification.SpecificationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.youlexuan.sellergoods.service.SpecificationService;

import com.youlexuan.entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private SpecificationMapper specificationMapper;

	@Autowired
	private SpecificationOptionMapper specificationOptionMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<Specification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		Page<Specification> page=(Page<Specification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(SpecificationGroup specificationGroup) {
		//新增规格表
		specificationMapper.insert(specificationGroup.getSpecification());
		//新增规格选项表
		for (SpecificationOption option: specificationGroup.getSpecificationOptionList()) {
			//规格选项要添加 规格表的外键
			option.setSpecId(specificationGroup.getSpecification().getId());
			specificationOptionMapper.insert(option);
		}
	}
	
	/**
	 * 修改
	 */
	@Override
	public void update(SpecificationGroup specificationGroup){
		//修改规格表
		specificationMapper.updateByPrimaryKey(specificationGroup.getSpecification());

		//先删除规格选项表
		SpecificationOptionQuery query=new SpecificationOptionQuery();
		SpecificationOptionQuery.Criteria criteria = query.createCriteria();
		criteria.andSpecIdEqualTo(specificationGroup.getSpecification().getId());
		specificationOptionMapper.deleteByExample(query);

		//后新增规格选项表
		for (SpecificationOption option:specificationGroup.getSpecificationOptionList()) {
			option.setSpecId(specificationGroup.getSpecification().getId());
			specificationOptionMapper.insert(option);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public SpecificationGroup findOne(Long id){
		SpecificationGroup specificationGroup = new SpecificationGroup();
		//查规格表
		Specification specification = specificationMapper.selectByPrimaryKey(id);
		//查规格选项表
		SpecificationOptionQuery example = new SpecificationOptionQuery();
		SpecificationOptionQuery.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<SpecificationOption> list = specificationOptionMapper.selectByExample(example);
		//将规格表与规格选项表 加入组合对象
		specificationGroup.setSpecification(specification);
		specificationGroup.setSpecificationOptionList(list);
		return specificationGroup;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			System.out.println(id+"::::id");
			//删除规格表
			specificationMapper.deleteByPrimaryKey(id);
			//删除规格选项表
			SpecificationOptionQuery query=new SpecificationOptionQuery();
			SpecificationOptionQuery.Criteria criteria = query.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(query);
		}		
	}

	@Override
	public PageResult findPage(Specification specification, int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);

		SpecificationQuery query=new SpecificationQuery();
		SpecificationQuery.Criteria criteria = query.createCriteria();
		
		if(specification!=null){			
			if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
		}

		Page<Specification> page= (Page<Specification>)specificationMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Specification> selectSpecificationList() {
		return specificationMapper.selectSpecificationList();
	}
}
