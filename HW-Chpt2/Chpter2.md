# A》》》

## 1.activity之间的跳转

显示intent和隐式intent

```java
Intent intent = new Intent（ packageContext∶ MainActivity.this，MyActiVity.cLass）; startActivity(intent);

```



## 2.activity生命周期

7个回调方法：

OnCreate  onStart onResume  onPause   onStop  onDestroy

onRestart



onCreate：创建

onStart：不可见到可见

onResume：进入前台可操作

onPause：移动到后台，可见到不可见

onStop：

onDestroy：关闭活动，销毁活动

onRestart：返回上一个活动

![image-20210713095130296](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210713095130296.png)

activity是以栈结构储存的。（很好理解呀，一直按返回键会回到上一个界面）



## 3.activity启动模式

简单的入栈模式：

```Java
android:launchMode = "standard"
//在activity控件中
```

standard

singTop：检查栈顶，是否和当前要创建的一样，如果一样就不会创建了（仅限栈顶）

singleTask:创建栈中已存在的页面就会将其弹出到栈顶

singleInstance：创建了新的返回栈，以免影响前一个栈的顺序



# B》》》UI

### 1.控件和布局

TextView	ImageView	Button	ProgressBar	ScollView	Toast

LinearLayout	RelativeLayout	FrameLayout

### 2.IN xml

layout_width:

layout_height:

wrap_content:自身内容一样长

match_parent:与父组件一样长



```java
<TesxtView
	android:text = "Hello World!"
	android:textSize = "20sp"
	android:textStyle = "bold"
	android:textColor = "#699fdb"/>
```

![image-20210713101140557](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210713101140557.png)

```java
<EditText
	android:hint = "请输入内容"/>
```

记住监听此控件

.addTextChangeListener(new TextWathcer(){});

### 3.布局

#### LinearLayout ：线性布局

方向 vertical/horizontal



嵌套布局会消耗算力，不建议使用线性的嵌套

#### RealtiveLayout ：相对布局

#### ScrollView ：滚动布局



# C》》》RecyclerView

不能用ScollView和LinearLayout了，会崩。

### 滚动控件

![image-20210713104124421](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210713104124421.png)



1.添加依赖 build.gradle

com.android.support:recycleview-v7:28.0.0

2.添加控件

3.创建Item的布局文件

4.继承或者编写自己的ViewHolder

​	ViewHolder中调用findViewById()提高效率

5.创建Adapter