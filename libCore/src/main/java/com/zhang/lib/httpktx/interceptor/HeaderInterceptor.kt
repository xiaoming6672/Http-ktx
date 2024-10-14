package com.zhang.lib.httpktx.interceptor

import com.zhang.lib.httpktx.RetrofitSdk
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 统一添加Header拦截器
 *
 * @author ZhangXiaoMing 2024-10-14 11:46 周一
 */
internal class HeaderInterceptor : AbsInterceptor() {

    override fun interceptChain(chain : Interceptor.Chain) : Response {
        val request = chain.request()
        val headerMap = RetrofitSdk.headerBuilder?.invoke() ?: return chain.proceed()

        request.newBuilder().apply {
            headerMap.entries.forEach {
                if (it.key.isNotEmpty()     //key正常
                    && it.value.isNotEmpty()    //value正常
                    && !request.header(it.key).isNullOrEmpty() //接口未指定key的请求头
                ) {
                    addHeader(it.key , it.value)
                }
            }
        }.build().also {
            return chain.proceed(it)
        }
    }

}