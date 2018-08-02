package com.aries.graphics.palette;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.aries.graphics.DensityUtil;
import com.aries.graphics.FileUtil;
import com.aries.graphics.R;
import com.aries.graphics.pixelate.OnPixelateListener;
import com.aries.graphics.pixelate.TimeUtils;

import java.io.IOException;
import java.util.List;

public class PaletteActivity extends AppCompatActivity implements OnPixelateListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "PaletteActivity";
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的

    private Button selectBt;
    private TextView selectTv;
    private ImageView selectOriginalIv;
    private ImageView selectFinalIv;
    private SeekBar pixelSeekBar;
    private SeekBar paletteSeekBar;
    private TextView pixelTv;
    private TextView paletteTv;
    private ProgressBar progressBar;
    private AriesRecyclerView mAriesRecyclerView;
    private AriesRecyclerAdapter mAriesRecyclerAdapter;

    Bitmap originalBitmap = null;
    private int curProgressPixel = 112;
    private int curProgressPalette = 12;
    private int spanCount = 5;
    private String imageFormat = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        selectBt = findViewById(R.id.activity_pixelate_select_bt);
        selectTv = findViewById(R.id.activity_pixelate_select_tv);
        selectOriginalIv = findViewById(R.id.activity_pixelate_original_iv);
        selectFinalIv = findViewById(R.id.activity_pixelate_final_iv);
        pixelSeekBar = findViewById(R.id.activity_pixelate_progress);
        paletteSeekBar = findViewById(R.id.activity_palette_progress);
        pixelTv = findViewById(R.id.activity_pixelate_density_tv);
        paletteTv = findViewById(R.id.activity_palette_tv);
        progressBar = findViewById(R.id.activity_pixelate_progressBar);
        mAriesRecyclerView = findViewById(R.id.activity_pixelate_recycler);

        mAriesRecyclerView.setHasFixedSize(false);
        mAriesRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        int space = DensityUtil.dip2px(this, 2);
        DividerGridItemDecoration itemDecoration = new DividerGridItemDecoration.Builder()
                .drawLREdge(true)
                .drawTBEdge(false)
                .includeLREdge(false)
                .includeTBEdge(true)
                .spaceSize(space)
                .spanCount(spanCount)
                .build();
        mAriesRecyclerView.addItemDecoration(itemDecoration);
