package com.zhang.lib.httpktx.factory

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * 转换器工厂
 *
 * @author ZhangXiaoMing 2024-10-14 18:59 周一
 */

internal class ConverterFactory @PublishedApi internal constructor(
    private val factory : Converter.Factory = GsonConverterFactory.create() ,
    private val analyzer : String.() -> Unit ,
) : Converter.Factory() {


    override fun requestBodyConverter(
        type : Type ,
        parameterAnnotations : Array<out Annotation> ,
        methodAnnotations : Array<out Annotation> ,
        retrofit : Retrofit
    ) : Converter<* , RequestBody>? {
        return factory.requestBodyConverter(type , parameterAnnotations , methodAnnotations , retrofit)
    }

    override fun responseBodyConverter(
        type : Type ,
        annotations : Array<out Annotation> ,
        retrofit : Retrofit
    ) : Converter<ResponseBody , *> {
        val converter = factory.responseBodyConverter(type , annotations , retrofit)
        return Converter<ResponseBody , _> {
            analyseResponseContent(it)

            if (type == String::class.java || converter == null)
                return@Converter it.string()

            converter.convert(it)
        }
    }


    /** 分析请求结果 */
    private fun analyseResponseContent(body : ResponseBody?) {
        with(body ?: return) {
            val source = source()
            try {
                source.request(Long.MAX_VALUE)
            } catch (_ : Exception) {
            }

            val content = if (contentLength() != 0L) source.buffer.clone().readUtf8() else ""
            analyzer(content)
        }
    }

    companion object {

        @JvmStatic
        fun create(
            factoryAgent : Converter.Factory = GsonConverterFactory.create() ,
            analyzer : String.() -> Unit
        ) = ConverterFactory(factoryAgent , analyzer)
    }
}