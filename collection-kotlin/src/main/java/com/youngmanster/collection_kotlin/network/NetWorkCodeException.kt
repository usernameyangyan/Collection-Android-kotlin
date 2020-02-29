package com.youngmanster.collection_kotlin.network

import android.net.ParseException
import com.google.gson.JsonParseException
import okhttp3.Connection
import org.json.JSONException
import retrofit2.HttpException

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class NetWorkCodeException{
    /**
     * ========================返回的code==================================
     */

    companion object{

        private val UNAUTHORIZED = 401
        private val FORBIDDEN = 403
        private val NOT_FOUND = 404
        private val REQUEST_TIMEOUT = 408
        private val INTERNAL_SERVER_ERROR = 500
        private val BAD_GATEWAY = 502
        private val SERVICE_UNAVAILABLE = 503
        private val GATEWAY_TIMEOUT = 504

        /**
         * 自定义的code
         */

        //未知错误
        val UNKNOWN = 1000
        //解析错误
        val PARSE_ERROR = 1001
        //网络错误
        val NETWORD_ERROR = 1002
        //协议出错
        val HTTP_ERROR = 1003
        //证书出错
        val SSL_ERROR = 1005

        fun getResponseThrowable(e: Throwable): ResponseThrowable {
            val ex: ResponseThrowable

            if (e is HttpException) {
                ex = ResponseThrowable()
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = "请检查权限"
                    }
                    NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = "网络错误"
                    }
                    else -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = "网络错误"
                    }
                }
                return ex
            } else if (e is ServerException) {
                ex = ResponseThrowable()
                ex.code = e.code
                ex.errorMessage = e.message
                return ex
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                ex = ResponseThrowable()
                ex.code = PARSE_ERROR
                ex.errorMessage = "解析错误"
                return ex
            } else if (e is Connection) {
                ex = ResponseThrowable()
                ex.code = NETWORD_ERROR
                ex.errorMessage = "连接失败"
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ResponseThrowable()
                ex.code = SSL_ERROR
                ex.errorMessage = "证书验证失败"
                return ex
            } else {
                ex = ResponseThrowable()
                ex.code = NETWORD_ERROR
                ex.errorMessage = "网络错误"
                return ex
            }
        }
    }


    class ResponseThrowable : Exception() {
        var code: Int = 0
        var errorMessage: String? = null
    }

    inner class ServerException : RuntimeException() {
        var code: Int = 0
        var errorMessage: String? = null
    }
}