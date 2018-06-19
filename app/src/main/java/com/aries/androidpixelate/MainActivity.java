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

import com.aries.androidpixelate.pixelate.OnPixelateListener;
import com.aries.androidpixelate.pixelate.Pixelate;
import com.aries.androidpixelate.pixelate.TimeUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnPixelateListener, SeekBar.OnSeekBarChangeListener {

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的

    private Button selectBt;
    private TextView selectTv;
    private ImageView selectOriginalIv;
    private ImageView selectFinalIv;
    private SeekBar pixelSeekBar;
    private TextView pixelTv;

    private Bitmap thumbnail = null;
    private int curProgress = 12;
    private String imageFormat = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectBt = findViewById(R.id.activity_pixelate_select_bt);
        selectTv = findViewById(R.id.activity_pixelate_select_tv);
        selectOriginalIv = findViewById(R.id.activity_pixelate_original_iv);
        selectFinalIv = findViewById(R.id.activity_pixelate_final_iv);
        pixelSeekBar = findViewById(R.id.activity_pixelate_progress);
        pixelTv = findViewById(R.id.activity_pixelate_density_tv);

        selectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, IMAGE_CODE);
            }
        });

        pixelSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;

        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();

        if (requestCode == IMAGE_CODE) {

            try {

                Uri originalUri = data.getData(); // 获得图片的uri
                if (originalUri != null) {
                    bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                    thumbnail = ThumbnailUtils.extractThumbnail(bm, 300, 300);
                    selectOriginalIv.setImageBitmap(thumbnail);  //使用系统的一个工具类，参数列表为 Bitmap Width,Height  这里使用压缩后显示，否则在华为手机上ImageView 没有显示
                    // 显得到bitmap图片
                    // imageView.setImageBitmap(bm);

                    String[] proj = { MediaStore.Images.Media.DATA };

                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);

                    // 按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    selectTv.setText(path);

                    imageFormat = path.substring(path.lastIndexOf("."));
                    String fileName = TimeUtils.getSimpleDate() + imageFormat;
                    Log.e("MainActivity", "fileName=" + fileName);
                    saveToSD(thumbnail, fileName);

                    //pixel 马赛克
                    new Pixelate(thumbnail)
                            .setDensity(12)
                            .setListener(this)
                            .make();
                } else {
                    //部分手机可能直接存放在bundle中
                    Bundle bundleExtras = data.getExtras();
                    if(bundleExtras != null){
                        Bitmap  bitmaps = bundleExtras.getParcelable("data");
                        selectOriginalIv.setImageBitmap(bitmaps);
                    }
                }


            } catch (IOException e) {
                Log.e("MainActivity", e.toString());
            } finally {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void saveToSD(Bitmap bitmap, String fileName) {
        //保存处理好的图片
        Log.e("MainActivity", "saveToSD fileName=" + fileName);
        FileUtil.saveBitmapToSDCardPrivateCacheDir(bitmap, fileName, this);
    }

    @Override
    public void onPixelated(Bitmap bitmap, int density) {
        //设置马赛克图片
        selectFinalIv.setImageBitmap(bitmap);

        //保存处理好的图片
        String fileName = TimeUtils.getSimpleDate() + "-" + String.valueOf(density) + imageFormat;
        Log.e("MainActivity", "onPixelated fileName=" + fileName);
        saveToSD(bitmap, fileName);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
        Log.e("MainActivity", "onProgressChanged progress=" + progress);
        curProgress = progress;
        pixelTv.setText("Density:" + Integer.toString(curProgress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("MainActivity", "开始滑动！");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("MainActivity", "停止滑动！curProgress=" + curProgress);
        if (thumbnail != null) {
            //pixel 马赛克
            new Pixelate(thumbnail)
                    .setDensity(curProgress)
                    .setListener(this)
                    .make();
        }
    }
}
