# ArcFaceDemo
Free SDK demo

>工程如何使用？
 1. 下载代码:    
    git clone https://github.com/mukever/ArcFaceDemo.git 或者直接下载压缩包
 
 2. 前往[官网](http://www.arcsoft.com.cn/ai/arcface.html)申请appid和sdkkey。    
    修改 ArcFaceDemo-master\src\main\java\com\arcsoft\sdk_demo\FaceDB.java 下面的对应的值:    
   
    ```java    
    public static String appid = "xxxx"; 		
    public static String fd_key = "xxxx";    
    public static String ft_key = "xxxx";
    public static String fr_key = "xxxx";
    ```
3. 下载sdk包之后，解压各个包里libs中的文件到 ArcFaceDemo-master\libs 下，同名so直接覆盖。

4. Android Studio3.0 中直接打开或者导入Project,编译运行即可。    


