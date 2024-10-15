package com.zhang.lib.httpktx.ca

import android.net.Uri
import com.zhang.lib.httpktx.RetrofitSdk
import com.zhang.lib.httpktx.logD
import com.zhang.lib.httpktx.logI
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * 信任主机名验证程序
 *
 * @author ZhangXiaoMing 2024-10-14 11:32 周一
 */
internal class TrustHostnameVerifier : HostnameVerifier {

    /**
     * Verify that the host name is an acceptable match with
     * the server's authentication scheme.
     *
     * @param hostname the host name
     * @param session SSLSession used on the connection to host
     * @return true if the host name is acceptable
     */
    override fun verify(hostname : String? , session : SSLSession?) : Boolean {
        logD { "TrustHostnameVerifier>>>verify()>>>hostname=$hostname" }

        for (url in RetrofitSdk.trustUrlList) {
            val uri = Uri.parse(url)
            logI { "TrustHostnameVerifier>>>verify()>>>trustUrl=$url, host=${uri.host}" }
            if (uri.host == hostname)
                return true
        }

        return false
    }
}