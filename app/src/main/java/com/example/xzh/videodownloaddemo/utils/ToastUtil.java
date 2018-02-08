package com.example.xzh.videodownloaddemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhenghangxia on 17-5-23.
 *
 *      Toast提示工具类
 */

public class ToastUtil {

    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void toast(Context context, String strings) {
        if (mToast == null) {
            mToast = Toast.makeText(context, strings, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(strings);
        }
        mToast.show();
    }

}
