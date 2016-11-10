package com.collectioncar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.collectioncar.baseapp.BaseApplication;

/**
 * Created by Administrator on 2016/1/17.
 */
public class UIUtils {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim()) || "{}".equals(str)
                || "[]".equals(str) || "null".equals(str);
    }
    /**
     * 将xml转换成view对象
     *
     * @param resId
     * @return
     */
    public static View getXmlView(int resId) {
        return View.inflate(getContext(), resId, null);
    }

    /**
     * 1dp=1px;
     * 1dp=0.5px;
     * 1dp=0.75px;
     * 1dp=1.5px;
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }


    public static void Toast(String text, boolean isLong) {
        Toast.makeText(getContext(), text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    /**
     * 获取字符串数组
     *
     * @param arrId
     * @return
     */
    public static String[] getStringArr(int arrId) {
        return getContext().getResources().getStringArray(arrId);
    }


    /**
     * 获取颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }






    public static Context getContext() {
        return BaseApplication.getAppContext();
    }


    public static void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getContext(), clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public static String getString(int stringId) {
        return getContext().getResources().getString(stringId);
    }

    public static void stringIdToast(int stringId) {
        String str = getString(stringId);
        int tid = android.os.Process.myTid();
        if (tid == BaseApplication.mainThreadId) {
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        } else {
            Looper.prepare();
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}
