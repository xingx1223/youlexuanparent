package com.youlexuan.mapper.seckill;

import com.youlexuan.pojo.seckill.SeckillGoods;
import com.youlexuan.pojo.seckill.SeckillGoodsQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SeckillGoodsMapper {
    int countByExample(SeckillGoodsQuery example);

    int deleteByExample(SeckillGoodsQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(SeckillGoods record);

    int insertSelective(SeckillGoods record);

    List<SeckillGoods> selectByExample(SeckillGoodsQuery example);

    SeckillGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SeckillGoods record, @Param("example") SeckillGoodsQuery example);

    int updateByExample(@Param("record") SeckillGoods record, @Param("example") SeckillGoodsQuery example);

    int updateByPrimaryKeySelective(SeckillGoods record);

    int updateByPrimaryKey(SeckillGoods record);
}