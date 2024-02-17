# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

#-keep,allowobfuscation,allowshrinking interface retrofit2.Call
#-keep,allowobfuscation,allowshrinking class retrofit2.Response
#
#-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
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
-keepattributes Signature
-keepattributes Annotation
#-keep class sun.misc.Unsafe { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class com.bsuir.bsuirschedule.domain.models.* { *; }
-keep class com.bsuir.bsuirschedule.data.db.entities.* { *; }
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type


