package com.zhang.lib.httpKtx.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zhang.lib.httpKtx.demo.databinding.ActivityMainBinding
import com.zhang.lib.httpktx.RetrofitSdk
import com.zhang.lib.httpktx.bean.RetrofitConfig
import com.zhang.lib.ktx.toMD5
import kotlinx.coroutines.launch
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitConfig.Builder().apply {
            setDebug(true)
            setConnectTimeout(15 * 1000)
            setReadTimeout(15 * 1000)
            setWriteTimeout(15 * 1000)
            setCacheSize(10 * 1024 * 1024)
        }.build().also {
            RetrofitSdk.init(application , it)
                .setHeaderBuilder { mutableMapOf() }
                .setResponseAnalyzer { }
                .addTrustUrl("https://fanyi-api.baidu.com")
        }

        binding.btnTest.setOnClickListener {
            lifecycleScope.launch {

                val originContent = "你好"
                val appId = "appId" //（APPID）可在[百度翻译开放平台](http://api.fanyi.baidu.com/) 查看
                val salt = "123456"
                val secret = "secret"
                val sign = "$appId$originContent$salt$secret".toMD5()

                RetrofitSdk.create("https://fanyi-api.baidu.com" , BaiDuTranslateApi::class.java)
                    .translate(
                        originContent = originContent ,
                        appId = appId ,
                        salt = salt ,
                        sign = sign
                    )
            }
        }
    }
}


interface BaiDuTranslateApi {

    @POST("api/trans/vip/translate")
    @FormUrlEncoded
    suspend fun translate(
        @Field("q") originContent : String ,
        @Field("from") from : String = "auto" ,
        @Field("to") to : String = "auto" ,
        @Field("appid") appId : String ,
        @Field("salt") salt : String ,
        @Field("sign") sign : String ,
    ) : Any
}