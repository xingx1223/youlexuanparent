package com.youlexuan.sellergoods.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youlexuan.group.GoodsGroup;
import com.youlexuan.mapper.good.BrandMapper;
import com.youlexuan.mapper.good.GoodsDescMapper;
import com.youlexuan.mapper.good.GoodsMapper;
import com.youlexuan.mapper.item.ItemCatMapper;
import com.youlexuan.mapper.item.ItemMapper;
import com.youlexuan.mapper.seller.SellerMapper;
import com.youlexuan.pojo.good.Brand;
import com.youlexuan.pojo.good.Goods;
import com.youlexuan.pojo.good.GoodsDesc;
import com.youlexuan.pojo.good.GoodsQuery;
import com.youlexuan.pojo.item.Item;
import com.youlexuan.pojo.item.ItemCat;
import com.youlexuan.pojo.item.ItemQuery;
import com.youlexuan.pojo.seller.Seller;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.sellergoods.service.GoodsService;

import com.youlexuan.entity.PageResult;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	//商品
	@Autowired
	private GoodsMapper goodsMapper;
	//详情
	@Autowired
	private GoodsDescMapper goodsDescMapper;
	//库存
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private BrandMapper brandMapper;
	//类别
	@Autowired
	private ItemCatMapper itemCatMapper;
	//商家
	@Autowired
	private SellerMapper sellerMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	//商品上架使用
	@Autowired
	private ActiveMQTopic topicPageAndSolrDestination;
	//为商品的下架使用
	@Autowired
	private ActiveMQQueue queueSolrDeleteDestination;
	/*@Autowired
	private SolrTemplate solrTemplate;*/
	//private Destination
	/**
	 * 查询全部
	 */
	@Override
	public List<Goods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<Goods> page=(Page<Goods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(GoodsGroup goodsGroup) {
		goodsGroup.getGoods().setAuditStatus("0");//设置未申请状态
		//添加商品
		goodsMapper.insert(goodsGroup.getGoods());//插入商品表

		goodsGroup.getGoodsDesc().setGoodsId(goodsGroup.getGoods().getId());//设置ID
		goodsDescMapper.insertSelective(goodsGroup.getGoodsDesc());//添加商品扩展数据

		//3.保存库存集合对象
		insertItem(goodsGroup);//插入商品SKU列表数据
	}

	private void insertItem(GoodsGroup goodsGroup) {
		//启用规格
		if ("1".equals(goodsGroup.getGoods().getIsEnableSpec())){
			//勾选复选框 有库存数据
			if (goodsGroup.getItemList() != null){
				//库存对象
				for (Item item : goodsGroup.getItemList()) {
					//标题由商品名+规格组成  供消费者搜索使用
					String title = goodsGroup.getGoods().getGoodsName();
					String specJsonStr = item.getSpec();
					//将json 转成对象
					Map specMap = JSON.parseObject(specJsonStr,Map.class);
					//获取specMap中的value集合
					Collection<String> values = specMap.values();
					for (String value : values) {
						title+=" "+value;
					}
					item.setTitle(title);
					//  设置库存的对象的属性值
					setItemValus(goodsGroup,item);
					itemMapper.insertSelective(item);
				}
			}
		}else {
			//不启用规格
			//没有勾选  没有库存  但是初始化一条
			Item item = new Item();
			item.setPrice(goodsGroup.getGoods().getPrice());
			item.setStatus("1");// 状态
			item.setIsDefault("1");// 是否默认
			//库存量
			item.setNum(0);
			//初始化规格
			item.setSpec("{}");
			//标题
			item.setTitle(goodsGroup.getGoods().getGoodsName());
			//设置库存对象的属性值
			setItemValus(goodsGroup,item);
			itemMapper.insertSelective(item);

		}
	}

	private void setItemValus(GoodsGroup goodsGroup, Item item) {
		item.setGoodsId(goodsGroup.getGoods().getId());//商品spu编号
		item.setSellerId(goodsGroup.getGoods().getSellerId());//商家编号
		item.setCategoryid(goodsGroup.getGoods().getCategory3Id());//商品分类编号
		item.setCreateTime(new Date());
		item.setUpdateTime(new Date());

		//品牌名称
		Brand brand = brandMapper.selectByPrimaryKey(goodsGroup.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//分类名称
		ItemCat itemCat = itemCatMapper.selectByPrimaryKey(goodsGroup.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		//商家名称
		Seller seller = sellerMapper.selectByPrimaryKey(goodsGroup.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		//图片地址（取spu的第一个图片）
		List<Map> imageList = JSON.parseArray(goodsGroup.getGoodsDesc().getItemImages(), Map.class);
		if (imageList.size() > 0) {
			item.setImage((String) imageList.get(0).get("url"));
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(GoodsGroup goodsGroup){
		//设置未申请状态:如果是经过修改的商品，需要重新设置状态
		goodsGroup.getGoods().setAuditStatus("0");
		//修改商品表
		goodsMapper.updateByPrimaryKey(goodsGroup.getGoods());
		//修改商品详情表
		goodsDescMapper.updateByPrimaryKey(goodsGroup.getGoodsDesc());
		//删除原有的sku列表数据
		ItemQuery itemQuery=new ItemQuery();
		ItemQuery.Criteria criteria = itemQuery.createCriteria();
		criteria.andGoodsIdEqualTo(goodsGroup.getGoods().getId());
		itemMapper.deleteByExample(itemQuery);
		//添加新的sku的列表数据
		insertItem(goodsGroup);
	}


	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public GoodsGroup findOne(Long id){
		GoodsGroup goodsGroup=new GoodsGroup();
		Goods goods = goodsMapper.selectByPrimaryKey(id);
		goodsGroup.setGoods(goods);

		GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goodsGroup.setGoodsDesc(goodsDesc);

		//查询SKU商品列表
		ItemQuery query=new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andGoodsIdEqualTo(id);//查询条件，商品
		List<Item> itemList = itemMapper.selectByExample(query);
		goodsGroup.setItemList(itemList);

		return goodsGroup;
		//return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(final Long[] ids) {
		for(final Long id:ids){
			Goods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);

			//2. 将商品的id作为消息发送给消息服务器
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage=session.createTextMessage(String.valueOf(id));
					return textMessage;
				}
			});
		}		
	}
	
	
	@Override
	public PageResult findPage(Goods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
        GoodsQuery query=new GoodsQuery();
        GoodsQuery.Criteria criteria = query.createCriteria();

        if(goods!=null){
        	//该用户的商品
            if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
                /*criteria.andSellerIdLike("%"+goods.getSellerId()+"%");*/
				criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            //商品名称模糊查询
            if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
                criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            //状态
            if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
                criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
            }

            /*if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
                criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
            }
            if(goods.getCaption()!=null && goods.getCaption().length()>0){
                criteria.andCaptionLike("%"+goods.getCaption()+"%");
            }
            if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
                criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
            }
            if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
                criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
            }*/
            if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
                /*criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");*/
				criteria.andIsDeleteEqualTo("0");
            }
        }
		Page<Goods> page= (Page<Goods>)goodsMapper.selectByExample(query);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(final Long[] ids, String status) {

		//按照SPU ID查询SKU列表（状态为1）
		/*if (status.equals("1")){
			List<Item> itemList = findItemListByGoodsIdandStatus(ids, status);
			//调用方法实现数据批量导入
			if (itemList.size()>0) {
				importList(itemList);
			}else {
				System.out.println("没有明细数据");
			}
		}*/
		for (final Long id : ids) {
			// 1 根据商品的id  修改商品的状态码
			Goods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKeySelective(goods);
			// 2 根据商品id   修改库存对象的状态码
			Item item=new Item();
			item.setStatus(status);
			ItemQuery query=new ItemQuery();
			ItemQuery.Criteria criteria = query.createCriteria();
			criteria.andGoodsIdEqualTo(id);
			itemMapper.updateByExampleSelective(item,query);
			// 将商品的id 作为消息发送给消息服务器
			if ("1".equals(status)){
				jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						TextMessage textMessage=session.createTextMessage(String.valueOf(id));
						return textMessage;
						//接受方有两个 一个是search    一个是page
					}
				});
			}
		}
	}

	@Override
	public List<Item> findItemListByGoodsIdandStatus(Long[] goodsIds, String status) {
		ItemQuery query=new ItemQuery();
		ItemQuery.Criteria criteria = query.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andGoodsIdIn(Arrays.asList(goodsIds));
		return itemMapper.selectByExample(query);
	}

	private void importList(List<Item> list){
		for (Item item : list) {
			//从数据库中提取规格json字符串转换为map
			Map specMap = JSON.parseObject(item.getSpec(), Map.class);
			item.setSpecMap(specMap);
		}
		/*solrTemplate.saveBeans(list);
		solrTemplate.commit();*/
	}

}
