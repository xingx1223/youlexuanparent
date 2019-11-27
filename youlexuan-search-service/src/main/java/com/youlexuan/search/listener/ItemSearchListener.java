package com.youlexuan.search.listener;

import com.youlexuan.sellergoods.service.SolrManagerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;

public class ItemSearchListener implements MessageListener {

    @Autowired
    private SolrManagerService solrManagerService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
        try {
            String goodsId=activeMQTextMessage.getText();
            solrManagerService.saveItemToSolr(Long.valueOf(goodsId));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
