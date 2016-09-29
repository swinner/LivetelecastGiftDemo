package com.akl.livetelecastgiftdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * @author:gtf
 * @date: 2016/5/20 16:01
 * @company:yj
 * @description:
 * @version:1.0
 */
public class CustomGridViewAdapter extends BaseAdapter {

    private List<GiftListLayoutBean> gift;
    private int index;
    private Context ctx;
    public CustomGridViewAdapter(Context ctx, List<GiftListLayoutBean> gift, int index){
        this.ctx = ctx;
        this.gift = gift;
        this.index = index;
    }
    public void setIndex(int index){
        this.index = index;
    }
    @Override
    public int getCount() {
        if(gift == null){
            return 0;
        }else{
            if(gift.size()>8){
                return 8;
            }else{
                return gift.size();
            }
        }
    }
    @Override
    public Object getItem(int position) {
        return gift.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e("CustomGridViewAdapter", "getView====>" + position + "index===>" + index);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_grideview, null);
            holder.iv_image = (ImageView)convertView.findViewById(R.id.iv_image);
            holder.iv_gift_select_state = (ImageView)convertView.findViewById(R.id.iv_gift_select_state);
            holder.tv_gift_name = (TextView)convertView.findViewById(R.id.tv_gift_name);
            holder.tv_gift_price = (TextView)convertView.findViewById(R.id.tv_gift_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        GiftListLayoutBean giftBean = gift.get(position);
        holder.tv_gift_name.setText(giftBean.giftName);
        holder.tv_gift_price.setText(Math.round(Double.parseDouble(giftBean.giftPrice)) + "");
        Glide.with(ctx)
                .load(giftBean.giftIcon)
                .placeholder(R.mipmap.icon_giftimage_default) //设置占位图
                .error(R.mipmap.icon_giftimage_default) //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_image);

        String giftType = giftBean.giftType;
        if("2".equals(giftType)){
            holder.iv_gift_select_state.setBackgroundResource(R.drawable.bg_effect_gift_send_select);
        }else{
            holder.iv_gift_select_state.setBackgroundResource(R.drawable.bg_effect_gift_single_send_select);
        }
        if(index == position){
            holder.iv_gift_select_state.setSelected(true);
        }else{
            holder.iv_gift_select_state.setSelected(false);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView iv_image;
        ImageView iv_gift_select_state;
        TextView tv_gift_name;
        TextView tv_gift_price;
    }
}
