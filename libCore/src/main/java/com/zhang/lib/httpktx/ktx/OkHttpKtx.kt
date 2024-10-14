package com.zhang.lib.httpktx.ktx

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * OkHttp包的拓展函数
 *
 * @author ZhangXiaoMing 2024-10-14 18:36 周一
 */

/** 添加拦截器列表 */
internal fun OkHttpClient.Builder.addInterceptor(vararg interceptor : Interceptor) = apply {
    interceptor.forEach { addInterceptor(it) }
}

/** 添加拦截器列表 */
internal fun OkHttpClient.Builder.addInterceptor(list : List<Interceptor>) = apply {
    list.forEach {
        addInterceptor(it)
    }
}

/** 添加转换器工厂类 */
internal fun Retrofit.Builder.addConverterFactory(list : List<Converter.Factory>) = apply {
    list.forEach { addConverterFactory(it) }
}

/** 添加适配工厂类 */
internal fun Retrofit.Builder.addCallAdapterFactory(list : List<CallAdapter.Factory>) = apply {
    list.forEach { addCallAdapterFactory(it) }
}