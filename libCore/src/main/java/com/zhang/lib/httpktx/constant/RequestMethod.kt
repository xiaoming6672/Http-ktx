package com.zhang.lib.httpktx.constant

import androidx.annotation.StringDef

/**
 * Http请求方式
 *
 * @author ZhangXiaoMing 2024-10-14 11:44 周一
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    GET ,
    POST ,
    PUT ,
    HEAD ,
    DELETE ,
    OPTIONS ,
    TRACE ,
    PATCH ,
    CONNECT ,
    MKCOL ,
    COPY ,
    MOVE ,
    LOCK ,
    UNLOCK ,
)
annotation class RequestMethod()

const val GET = "GET"
const val POST = "POST"
const val PUT = "PUT"
const val HEAD = "HEAD"
const val DELETE = "DELETE"
const val OPTIONS = "OPTIONS"
const val TRACE = "TRACE"
const val PATCH = "PATCH"
const val CONNECT = "CONNECT"
const val MKCOL = "MKCOL"
const val COPY = "COPY"
const val MOVE = "MOVE"
const val LOCK = "LOCK"
const val UNLOCK = "UNLOCK"