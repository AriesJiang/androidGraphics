package com.aries.graphics.pixelate;

import android.graphics.Bitmap;

/**
 * Created by JiangYiDong on 2018/6/19.
 */

public interface OnPixelateListener {
    void onPixelated(final Bitmap bitmap, int density);
}
