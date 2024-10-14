package com.zhang.lib.httpktx.constant

import androidx.annotation.StringDef


/**
 * 接口请求中常用的MediaType枚举
 *
 * @author ZhangXiaoMing 2024-10-14 11:43 周一
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    APPLICATION_ATOM_XML ,
    APPLICATION_BASE64 ,
    APPLICATION_JAVASCRIPT ,
    APPLICATION_JSON ,
    APPLICATION_OCTET_STREAM ,
    APPLICATION_FORM_URL_ENCODED ,
    APPLICATION_XML ,
    MULTIPART_ALTERNATIVE ,
    MULTIPART_FORM_DATA ,
    MULTIPART_MIXED ,
    TEXT_CSS ,
    TEXT_HTML ,
    TEXT_PAINT ,
)
annotation class RequestContentType

const val APPLICATION_ATOM_XML = "application/atom+xml"
const val APPLICATION_BASE64 = "application/base64"
const val APPLICATION_JAVASCRIPT = "application/javascript"
const val APPLICATION_JSON = "application/json"
const val APPLICATION_OCTET_STREAM = "application/octet-stream"
const val APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded"
const val APPLICATION_XML = "application/xml"
const val MULTIPART_ALTERNATIVE = "multipart/alternative"
const val MULTIPART_FORM_DATA = "multipart/form-data"
const val MULTIPART_MIXED = "multipart/mixed"
const val TEXT_CSS = "text/css"
const val TEXT_HTML = "text/html"
const val TEXT_PAINT = "text/plain"