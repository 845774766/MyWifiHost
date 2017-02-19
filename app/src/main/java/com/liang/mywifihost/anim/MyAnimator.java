package com.liang.mywifihost.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by 广靓 on 2017/2/11.
 */

public class MyAnimator {

    /**
     * 翻转效果 360度
     * @param view
     */
    public void rotatey360X_AnimRun(View view)
    {
        ObjectAnimator//
                .ofFloat(view, "rotationX", 0.0F, 360.0F)//
                .setDuration(500)//
                .start();
    }

    /**
     * 翻转效果 540度
     * @param view
     */
    public void rotatey540X_AnimRun(View view){
        ObjectAnimator
                .ofFloat(view,"rotationX",0.0F,540.0F)
                .setDuration(500)
                .start();
    }

    /**
     * 旋转效果 360度
     * @param view
     */
    public void rotate720AnimRun(View view){
        ObjectAnimator//
                .ofFloat(view, "rotation", 0.0F, 720.0F)//
                .setDuration(1500)//
                .start();
    }
}
