package com.zhang.lib.httpktx.bean

import okhttp3.CookieJar
import okhttp3.Interceptor
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * 初始化参数
 *
 * @author ZhangXiaoMing 2024-10-14 11:29 周一
 */
class RetrofitConfig private constructor(
    val isDebug : Boolean ,
    val connectTimeout : Long ,
    val readTimeout : Long ,
    val writeTimeout : Long ,
    val cacheSize : Long ,
    val interceptorList : List<Interceptor> ,
    val trustUrlList : List<String> ,
    val cookieJar : CookieJar = CookieJar.NO_COOKIES ,
    val converterFactoryList : List<Converter.Factory> ,
    val callAdapterFactoryList : List<CallAdapter.Factory> ,
) {

    class Builder {

        private var isDebug : Boolean = false
        private var connectTimeout : Long = 15 * 1000L
        private var readTimeout : Long = 15 * 1000L
        private var writeTimeout : Long = 15 * 1000L
        private var cacheSize : Long = 0L
        private val interceptorList = mutableListOf<Interceptor>()
        private val trustUrlList = mutableListOf<String>()
        private var cookieJar : CookieJar = CookieJar.NO_COOKIES
        private val converterFactoryList = mutableListOf<Converter.Factory>()
        private val adapterFactoryList = mutableListOf<CallAdapter.Factory>()


        /**
         * 设置debug状态
         *
         * @param debug debug状态
         */
        fun setDebug(debug : Boolean) = apply { isDebug = debug }

        /**
         * 设置连接超时时间，单位：毫秒
         *
         * @param timeout 超时时间
         */
        fun setConnectTimeout(timeout : Long) = apply { connectTimeout = timeout }

        /**
         * 设置读取超时时间，单位：毫秒
         *
         * @param timeout 超时时间
         */
        fun setReadTimeout(timeout : Long) = apply { readTimeout = timeout }

        /**
         * 设置写入超时时间，单位：毫秒
         *
         * @param timeout 超时时间
         */
        fun setWriteTimeout(timeout : Long) = apply { writeTimeout = timeout }

        /**
         * 设置缓存大小
         *
         * @param size 缓存大小
         */
        fun setCacheSize(size : Long) = apply { cacheSize = size }

        /**
         * 添加拦截器
         *
         * @param interceptor 拦截器
         */
        fun addInterceptor(vararg interceptor : Interceptor) = apply { interceptorList.addAll(interceptor) }

        /**
         * 添加信任链接
         *
         * @param trustUrl 链接
         */
        fun addTrustUrl(vararg trustUrl : String) = apply { trustUrlList.addAll(trustUrl) }

        /** 设置CookieJar */
        fun setCookieJar(jar : CookieJar) = apply { cookieJar = jar }

        /** 添加转换工厂类 */
        fun addConverterFactory(vararg factory : Converter.Factory) = apply { converterFactoryList.addAll(factory) }

        /** 添加适配工厂类 */
        fun addCallAdapterFactory(vararg factory : CallAdapter.Factory) = apply { adapterFactoryList.addAll(factory) }

        /** 构建 */
        fun build() = RetrofitConfig(
            isDebug = isDebug ,
            connectTimeout = connectTimeout ,
            readTimeout = readTimeout ,
            writeTimeout = writeTimeout ,
            cacheSize = cacheSize ,
            interceptorList = interceptorList ,
            trustUrlList = trustUrlList ,
            cookieJar = cookieJar ,
            converterFactoryList = converterFactoryList ,
            callAdapterFactoryList = adapterFactoryList
        )
    }
}
