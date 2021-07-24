# 安卓多媒体基础

#### 图片

png, jpeg, jpg, gif

WebP

Nine Patch (.9图)：后缀：.9.png      方便拉伸



像素（RGBA）

ARGB_8888:4Byte

ARGB_4444:2B

RGB_565:5R,6G,5B= 2Byte + ALPHA_8 = 1B

#### 加载图片 Bitmap

#### 加载超大图片

防止出现OOM，在加载图片前，预判图片大小

设定缩放尺寸，像素配置



#### Drawable 可绘制对象资源：泛指可以在屏幕上绘制的图形

BitmapDrawable  .jpg/.png.webp

NinePathchDrawable .9.png

GradientDrawable

AnimationDrawable



ImageView.Scale.Type : 适配图片到IV上面



网络请求，磁盘存储，内存管理，别放在UI主线程，解码

变换

加载进度条、动画支持





#### 图片库：glide、fresco(低版本优化强)





#### 音频视频

非流：AVI

流媒体：MP4、FLV（B站）、MKV、RMVB



#### 安卓 MediaView

#### B站 ijkplayer

