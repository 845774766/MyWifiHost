package com.liang.mywifihost.custom;

/**
 * Created by Administrator on 2016/12/20.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 自定义popupWindow
 *
 * @author wwj
 *
 *
 */
public class CustomPopuwindow extends PopupWindow {
    private View conentView;
    private int i=2;

    public CustomPopuwindow(final Activity context , View conentView , int i_type) {
        this.i=i_type;   //设置显示位置及popu宽度

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        conentView = inflater.inflate(layoutid, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        if (i==1) {
            this.setWidth(w - 50);
        }else if(i==2){
            this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        }else {
            this.setWidth(w);
        }
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            switch (i){
                case 0:
                    this.showAsDropDown(parent);// 以下拉方式显示popupwindow
                    break;
                case 1:
                    this.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER,0,0); // 底部显示窗口
                    break;
                case 2:
                    this.showAsDropDown(parent);// 以下拉方式显示popupwindow
                    break;
                default:break;

            }
        } else {
            this.dismiss();
        }
    }

    public void popuDismiss(){
        this.dismiss();
    }
}