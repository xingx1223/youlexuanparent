package com.youlexuan.mapper.template;

import com.youlexuan.pojo.template.TypeTemplate;
import com.youlexuan.pojo.template.TypeTemplateQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TypeTemplateMapper {
    int countByExample(TypeTemplateQuery example);

    int deleteByExample(TypeTemplateQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(TypeTemplate record);

    int insertSelective(TypeTemplate record);

    List<TypeTemplate> selectByExample(TypeTemplateQuery example);

    TypeTemplate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TypeTemplate record, @Param("example") TypeTemplateQuery example);

    int updateByExample(@Param("record") TypeTemplate record, @Param("example") TypeTemplateQuery example);

    int updateByPrimaryKeySelective(TypeTemplate record);

    int updateByPrimaryKey(TypeTemplate record);
}