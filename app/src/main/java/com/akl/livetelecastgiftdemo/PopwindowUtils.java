package com.akl.livetelecastgiftdemo;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author:gtf
 * @date: 2016/9/29 15:55
 * @company:yj
 * @description:
 * @version:1.0
 */
public class PopwindowUtils {
    public static PopupWindow getPopWindow(Context ctx, View targetView, int width, int height,int res) {
        PopupWindow popupWindow = new PopupWindow(targetView, width, height);
        popupWindow.setBackgroundDrawable(ctx.getResources().getDrawable(res));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.update();
        return popupWindow;
    }
}
