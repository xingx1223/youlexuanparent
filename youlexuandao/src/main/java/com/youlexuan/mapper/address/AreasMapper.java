package com.youlexuan.mapper.address;

import com.youlexuan.pojo.address.Areas;
import com.youlexuan.pojo.address.AreasQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AreasMapper {
    int countByExample(AreasQuery example);

    int deleteByExample(AreasQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Areas record);

    int insertSelective(Areas record);

    List<Areas> selectByExample(AreasQuery example);

    Areas selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Areas record, @Param("example") AreasQuery example);

    int updateByExample(@Param("record") Areas record, @Param("example") AreasQuery example);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);
}