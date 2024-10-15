package com.zhang.lib.httpktx.interceptor

import androidx.annotation.CallSuper
import com.zhang.lib.httpktx.logD
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 内部父类抽象拦截器
 *
 * @author ZhangXiaoMing 2024-10-14 11:47 周一
 */
abstract class AbsInterceptor : Interceptor {

    @CallSuper
    override fun intercept(chain : Interceptor.Chain) : Response {
        logD { "${javaClass.name}>>>intercept()" }
        return interceptChain(chain)
    }


    abstract fun interceptChain(chain : Interceptor.Chain) : Response
}

fun Interceptor.Chain.proceed() = proceed(request())