package com.youlexuan.sellergoods.service.impl;
import java.util.Date;
import java.util.List;

import com.youlexuan.mapper.seller.SellerMapper;
import com.youlexuan.pojo.seller.Seller;
import com.youlexuan.pojo.seller.SellerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.SellerService;

import com.youlexuan.entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

	@Autowired
	private SellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<Seller> findAll() {
		return sellerMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<Seller> page=   (Page<Seller>) sellerMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Seller seller) {
		seller.setCreateTime(new Date());
		seller.setStatus("0");
		sellerMapper.insert(seller);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Seller seller){
		sellerMapper.updateByPrimaryKey(seller);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param sellerId
	 * @return
	 */
	@Override
	public Seller findOne(String sellerId){
		return sellerMapper.selectByPrimaryKey(sellerId);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(String[] sellerIds) {
		for(String sellerId:sellerIds){
			sellerMapper.deleteByPrimaryKey(sellerId);
		}		
	}
	
	
		@Override
	public PageResult findPage(Seller seller, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		SellerQuery query=new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();

        if(seller!=null){
            if(seller.getName()!=null && seller.getName().length()>0){
                criteria.andNameLike("%"+seller.getName()+"%");
            }			if(seller.getNickName()!=null && seller.getNickName().length()>0){
                criteria.andNickNameLike("%"+seller.getNickName()+"%");
            }			if(seller.getPassword()!=null && seller.getPassword().length()>0){
                criteria.andPasswordLike("%"+seller.getPassword()+"%");
            }			if(seller.getEmail()!=null && seller.getEmail().length()>0){
                criteria.andEmailLike("%"+seller.getEmail()+"%");
            }			if(seller.getMobile()!=null && seller.getMobile().length()>0){
                criteria.andMobileLike("%"+seller.getMobile()+"%");
            }			if(seller.getTelephone()!=null && seller.getTelephone().length()>0){
                criteria.andTelephoneLike("%"+seller.getTelephone()+"%");
            }			if(seller.getStatus()!=null && seller.getStatus().length()>0){
                criteria.andStatusLike("%"+seller.getStatus()+"%");
            }			if(seller.getAddressDetail()!=null && seller.getAddressDetail().length()>0){
                criteria.andAddressDetailLike("%"+seller.getAddressDetail()+"%");
            }			if(seller.getLinkmanName()!=null && seller.getLinkmanName().length()>0){
                criteria.andLinkmanNameLike("%"+seller.getLinkmanName()+"%");
            }			if(seller.getLinkmanQq()!=null && seller.getLinkmanQq().length()>0){
                criteria.andLinkmanQqLike("%"+seller.getLinkmanQq()+"%");
            }			if(seller.getLinkmanMobile()!=null && seller.getLinkmanMobile().length()>0){
                criteria.andLinkmanMobileLike("%"+seller.getLinkmanMobile()+"%");
            }			if(seller.getLinkmanEmail()!=null && seller.getLinkmanEmail().length()>0){
                criteria.andLinkmanEmailLike("%"+seller.getLinkmanEmail()+"%");
            }			if(seller.getLicenseNumber()!=null && seller.getLicenseNumber().length()>0){
                criteria.andLicenseNumberLike("%"+seller.getLicenseNumber()+"%");
            }			if(seller.getTaxNumber()!=null && seller.getTaxNumber().length()>0){
                criteria.andTaxNumberLike("%"+seller.getTaxNumber()+"%");
            }			if(seller.getOrgNumber()!=null && seller.getOrgNumber().length()>0){
                criteria.andOrgNumberLike("%"+seller.getOrgNumber()+"%");
            }			if(seller.getLogoPic()!=null && seller.getLogoPic().length()>0){
                criteria.andLogoPicLike("%"+seller.getLogoPic()+"%");
            }			if(seller.getBrief()!=null && seller.getBrief().length()>0){
                criteria.andBriefLike("%"+seller.getBrief()+"%");
            }			if(seller.getLegalPerson()!=null && seller.getLegalPerson().length()>0){
                criteria.andLegalPersonLike("%"+seller.getLegalPerson()+"%");
            }			if(seller.getLegalPersonCardId()!=null && seller.getLegalPersonCardId().length()>0){
                criteria.andLegalPersonCardIdLike("%"+seller.getLegalPersonCardId()+"%");
            }			if(seller.getBankUser()!=null && seller.getBankUser().length()>0){
                criteria.andBankUserLike("%"+seller.getBankUser()+"%");
            }			if(seller.getBankName()!=null && seller.getBankName().length()>0){
                criteria.andBankNameLike("%"+seller.getBankName()+"%");
            }
        }
		
		Page<Seller> page= (Page<Seller>)sellerMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 更改状态
	 * @param sellerId
	 * @param status
	 */
	@Override
	public void updateStatus(String sellerId, String status) {
		Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
		seller.setStatus(status);
		sellerMapper.updateByPrimaryKey(seller);
	}

}
