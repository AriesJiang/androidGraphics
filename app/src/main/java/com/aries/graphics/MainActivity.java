package com.aries.graphics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aries.graphics.palette.PaletteActivity;
import com.aries.graphics.pixelate.PixelateActivity;

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
