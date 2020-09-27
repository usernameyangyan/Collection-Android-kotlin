package com.youngmanster.collection_kotlin.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.utils.NetworkUtils
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

        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        val CHECK_PERMISSION="请检查权限"
        val HTTP_ERROR_MESSAGE="服务器错误"
        val PARSE_ERROR_MESSAGE="解析错误"
        val NETWORD_ERROR_MESSAGE="网络错误"
        val CONNECTION_SERVICE_ERROR_MESSAGE="连接服务器错误"
        val SSL_ERROR_MESSAGE="证书验证失败"
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
        //服务器连接失败
        val CONNECTION_SERVICE_ERROR = 1006



        fun isError(code:Int):Boolean{
            if(code==UNAUTHORIZED||code==FORBIDDEN||code==NOT_FOUND||
                code==REQUEST_TIMEOUT||code==INTERNAL_SERVER_ERROR||code==BAD_GATEWAY||
                code==SERVICE_UNAVAILABLE||code==GATEWAY_TIMEOUT||code==PARSE_ERROR||
                code==NETWORD_ERROR||code==HTTP_ERROR||code==SSL_ERROR||
                code==CONNECTION_SERVICE_ERROR){
                return true
            }
            return false
        }

        fun getResponseThrowable(e: Throwable): ResponseThrowable {
            val ex: ResponseThrowable

            if (e is HttpException) {
                ex = ResponseThrowable()
                when (e.code()) {
                    UNAUTHORIZED, FORBIDDEN -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = CHECK_PERMISSION
                    }
                    NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = HTTP_ERROR_MESSAGE
                    }
                    else -> {
                        ex.code = HTTP_ERROR
                        ex.errorMessage = HTTP_ERROR_MESSAGE
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
                ex.errorMessage = PARSE_ERROR_MESSAGE
                return ex
            } else if (e is Connection) {
                ex = ResponseThrowable()
                if(NetworkUtils.isNetworkConnected(Config.CONTEXT)){
                    ex.code = CONNECTION_SERVICE_ERROR
                    ex.errorMessage =CONNECTION_SERVICE_ERROR_MESSAGE
                }else{
                    ex.code = NETWORD_ERROR
                    ex.errorMessage =NETWORD_ERROR_MESSAGE
                }

                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                ex = ResponseThrowable()
                ex.code = SSL_ERROR
                ex.errorMessage = SSL_ERROR_MESSAGE
                return ex
            } else {
                ex = ResponseThrowable()
                ex.code = NETWORD_ERROR
                ex.errorMessage = NETWORD_ERROR_MESSAGE
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