package com.aries.androidpixelate;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aries.androidpixelate.palette.PaletteActivity;
import com.aries.androidpixelate.pixelate.OnPixelateListener;
import com.aries.androidpixelate.pixelate.Pixelate;
import com.aries.androidpixelate.pixelate.PixelateActivity;
import com.aries.androidpixelate.pixelate.TimeUtils;

import java.io.IOException;

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
        }
    }
}
