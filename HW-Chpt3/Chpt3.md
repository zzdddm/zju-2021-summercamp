# 进阶UI



## Fragment：生命周期封装了的View视图

定义布局文件和Fragment类
动态添加：

- Fragment 容器：位置和大小
- Fragment Manager ：一组Fragment的事务处理
-  .add(R.id.fragment_container, new Fragment())

## ViewPager + Fragment
- 需要 Adapter
- 配置 TabLayout 或者第三方库添加 Title

## Fragment 与 Activity 之间通信
- setArguments / getArguments


## 动画！

### 属性动画：
- 可以连续变化的属性都可以变成动画
- 复杂动画是不同属性的组合
- view 属性动画 和 视图动画 ：view真的动了还是假的动了？视图动画用得少！
- 逐帧动画：按次序播放一系列图片，可能会内存爆炸
- 




