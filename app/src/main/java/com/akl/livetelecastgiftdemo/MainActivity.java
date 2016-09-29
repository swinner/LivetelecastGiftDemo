package com.akl.livetelecastgiftdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Button btn_gift;
    private MainActivity ctx;
    private ViewPager vp_gift_viewpager;
    private LinearLayout llPointGroup;
    private Button btn_continue_click;
    private RetrofitService retrofit;
    private MyCountDownTimer mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        btn_gift = (Button)findViewById(R.id.btn_gift);
        btn_gift.setOnClickListener(this);
        mc = new MyCountDownTimer(3100, 100);
        retrofit =  RetrofitControler.getService();
        Observable <ServerGiftBean> observable =retrofit.getGift("2.1.0", "ANDROID");
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ServerGiftBean>() {
                    @Override
                    public void onCompleted() {
                        // 处理完成
                        Log.e("TAG", "onCompleted==>");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 处理异常
                        Log.e("TAG", "onError==>" + e);
                    }

                    @Override
                    public void onNext(ServerGiftBean s) {
                        // 处理响应结果
                        Log.e("TAG", "onNext==>" + s.data.size());
                        giftList = s.data;
                    }
                });
    }
    List<ServerGiftBean.GiftListData> giftList;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gift:
                showGifViewWindow(giftList);
                break;
            case R.id.btn_effect_gift_send://送礼物按钮，特效
                if (TextUtils.isEmpty(mGiftId) || TextUtils.isEmpty(mGiftName)) {
                    return;
                }
                if ("2".equals(mGiftType)) {
                    btn_continue_click.setVisibility(View.VISIBLE);
                    mc.cancel();
                    mc.start();
                    btn_effect_gift_send.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_continue_click:
                mc.cancel();
                mc.start();
                Toast.makeText(ctx, "送"+mGiftName+"第"+(clickTimes)+"个", Toast.LENGTH_SHORT).show();
                clickTimes = clickTimes + 1;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mc.onFinish();
    }

    private void showGifViewWindow(List<ServerGiftBean.GiftListData> giftList) {
        if (effectGiftPopwindow == null) {
            initGiftPopwindow(giftList);
            if (giftList != null && giftList.size() != 0) {
                for (int i = 0; i < (giftList.size() - 1) / 8 + 1; i++) {
                    initGrideView(i);
                }
                if ((giftList.size() - 1) / 8 > 0) {
                    initDots((giftList.size() - 1) / 8 + 1);
                }
                GiftPageAdapter giftPageAdapter = new GiftPageAdapter();
                vp_gift_viewpager.setAdapter(giftPageAdapter);
            }
        }
        effectGiftPopwindow.showAtLocation(btn_gift, Gravity.BOTTOM, 0, 0);
        resetState(mGiftId, mGiftName);
    }
    private String mGiftId,mGiftType,mGiftName;
    private List<View> imageViewList = new ArrayList();
    private List<CustomGrideView> grideList = new ArrayList<CustomGrideView>();
    private void initGrideView(int i) {
        CustomGrideView mGifGrideView = new CustomGrideView(ctx, giftList, i);
        grideList.add(mGifGrideView);
        mGifGrideView.setOnGiftSelectCallBack(new CustomGrideView.EffectGiftCallBack() {
            @Override
            public void effectGiftId(String giftId, String giftType, String giftName, int type) {
                Log.e("effectGiftId", "giftId===>" + giftId + "giftType===>" + giftType + "giftName====>" + giftName);
                mGiftId = giftId;
                mGiftType = giftType;
                mGiftName = giftName;
                for (CustomGrideView mGride : grideList) {
                    if (mGride != null && mGride.getType() != type) {
                        mGride.clearAdapter();
                    }
                }
                resetState(giftId,giftName);
                mc.onFinish();
            }
        });
        imageViewList.add(mGifGrideView.getViews());
    }
    void resetState(String sendGiftId, String sendGiftName) {
        if (TextUtils.isEmpty(sendGiftId) || TextUtils.isEmpty(sendGiftId)) {//不可点击
            btn_effect_gift_send.setClickable(false);
            btn_effect_gift_send.setBackgroundResource(R.drawable.btn_selector_gray);
        } else {
            btn_effect_gift_send.setClickable(true);
            btn_effect_gift_send.setBackgroundResource(R.drawable.btn_selector);
        }
    }
    class GiftPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViewList.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View iv = imageViewList.get(position);
            // 1. 向ViewPager中添加一个view对象
            container.addView(iv);
            // 2. 返回当前添加的view对象
            return iv;
        }
    }

    private View view;
    private List<View> viewList = new ArrayList<>();
    private LinearLayout.LayoutParams params;
    private void initDots(int dots) {
        //根据图片的个数, 每循环一次向LinearLayout中添加一个点，此处的LayoutParams(20, 20)对应布局中的px，
        //并且设置两个点之间的宽度，设置没有选中的指示点的颜色
        for (int i = 0; i < dots; i++) {
            view = new View(this);
            params = new LinearLayout.LayoutParams(16, 16);
            if (i != 0) {
                params.leftMargin = 20;
                view.setBackgroundResource(R.mipmap.dot_gray);
            } else {
                view.setBackgroundResource(R.mipmap.dot_white);
            }
            view.setLayoutParams(params);
            llPointGroup.addView(view);
            viewList.add(view);
        }
    }
    /**
     * 当页面正在滚动时
     * position 当前选中的是哪个页面,让指示点跟着走
     * positionOffset 比例
     * positionOffsetPixels 偏移像素
     */
    int oldPosition = 0;

    @Override
    public void onPageSelected(int position) {
        viewList.get(position).setBackgroundResource(R.mipmap.dot_white);
        viewList.get(oldPosition).setBackgroundResource(R.mipmap.dot_gray);
        oldPosition = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageScrollStateChanged(int state) {}

    private View mSelectPointView;
    private Button btn_effect_gift_send;
    private ProgressBar gift_progress;
    private PopupWindow effectGiftPopwindow;
    void initGiftPopwindow(List<ServerGiftBean.GiftListData> giftList) {
        View popView = ((LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pop_protrait_effectgift, null);
        vp_gift_viewpager = (ViewPager) popView.findViewById(R.id.vp_gift_viewpager);
        vp_gift_viewpager.setOnPageChangeListener(this);
        llPointGroup = (LinearLayout) popView.findViewById(R.id.ll_guide_point_group);
        mSelectPointView = (View) popView.findViewById(R.id.select_point);

        btn_continue_click = (Button) popView.findViewById(R.id.btn_continue_click);
        btn_continue_click.setOnClickListener(this);
        btn_effect_gift_send = (Button) popView.findViewById(R.id.btn_effect_gift_send);
        btn_effect_gift_send.setOnClickListener(this);

        LinearLayout ll_gift_progress = (LinearLayout) popView.findViewById(R.id.ll_gift_progress);
        gift_progress = (ProgressBar) popView.findViewById(R.id.gift_progress);
        if(giftList==null || giftList.size() == 0){
            ll_gift_progress.setVisibility(View.VISIBLE);
            btn_effect_gift_send.setVisibility(View.GONE);
            if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                final Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.frame_loading2_v6);
                gift_progress.setIndeterminateDrawable(drawable);
            }
        }
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = getResources().getDimensionPixelOffset(R.dimen.pop_gift_height);
        effectGiftPopwindow = PopwindowUtils.getPopWindow(ctx, popView, width, height, R.color.half_transparent);
        //popWindow消失监听方法
        effectGiftPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //改变图标状态
                mc.onFinish();
            }
        });
    }
    private int clickTimes = 2;
    private Boolean isContinteFinish = false;
    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            clickTimes = 2;
            if (isContinteFinish) {
                isContinteFinish = false;
                if (btn_continue_click != null && btn_effect_gift_send != null) {
                    btn_continue_click.setVisibility(View.GONE);
                    btn_effect_gift_send.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            isContinteFinish = true;
            if (btn_continue_click != null) {
                btn_continue_click.setText("连发" + "\n" + millisUntilFinished / 100);
            }
        }
    }
}
