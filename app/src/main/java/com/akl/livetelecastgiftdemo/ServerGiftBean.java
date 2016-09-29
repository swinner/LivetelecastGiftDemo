package com.akl.livetelecastgiftdemo;

import java.util.List;

/**
 * @author:gtf
 * @date: 2016/6/2 15:34
 * @company:yj
 * @description:后台返回的数据bean
 * @version:1.0
 */
public class ServerGiftBean {
    public List<GiftListData> data;
    public class GiftListData{
        public String id;//礼物id
        public String  amount;//连发礼物数量
        public String  appIoc;//app图片.用到的图片
        public String  name;//礼物名称，String类型
        public String  price;//礼物价格
        public String cateId;//礼物分类
        public String cateName;//礼物分类名称
        public String  effect;//礼物特效
        public String  appEffectsPic;//礼物特效url

        public Boolean  isDisplay;//是否展示，boolean类型
        public int  order;//排序。
    }
}
