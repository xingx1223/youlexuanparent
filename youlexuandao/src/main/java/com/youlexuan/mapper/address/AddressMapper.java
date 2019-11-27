package com.youlexuan.mapper.address;

import com.youlexuan.pojo.address.Address;
import com.youlexuan.pojo.address.AddressQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressMapper {
    int countByExample(AddressQuery example);

    int deleteByExample(AddressQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    List<Address> selectByExample(AddressQuery example);

    Address selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Address record, @Param("example") AddressQuery example);

    int updateByExample(@Param("record") Address record, @Param("example") AddressQuery example);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}