//        mAriesRecyclerView.addItemDecoration(new SpacesItemDecoration(ViewUtils.dipToPx(this, 2)));
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mAriesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAriesRecyclerView.closeDefaultAnimator();
        mAriesRecyclerAdapter = new AriesRecyclerAdapter(this);
        mAriesRecyclerView.setAdapter(mAriesRecyclerAdapter);

        selectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, IMAGE_CODE);
            }
        });

        pixelSeekBar.setOnSeekBarChangeListener(this);
        paletteSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();

        if (requestCode == IMAGE_CODE) {

            try {

                Uri originalUri = data.getData(); // 获得图片的uri
                if (originalUri != null) {
                    originalBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    // 显得到bitmap图片
                    selectOriginalIv.setImageBitmap(originalBitmap);

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
                    Log.e(TAG, "fileName=" + fileName);
//                    saveToSD(originalBitmap, fileName);
                } else {
                    //部分手机可能直接存放在bundle中
                    Bundle bundleExtras = data.getExtras();
                    if(bundleExtras != null){
                        Bitmap  bitmaps = bundleExtras.getParcelable("data");
                        selectOriginalIv.setImageBitmap(bitmaps);
                    }
                }


            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void saveToSD(Bitmap bitmap, String fileName) {
        //保存处理好的图片
        Log.e(TAG, "saveToSD fileName=" + fileName);
        FileUtil.saveBitmapToSDCardPrivateCacheDir(bitmap, fileName, this);
    }

    @Override
    public void onPixelated(Bitmap bitmap, int density) {
        //设置马赛克图片
        selectFinalIv.setImageBitmap(bitmap);

        //保存处理好的图片
        String fileName = TimeUtils.getSimpleDate() + "-" + String.valueOf(density) + imageFormat;
        Log.e(TAG, "onPixelated fileName=" + fileName);
        saveToSD(bitmap, fileName);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
//        Log.e(TAG, "onProgressChanged progress=" + progress);
        if (seekBar == pixelSeekBar) {
            curProgressPixel = progress;
            pixelTv.setText("Max Pixel:" + Integer.toString(seekBar.getMax()) + " cur:" + Integer.toString(curProgressPixel));
        } else if (seekBar == paletteSeekBar) {
            curProgressPalette = progress;
            paletteTv.setText("Max Colors:" + Integer.toString(curProgressPalette));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "开始滑动！");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "停止滑动！pixel:" + Integer.toString(curProgressPixel) + " Max Colors:" + Integer.toString(curProgressPalette));
        Log.e(TAG, "Max Pixel:" + Integer.toString(pixelSeekBar.getMax()) + " cur:" + Integer.toString(curProgressPixel));

        progressBar.setVisibility(View.VISIBLE);
        final long startTime = System.currentTimeMillis();
        Palette.Builder builder = new Palette.Builder(originalBitmap);
        builder.maximumColorCount(curProgressPalette);
        builder.resizeBitmapWHSize(curProgressPixel);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                List<Palette.Swatch> swatchList = palette.getSwatches();
                if (originalBitmap == null || swatchList == null || swatchList.isEmpty()) {
                    selectFinalIv.setImageBitmap(originalBitmap);
                    return;
                }
                Log.d(TAG, "swatchList size=" + swatchList.size());
                int[] mColorsArray = new int[swatchList.size()];
                int[] mColorsPopulation = new int[swatchList.size()];
                int[] mTextColorsArray = new int[swatchList.size()];
                for (int index = 0; index < swatchList.size(); index++) {
                    Palette.Swatch swatch = swatchList.get(index);
                    mColorsArray[index] = swatch.getRgb();
                    mColorsPopulation[index] = swatch.getPopulation();
                    mTextColorsArray[index] = swatch.getTitleTextColor();
//                    Log.d(TAG, "swatch mPopulation=" + swatch.getPopulation());
                }

                paletteTv.setText("Max Colors :" + Integer.toString(curProgressPalette) + " Real Colors:" + Integer.toString(swatchList.size()));
                mAriesRecyclerAdapter.reFreshColor(mColorsArray, mColorsPopulation, mTextColorsArray);
                Bitmap resource = palette.getmBitmapScale();

                Log.e(TAG, "resource Width:" + resource.getWidth() + "  Height:" + resource.getHeight());
                int[] pixels = new int[(resource.getHeight() * resource.getWidth())];
                int[] pixelsSwatch = new int[(resource.getHeight() * resource.getWidth())];
                resource.getPixels(pixels, 0, resource.getWidth(), 0, 0, resource.getWidth(), resource.getHeight());
                int height = resource.getHeight();
                int width = resource.getWidth();
                for (int indexH = 0; indexH < height; indexH++) {
                    for (int indexW = 0; indexW < width; indexW++) {
                        int colorAlpha = pixels[(width * indexH) + indexW];
                        int color = pixels[(width * indexH) + indexW] & 0x00ffffff;

                        //兼容图片包涵透明像素的情况，这时候直接给回透明到图片中，也不画出来
                        if (Color.alpha(colorAlpha) == 0) {
//                            Log.d(TAG, "colorAlpha=" + Integer.toHexString(colorAlpha) + "  color=" + Integer.toHexString(color));
                            pixelsSwatch[(width * indexH) + indexW] = colorAlpha;
                            continue;
                        }
                        boolean isRange = false;
                        int index = 0;
//						for (; index < swatchList.size(); index++) {
//							if (swatchList.get(index).isColorInRange(color)) {
//								isRange = true;
//								break;
//							}
//						}
                        if (isRange) {
                            pixelsSwatch[(width * indexH) + indexW] = swatchList.get(index).getRgb();
                        } else {
//							Log.d(TAG, "onStartClick isRange failed!!!");
                            int r = Color.red(color);
                            int g = Color.green(color);
                            int b = Color.blue(color);

                            double tempOffset = Integer.MAX_VALUE;
                            Palette.Swatch tempColoRGB = swatchList.get(0);
                            for (Palette.Swatch colorRGB : swatchList) {
                                double offset = colorRGB.distanceOf(r, g, b);
                                if (offset < tempOffset) {
                                    tempOffset = offset;
                                    tempColoRGB = colorRGB;
                                }
                            }
                            pixelsSwatch[(width * indexH) + indexW] = tempColoRGB.getRgb();
                        }
                    }
                }
                Bitmap bitmapSwatch = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmapSwatch.setPixels(pixelsSwatch, 0, width, 0, 0, width, height);
                selectFinalIv.setImageBitmap(bitmapSwatch);
                Log.d(TAG, "Max Colors :" + Integer.toString(curProgressPalette)
                        + "  Real Colors:" + Integer.toString(swatchList.size())
                        + "  spent=" + (System.currentTimeMillis() - startTime) + "ms");
                paletteTv.setText("Max Colors :" + Integer.toString(curProgressPalette)
                        + "  Real Colors:" + Integer.toString(swatchList.size())
                        + "  spent=" + (System.currentTimeMillis() - startTime) + "ms");
                pixelTv.setText("Max Pixel:" + Integer.toString(pixelSeekBar.getMax())
                        + " cur:" + Integer.toString(curProgressPixel)
                        + " w:" + resource.getWidth()
                        + " h:" + resource.getHeight());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
