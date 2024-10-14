package com.zhang.lib.httpktx.interceptor

import com.zhang.lib.httpktx.RetrofitSdk
import com.zhang.lib.httpktx.logD
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONTokener

/**
 * 日志打印拦截器
 *
 * @author ZhangXiaoMing 2024-10-14 12:26 周一
 */
internal class LogInterceptor : AbsInterceptor() {

    override fun interceptChain(chain : Interceptor.Chain) : Response {
        if (RetrofitSdk.isRelease)
            return chain.proceed()

        val before = System.currentTimeMillis()

        val request = chain.request()
        val response = chain.proceed(request)

        val after = System.currentTimeMillis()
        val interceptorCost = after - before
        val responseCost = response.receivedResponseAtMillis - response.sentRequestAtMillis

        logD {
            "Retrofit请求>>>url=${request.url.toString().trim()}" +
                    "\n整体请求耗时(包含拦截器耗时)${interceptorCost.printTimeInfo()}，请求耗时${responseCost.printTimeInfo()}" +
                    "\n请求参数=${request.requestParams}" +
                    "\n请求结果=${response.responseContent}"
        }
        return response
    }
}

/** 打印时间格式 */
private fun Long.printTimeInfo() : String {
    return when {
        //1秒以内
        this < 1000           -> "${this}ms"
        //1分钟以内
        this < 60 * 1000      -> "${this / 1000F}s"
        //1小时以内
        this < 60 * 60 * 1000 -> "${this / 60000}m${this % 60000}s"
        else                  -> "${this}ms"
    }
}

/** 请求参数 */
private val Request?.requestParams : String
    get() {
        this ?: return "Request is Null"
        val body = body ?: return "无"

        val data = try {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } catch (e : Exception) {
            null
        }
        return data.takeUnless { data.isNullOrEmpty() } ?: "无"
    }

/** 请求结果 */
private val Response?.responseContent : String
    get() {
        this ?: return "Response is Null"
        if (!isSuccessful)
            return "Response is Fail"

        val body = body ?: return "Response body is Null"
        val contentLength = body.contentLength()
        val source = body.source()

        try {
            source.request(Long.MAX_VALUE)
        } catch (_ : Exception) {
        }

        var content = if (contentLength != 0L) source.buffer.clone().readUtf8()
        else ""

        try {
            if (content.isJson()) {
                content = JSONTokener(content).nextValue().toString()
            }
        } catch (_ : Exception) {
        }

        return content
    }

/** 是否是Json格式 */
private fun String.isJson() : Boolean = when {
    isEmpty()                        -> false
    startsWith("{") && endsWith("}") -> true
    startsWith("[") && endsWith("]") -> true
    else                             -> false
}