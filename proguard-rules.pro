# Retrofit rules
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp3 rules
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Gson rules
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* { *; }

# PolyMarket Bot API Data Classes
-keep class com.polymarket.tradingbot.api.** { *; }
-keep class com.polymarket.tradingbot.repository.** { *; }
-keep class com.polymarket.tradingbot.ui.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep all model classes for Gson serialization
-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
