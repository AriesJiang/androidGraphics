package com.aries.graphics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aries.graphics.palette.PaletteActivity;
import com.aries.graphics.pixelate.PixelateActivity;

/**
 * Created by JiangYiDong on 2018/18/2.
 * 对图像进行处理
 * 联系方式 QQ:870184773
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.activity_pixelate:
                startActivity(new Intent(this, PixelateActivity.class));
                break;
            case R.id.activity_palette:
                startActivity(new Intent(this, PaletteActivity.class));
                break;
            case R.id.activity_leak_test:
                startActivity(new Intent(this, NormalThreadLeakActivity.class));
                break;
        }
    }
}
