package com.youlexuan.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youlexuan.entity.PageResult;
import com.youlexuan.entity.Result;
import com.youlexuan.mapper.ad.ContentMapper;
import com.youlexuan.pojo.ad.Content;
import com.youlexuan.pojo.ad.ContentCategory;
import com.youlexuan.pojo.ad.ContentQuery;
import com.youlexuan.sellergoods.service.ContentService;
import com.youlexuan.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 分页+查询数据+带查询条件
     * @param content
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult search(Content content, int page, int rows) {
        PageHelper.startPage(page,rows);

        ContentQuery query=new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        if (content !=null){
            if (content.getTitle()!=null && content.getTitle().length()>0){
                criteria.andTitleLike("%"+content.getTitle()+"%");
            }
        }

        Page<Content> pages=(Page<Content>)contentMapper.selectByExample(query);

        PageResult pageResult = new PageResult(pages.getTotal(), pages.getResult());
        return pageResult;
    }

    /**
     * 删除广告
     * 在redis中只要删除就清空redis,清空广告
     * @param ids
     */
    @Override
    public void delete(long[] ids) {
        if (ids !=null){
            for (long id : ids) {

                //得到广告分类id
                Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
                //清除缓存
                redisTemplate.boundHashOps("content").delete(categoryId);

                contentMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 添加广告： 1.到数据库  2.清空redis缓存中
     * @param content
     * @return
     */
    @Override
    public Result add(Content content){
        try {
            contentMapper.insert(content);

            //清空缓存
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());

            return new Result(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    /**
     * 查询单个实体
     * @param id
     * @return
     */
    @Override
    public Content findOne(long id){
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * @param content
     * @return
     */
    @Override
    public Result update(Content content){
        try {
            //查询修改前的分类id
            Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
            //清空缓存
            redisTemplate.boundHashOps(Constants.Constant_LIST_REDIS).delete(categoryId);

            contentMapper.updateByPrimaryKeySelective(content);

            //如果分类id发生了修改，清除修改后的分类id的缓存
            if (categoryId.longValue() !=content.getCategoryId().longValue()){
                redisTemplate.boundHashOps("content").delete(content.getCategoryId());
            }

            return new Result(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {

        List<Content> contentList = (List<Content>)redisTemplate.boundHashOps("content").get(categoryId);
        if (contentList ==null){
            System.out.println("从数据库读取放入缓存中");
            //根据广告分类id查询广告列表
            ContentQuery query=new ContentQuery();
            ContentQuery.Criteria criteria = query.createCriteria();

            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andStatusEqualTo("1");//开启状态
            query.setOrderByClause("sort_order");//排序

            contentList = contentMapper.selectByExample(query);

            //存入缓存
            redisTemplate.boundHashOps("content").put(categoryId,contentList);

        }
        return contentList;
    }


}
