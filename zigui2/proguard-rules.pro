# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, these files are no longer used. Newer
# versions are distributed with the plugin and unpacked at build time. Files in this directory are
# no longer maintained.

#表示混淆时不使用大小写混合类名
-dontusemixedcaseclassnames
#表示不跳过library中的非public的类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers



#打印混淆的详细信息 #生成原类名和混淆后的类名的映射文件
-verbose
-printmapping proguardMapping.txt

#代码混淆的压缩比例，值在0-7之间
-optimizationpasses 5

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).

##表示不进行校验,这个校验作用 在java平台上的
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

#-optimizations optimization_filter
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#不混淆Annotation
-keepattributes *Annotation*,InnerClasses


#忽略警告
#-ignorewarnings
#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
#保护泛型(混淆泛型)
-keepattributes Signature

#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable



-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}


#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}




-dontwarn com.easefun.polyvsdk.**
-keep class com.easefun.polyvsdk.**{*;}

-dontwarn tv.danmaku.ijk.media.**
-keep class tv.danmaku.ijk.media.**{*;}

-dontwarn com.mob.wrappers.**
-keep class com.mob.wrappers.**{*;}

-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties



-dontwarn com.dh.DpsdkCore.**
-keep class com.dh.DpsdkCore.**{*;}

-dontwarn com.google.android.**
-keep class com.google.android.**{*;}


-dontwarn com.mob.**
-keep class com.mob.**{*;}


-dontwarn net.sqlcipher.database.**
-keep class net.sqlcipher.database.**{*;}

-dontwarn rx.**
-keep class rx.**{*;}

-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.**{*;}


-dontwarn java.nio.**
-keep class java.nio.**{*;}

-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.**{*;}

-dontwarn java.lang.**
-keep class java.lang.**{*;}

-dontwarn pl.droidsonroids.**
-keep class pl.droidsonroids.**{*;}

-dontwarn com.fall.view.**
-keep class com.fall.view.**{*;}

-dontwarn cn.smssdk.**
-keep class cn.smssdk.**{*;}

-dontwarn com.squareup.**
-keep class com.squareup.**{*;}



#环信
-dontwarn ch.imvs.**
-keep class ch.imvs.**{*;}




-dontwarn com.alipay.**
-keep class com.alipay.**{*;}


#百度
-dontwarn com.baidu.**
-keep class vi.com.** {*;}
-keep class com.baidu.**{*;}

-dontwarn android.util.**
-keep class android.util.**{*;}


-dontwarn org.apache.**
-keep class org.apache.**{*;}



-dontwarn javax.annotation.**
-keep class javax.annotation.**{*;}

-dontwarn android.app.**
-keep class android.app.**{*;}



#okHttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}


#Gson
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod




-dontwarn org.slf4j.**
-keep class org.slf4j.**{*;}



#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.**{*;}
-keepattributes Signature
-keepattributes Exceptions



#实体类
-dontwarn com.wcyc.zigui2.newapp.bean.**
-keep class com.wcyc.zigui2.newapp.bean.**{*;}

-dontwarn com.wcyc.zigui2.newapp.module.**
-keep class com.wcyc.zigui2.newapp.module.**{*;}


-keep public class * implements java.io.Serializable {*;}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}




#与js交互的类
-keep class com.wcyc.zigui2.core.**{*;}



-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
