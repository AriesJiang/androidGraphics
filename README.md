# androidPixelate
Android Demo on graphics processing:
1. Pixelate Images 
2. Palette Image (Get the key color of the image)

## Screenshotes:
![进度条调节马赛克大小](https://github.com/AriesJiang/androidPixelate/blob/master/image/微信图片_20180619200628.jpg)
![进度条调节颜色数量-颜色少](https://github.com/AriesJiang/androidPixelate/blob/master/image/S80805-16480638.jpg)
![进度条调节颜色数量-颜色多](https://github.com/AriesJiang/androidPixelate/blob/master/image/S80805-16514215.jpg)

## 实现
#### Pixelate Images
1. 选择手机图库图片，进行马赛克处理
2. 滑动进度条，调节马赛克大小

#### Palette Image
1. 获取图片bitmap
2. 使用中位切分法（Median cut）对颜色进行量化处理，得到预先设置的X个主题颜色
3. 将获取到的多个颜色回放的新创建的图像中，得出新的图片

## 技术点/Technical point
1. 马赛克处理/Pixelate

## 使用开源项目/Reference
1. 马赛克算法：https://github.com/DanielMartinus/Pixelate
2. Median-Cut Color Quantization： http://collaboration.cmc.ec.gc.ca/science/rpn/biblio/ddj/Website/articles/DDJ/1994/9409/9409e/9409e.htm
3. Android Palette base on com.android.support:palette-v7:26.1.0