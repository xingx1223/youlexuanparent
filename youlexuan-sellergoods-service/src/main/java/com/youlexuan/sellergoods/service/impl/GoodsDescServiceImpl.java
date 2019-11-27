package com.youlexuan.sellergoods.service.impl;
import java.util.List;

import com.youlexuan.mapper.good.GoodsDescMapper;
import com.youlexuan.pojo.good.GoodsDesc;
import com.youlexuan.pojo.good.GoodsDescQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.GoodsDescService;

import com.youlexuan.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsDescServiceImpl implements GoodsDescService {

	@Autowired
	private GoodsDescMapper goodsDescMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<GoodsDesc> findAll() {
		return goodsDescMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<GoodsDesc> page=   (Page<GoodsDesc>) goodsDescMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(GoodsDesc goodsDesc) {
		goodsDescMapper.insert(goodsDesc);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(GoodsDesc goodsDesc){
		goodsDescMapper.updateByPrimaryKey(goodsDesc);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param
	 * @return
	 */
	@Override
	public GoodsDesc findOne(Long goodsId){
		return goodsDescMapper.selectByPrimaryKey(goodsId);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] goodsIds) {
		for(Long goodsId:goodsIds){
			goodsDescMapper.deleteByPrimaryKey(goodsId);
		}		
	}
	
	
	@Override
	public PageResult findPage(GoodsDesc goodsDesc, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
			GoodsDescQuery query=new GoodsDescQuery();
		//GoodsDescExample example=new GoodsDescExample();
			GoodsDescQuery.Criteria criteria = query.createCriteria();

			if(goodsDesc!=null){
						if(goodsDesc.getIntroduction()!=null && goodsDesc.getIntroduction().length()>0){
				criteria.andIntroductionLike("%"+goodsDesc.getIntroduction()+"%");
			}			if(goodsDesc.getSpecificationItems()!=null && goodsDesc.getSpecificationItems().length()>0){
				criteria.andSpecificationItemsLike("%"+goodsDesc.getSpecificationItems()+"%");
			}			if(goodsDesc.getCustomAttributeItems()!=null && goodsDesc.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+goodsDesc.getCustomAttributeItems()+"%");
			}			if(goodsDesc.getItemImages()!=null && goodsDesc.getItemImages().length()>0){
				criteria.andItemImagesLike("%"+goodsDesc.getItemImages()+"%");
			}			if(goodsDesc.getPackageList()!=null && goodsDesc.getPackageList().length()>0){
				criteria.andPackageListLike("%"+goodsDesc.getPackageList()+"%");
			}			if(goodsDesc.getSaleService()!=null && goodsDesc.getSaleService().length()>0){
				criteria.andSaleServiceLike("%"+goodsDesc.getSaleService()+"%");
			}	
		}
		
		Page<GoodsDesc> page= (Page<GoodsDesc>)goodsDescMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
