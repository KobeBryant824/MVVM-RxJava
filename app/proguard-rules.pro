# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 忽略警告
-ignorewarnings
-dontwarn
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

# 不混淆实体类
-keep class com.cxh.mvpsample.model.api.**{*;}
#自定义控件不参与混淆
-keep class com.cxh.mvpsample.ui.widget.** { *; }

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

# EventBus混淆
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# LeakCanary混淆
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

-keep class android.support.v4.**{*;}
-keep class android.support.v7.**{*;}
-keep class android.support.design.**{*;}

-keep class com.zhy.autolayout.widget.**{*;}
-keep class com.zhy.autolayout.**{*;}
-keep class com.hss01248.pagestate.**{*;}

-keep class com.socks.**{*;}
-keep class com.alibaba.fastjson.**{*;}
-keep class com.github.**{*;}

-keep class retrofit2.converter.fastjson.**{*;}
-keep class me.tatarka.bindingcollectionadapter2.**{*;}