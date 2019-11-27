package com.youlexuan.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.youlexuan.mapper.specification.SpecificationOptionMapper;
import com.youlexuan.mapper.template.TypeTemplateMapper;
import com.youlexuan.pojo.specification.SpecificationOption;
import com.youlexuan.pojo.specification.SpecificationOptionQuery;
import com.youlexuan.pojo.template.TypeTemplate;
import com.youlexuan.pojo.template.TypeTemplateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.TypeTemplateService;

import com.youlexuan.entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private RedisTemplate redisTemplate;
	//模板
	@Autowired
	private TypeTemplateMapper typeTemplateMapper;
	//规格
	@Autowired
	private SpecificationOptionMapper specificationOptionMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TypeTemplate> page=   (Page<TypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
	@Override
	public PageResult findPage(TypeTemplate typeTemplate, int pageNum, int pageSize) {

		List<TypeTemplate> templateList =typeTemplateMapper.selectByExample(null);
		for (TypeTemplate typeTemplate1 : templateList) {
			//JSON.parseArray():把json类型数据转换成数组格式
			List<Map> brandList = JSON.parseArray(typeTemplate1.getBrandIds(), Map.class);
			redisTemplate.boundHashOps("brandList").put(typeTemplate1.getId(),brandList);
			System.out.println("brandlist:");
			//存储规格列表
			List<Map> specList = findSpecList(typeTemplate1.getId());
			System.out.println("spec:"+specList);
			redisTemplate.boundHashOps("specList").put(typeTemplate1.getId(),specList);
		}

		PageHelper.startPage(pageNum, pageSize);

		TypeTemplateQuery query=new TypeTemplateQuery();
		TypeTemplateQuery.Criteria criteria = query.createCriteria();

		if(typeTemplate!=null){			
			if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}	
		}
		
		Page<TypeTemplate> page= (Page<TypeTemplate>)typeTemplateMapper.selectByExample(query);
		//System.out.println(page+"page");
		// 存入数据到缓存
		//saveToRedis();
		System.out.println("更新缓存：品牌。规格数据");
		return new PageResult(page.getTotal(), page.getResult());
	}

	//规格选项
	@Override
	public List<Map> findSpecList(Long id) {
		//查询模板
		TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
		List<Map> maps = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
		if (maps !=null) {
			for (Map map : maps) {
				/// 5 遍历 根据规格id  查询对应的规格选项数据
				Long specId = Long.parseLong(String.valueOf(map.get("id")));
				System.out.println("specid"+specId);
				//6  将规格选项  再封装道规格选项中 一起返回
				SpecificationOptionQuery query = new SpecificationOptionQuery();
				SpecificationOptionQuery.Criteria criteria = query.createCriteria();
				criteria.andSpecIdEqualTo(specId);
				// 根据规格id  获得规格选项数据
				List<SpecificationOption> optionList = specificationOptionMapper.selectByExample(query);
				// 将规格选项集合封装到原来的map 中
				map.put("options",optionList);
				System.out.println("optionList："+optionList);
			}
		}
		return maps;
	}

	/**
	 * 缓存品牌和规格列表数据
	 */
	private void saveToRedis(){
		List<TypeTemplate> templateList =typeTemplateMapper.selectByExample(null);
		for (TypeTemplate typeTemplate : templateList) {
			//JSON.parseArray():把json类型数据转换成数组格式
			List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
			redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(),brandList);
			//存储规格列表
			List<Map> specList = findSpecList(typeTemplate.getId());
			redisTemplate.boundHashOps("specList").put(typeTemplate.getId(),specList);

		}
	}

}
