package com.zhang.lib.httpktx

import android.content.Context
import android.util.Log
import com.zhang.lib.httpktx.bean.RetrofitConfig
import com.zhang.lib.httpktx.ca.TrustCerts
import com.zhang.lib.httpktx.ca.TrustHostnameVerifier
import com.zhang.lib.httpktx.factory.XMConverterFactory
import com.zhang.lib.httpktx.interceptor.HeaderInterceptor
import com.zhang.lib.httpktx.interceptor.LogInterceptor
import com.zhang.lib.httpktx.ktx.addCallAdapterFactory
import com.zhang.lib.httpktx.ktx.addConverterFactory
import com.zhang.lib.httpktx.ktx.addInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

/**
 * 核心类
 *
 * @author ZhangXiaoMing 2024-10-14 10:52 周一
 */
object RetrofitSdk {


    /** 存储[Retrofit]对象的集合 */
    private val _retrofitMap by lazy { mutableMapOf<String , Retrofit>() }

    /** 存储接口类的集合 */
    private val _apiMap by lazy { mutableMapOf<String , MutableList<Any>>() }

    /** [OkHttpClient]对象 */
    var httpClient : OkHttpClient? = null
        private set

    internal var isRelease : Boolean = true

    /** 转换器工厂类列表 */
    private val converterFactoryList by lazy {
        mutableListOf<Converter.Factory>(XMConverterFactory { responseAnalyzer?.invoke(this) })
    }

    /** 适配工厂列表 */
    private val callAdapterFactoryList by lazy { mutableListOf<CallAdapter.Factory>() }


    /**
     * 初始化
     *
     * @param config 配置参数
     */
    fun init(context : Context , config : RetrofitConfig) : RetrofitSdk {
        isRelease = !config.isDebug

        trustUrlList.addAll(config.trustUrlList)


        httpClient = OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            connectTimeout(config.connectTimeout , TimeUnit.MILLISECONDS)
            readTimeout(config.readTimeout , TimeUnit.MILLISECONDS)
            writeTimeout(config.writeTimeout , TimeUnit.MILLISECONDS)

            if (config.cacheSize > 0)
                cache(Cache(context.cacheDir , config.cacheSize))

            if (isRelease) {
                proxy(Proxy.NO_PROXY)
                proxySelector(object : ProxySelector() {
                    override fun select(uri : URI?) : MutableList<Proxy> {
                        return mutableListOf(Proxy.NO_PROXY)
                    }

                    override fun connectFailed(uri : URI? , sa : SocketAddress? , ioe : IOException?) = Unit
                })
            } else {
                HttpLoggingInterceptor { logD { it } }.apply {
                    HttpLoggingInterceptor.Level.BODY
                }.also { addNetworkInterceptor(it) }
            }

            addInterceptor(LogInterceptor() , HeaderInterceptor())
            addInterceptor(config.interceptorList)

            sslSocketFactory(getSSLSocketFactory() , TrustCerts())
            hostnameVerifier(TrustHostnameVerifier())

            cookieJar(config.cookieJar)
        }.build()

        if (config.converterFactoryList.isNotEmpty()) {
            converterFactoryList.addAll(config.converterFactoryList)
        }

        if (config.callAdapterFactoryList.isNotEmpty()) {
            callAdapterFactoryList.addAll(config.callAdapterFactoryList)
        }

        return this
    }


    val trustUrlList by lazy { mutableListOf<String>() }

    /** 添加信任域名 */
    fun addTrustUrl(vararg url : String) = apply {
        trustUrlList.addAll(url)
    }

    /** 请求结果解析 */
    var responseAnalyzer : (String.() -> Unit)? = null

    /** 设置请求结果解析 */
    fun setResponseAnalyzer(analyzer : String.() -> Unit) = apply {
        this.responseAnalyzer = analyzer
    }

    /** 请求头构造器 */
    var headerBuilder : (() -> Map<String , String>)? = null

    /** 设置请求头构造器 */
    fun setHeaderBuilder(builder : () -> Map<String , String>) = apply {
        this.headerBuilder = builder
    }


    /**
     * 获取[Retrofit]对象，如果不存在则创建后返回
     *
     * @param url 域名
     */
    fun getRetrofit(url : String) = _retrofitMap.get(url) ?: synchronized(RetrofitSdk::class) {
        Retrofit.Builder().apply {
            baseUrl(url)
            client(httpClient ?: error("请先调用init()方法"))
            addConverterFactory(converterFactoryList)
            addCallAdapterFactory(callAdapterFactoryList)
        }.build().also {
            _retrofitMap.put(url , it)
        }
    }

    /**
     * 生成请求接口类对象
     *
     * @param url   请求域名
     * @param clazz 请求接口类
     * @param T     请求接口类
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(url : String , clazz : Class<T>) : T {
        val apiList = _apiMap[url] ?: synchronized(RetrofitSdk::class) {
            mutableListOf<Any>().also { _apiMap[url] = it }
        }

        apiList.forEach {
            if (clazz.isAssignableFrom(it.javaClass))
                return it as T
        }

        return getRetrofit(url).create(clazz).also {
            apiList.add(it)
        }
    }

}


/** 获取SSL Socket工厂 */
private fun getSSLSocketFactory() : SSLSocketFactory {
    try {
        val sslContext = SSLContext.getInstance(/* protocol = */ "TLS")
        sslContext.init(null , arrayOf<TrustManager>(TrustCerts()) , SecureRandom())
        return sslContext.socketFactory
    } catch (_ : Exception) {
    }

    return SSLSocketFactory.getDefault() as SSLSocketFactory
}


internal const val TAG = "XMRetrofit"

internal inline fun logD(tag : String = TAG , block : () -> String) {
    if (!RetrofitSdk.isRelease) {
        Log.d(tag , block())
    }
}

internal inline fun logI(tag : String = TAG , block : () -> String) {
    if (!RetrofitSdk.isRelease) {
        Log.i(tag , block())
    }
}

internal inline fun logW(tag : String = TAG , block : () -> String) {
    if (!RetrofitSdk.isRelease) {
        Log.w(tag , block())
    }
}

internal inline fun logE(tag : String = TAG , block : () -> String) {
    if (!RetrofitSdk.isRelease) {
        Log.e(tag , block())
    }
}