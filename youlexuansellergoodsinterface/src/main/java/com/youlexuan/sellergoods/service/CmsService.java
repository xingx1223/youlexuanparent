package com.youlexuan.sellergoods.service;

import java.util.Map;

public interface CmsService {
    Map<String,Object> findGoodsData(Long goodsId);
    void createStaticPage(Long goodsId,Map<String,Object> rootMap) throws Exception;
}
