package com.youlexuan.sellergoods.service;

public interface SolrManagerService {
    public void saveItemToSolr(Long id);
    public void deleteItemFromSolr(Long ids);
}
