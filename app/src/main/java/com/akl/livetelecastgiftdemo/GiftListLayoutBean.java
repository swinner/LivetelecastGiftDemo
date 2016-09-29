package com.akl.livetelecastgiftdemo;

/**
 * @author:gtf
 * @date: 2016/5/20 19:16
 * @company:yj
 * @description: 布局中的礼物数据bean
 * @version:1.0
 */
public class GiftListLayoutBean {

    public String giftId;
    public String giftType;
    public String giftName;
    public String giftPrice;
    public String  giftIcon;//app图片.用到的图片

    public GiftListLayoutBean(String giftId, String giftType, String giftName, String giftPrice,String giftIcon) {
        this.giftId = giftId;
        this.giftType = giftType;
        this.giftName = giftName;
        this.giftPrice = giftPrice;
        this.giftIcon = giftIcon;
    }
}
