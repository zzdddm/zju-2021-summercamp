# Android 存储

DB 、I/O stream 、storage

不在主线程，会卡

缓存设计



### 存储架构

内部存储、外部存储（有很多个）

内置存储、外置存储

安卓虚拟分区：内部存储应用私有、外部存储（私有\公有）



一些存储目录：

Internal:

App,files,cache,db……

External:

DCIM,Dowload,Movies……



### 文件

File(String pathname )

File(String parent, String child )

File(File parent, String child )

File(URI uri)

exsits()

createNewFile()

mkdir() \ mkdirs()

List() \ listFiles()

getFreeSpace() \ getTotalSpace()



context.getFlieDir():返回文件路径

context.getChacheDir()

context.getDie(name,mode_private):自定义目录，一般都是mode_private

#### 使用 Environment APIs

授权：：：：：获取权限 -> 检查外置存储的可用性

1.在manifest中声明权限（安装时候一大堆）

2.动态申请权限6.0:

​	ActivityCompat.requestPermission(this, new, String[]{Mnaifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

3.可用性检查（稍微注意一下，现在很多手机都没SD card了）



#### 文件IO

io流：泛指计算机系统中把各个储存设备之间（存储及网络）的数据读写的过程



1.创建新文件（完整目录）（File.separeator）

2.文件是否存在

3.输出流：FileOutputSream （输出到文件）（写二进制数据，注意类型转换）

4.关流！

5.输入流（按二进制储存，，，byte[1024]字节数组）



文件拷贝（by流），一个输入一个输出串联



#### ShareReferences

用户配置信息（省流）用键值对（key-value结构）哈

读：

写：

小数据、全量写、使用cache以免ANR



#### JCDB

类：SQLiteOpenHelper， SQLiteDatabase

getReadbleDatabase（）

事务

