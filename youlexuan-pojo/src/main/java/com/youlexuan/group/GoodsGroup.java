package com.youlexuan.group;

import com.youlexuan.pojo.good.Goods;
import com.youlexuan.pojo.good.GoodsDesc;
import com.youlexuan.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsGroup implements Serializable {
    private Goods goods;//商品SPU:SPU = Standard Product Unit （标准产品单位）standard product unit
    // SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。属性值、特性相同的商品就可以称为一个SPU例如：
    //iphone7就是一个SPU，与商家，与颜色、款式、套餐都无关。
    private GoodsDesc goodsDesc;//商品扩展
    private List<Item> itemList;//stock  keeping unit:(库存量单位):例如：
    //纺织品中一个SKU通常表示：规格、颜色、款式。

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
