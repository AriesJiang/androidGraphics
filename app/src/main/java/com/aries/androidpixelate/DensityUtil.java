package com.aries.androidpixelate;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

/**
 * Created by JiangYiDong on 2018/6/19.
 * 尺寸帮助类
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取实际尺寸的宽度
     *
     * @param context
     * @param uiWidth    设计图的宽度
     * @param uiViewSize 设计图View的宽度
     * @return
     */
    public static float getDisplayWidth(Context context, float uiWidth, float uiViewSize) {
        float iconScale = uiViewSize / uiWidth; // 控件原图的宽高比例
        float displayIconSize = (int) (getScreenWidth(context) * iconScale);
        return displayIconSize;
    }

    /**
     * 获取实际尺寸的宽度
     *
     * @param context
     * @param uiHeight   设计图的高度
     * @param uiViewSize 设计图View的高度
     * @return
     */
    public static float getDisplayHeight(Context context, float uiHeight, float uiViewSize) {
        float iconScale = uiViewSize / uiHeight; // 控件原图的宽高比例
        float displayIconSize = (int) (getScreenHeight(context) * iconScale);
        return displayIconSize;
    }

    /**
     * 判断屏幕的dpi是否是高dpi
     */
    public static boolean isHighDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi > 320;
    }

    public static int getXmlDef(Context context, int id) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(id, value, true);
        return (int) TypedValue.complexToFloat(value.data);
    }

    public static int getXmlToPx(Context context, int id) {
        return dip2px(context, getXmlDef(context, id));
    }

    public static boolean isNavigationBarShow(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        Point realSize = new Point();
        display.getSize(size);
        display.getRealSize(realSize);
        return realSize.y != size.y;

    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

}
