package com.youlexuan.page.listener;

import com.youlexuan.sellergoods.service.CmsService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;
//@Component
public class PageListener implements MessageListener {

    @Autowired
    private CmsService cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId=atm.getText();
            Map<String, Object> goodsData = cmsService.findGoodsData(Long.parseLong(goodsId));
            //生成静态详情页面
            cmsService.createStaticPage(Long.parseLong(goodsId),goodsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
