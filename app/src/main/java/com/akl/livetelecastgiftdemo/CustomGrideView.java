package com.akl.livetelecastgiftdemo;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:高腾飞
 * @date: 2016/5/20 15:49
 * @description:
 * @version:1.0
 */
public class CustomGrideView implements AdapterView.OnItemClickListener {

    private String TAG = CustomGrideView.class.getSimpleName();
    private List<ServerGiftBean.GiftListData> gift;
    private  int type = 0;
    private Context ctx;
    private GridView layout_grideaa;
    private CustomGridViewAdapter grideViewAdapter;
    public CustomGrideView(Context ctx, List<ServerGiftBean.GiftListData>  gift, int type){
        this.ctx = ctx;
        this.gift = gift;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public View getViews(){
        View view = View.inflate(ctx, R.layout.layout_custom_grideview, null);
        layout_grideaa =  (GridView)view.findViewById(R.id.gv_grideviews);
        List<GiftListLayoutBean> newGift = getNewGiftList(type);
        grideViewAdapter = new CustomGridViewAdapter(ctx,newGift,-1);
        layout_grideaa.setAdapter(grideViewAdapter);
        layout_grideaa.setOnItemClickListener(this);
        return view;
    }

    private List<GiftListLayoutBean> newGiftList = new ArrayList<GiftListLayoutBean>();
    /*
    * 读取每页礼物
    * */
    private List<GiftListLayoutBean> getNewGiftList(int type){
        int maxLength=0;
        if(type*8+8>gift.size()){
            maxLength= gift.size();
        }else {
            maxLength=type*8+8;
        }
        for(int i=type*8;i<maxLength;i++){
            newGiftList.add(new GiftListLayoutBean(gift.get(i).id,gift.get(i).cateId,gift.get(i).name,
                    gift.get(i).price,gift.get(i).appIoc));
        }
        return newGiftList;
    }

    private int selectId = -1;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(selectId != position){
            GiftListLayoutBean giftBean = (GiftListLayoutBean)parent.getItemAtPosition(position);
            onGiftSelectCallBack.effectGiftId(giftBean.giftId,giftBean.giftType,giftBean.giftName,type);
            selectId = position;
            grideViewAdapter.setIndex(selectId);
            grideViewAdapter.notifyDataSetChanged();
        }else{
            onGiftSelectCallBack.effectGiftId("","","",type);
            selectId = -1;
            grideViewAdapter.setIndex(selectId);
            grideViewAdapter.notifyDataSetChanged();
        }
    }

    public void clearAdapter(){
        selectId = -1;
        grideViewAdapter.setIndex(selectId);
        grideViewAdapter.notifyDataSetChanged();
    }

    public interface EffectGiftCallBack{
        void effectGiftId(String giftId, String giftType, String giftName, int type);
    }
    public EffectGiftCallBack onGiftSelectCallBack;
    public void setOnGiftSelectCallBack(EffectGiftCallBack onGiftSelectCallBack) {
        this.onGiftSelectCallBack = onGiftSelectCallBack;
    }
}
