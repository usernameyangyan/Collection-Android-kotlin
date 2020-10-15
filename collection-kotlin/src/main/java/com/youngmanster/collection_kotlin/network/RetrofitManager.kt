package com.youngmanster.collection_kotlin.network

import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.config.Config.Companion.CONTEXT
import com.youngmanster.collection_kotlin.config.Config.Companion.MAX_CACHE_SECONDS
import com.youngmanster.collection_kotlin.network.download.DownloadProgressBody
import com.youngmanster.collection_kotlin.network.interceptor.BasicParamsInterceptor
import com.youngmanster.collection_kotlin.network.progress.UploadProgressBody
import com.youngmanster.collection_kotlin.network.request.RequestMethodImpl
import com.youngmanster.collection_kotlin.utils.NetworkUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class RetrofitManager {

    companion object {

        private fun getRetrofit(requestBuilder: RequestBuilder<*>?): Retrofit {
            return Retrofit.Builder().baseUrl(Config.URL_DOMAIN!!)
                .client(getOkHttpClient(requestBuilder))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun getOkHttpClient(requestBuilder: RequestBuilder<*>?): OkHttpClient {
            val builder = OkHttpClient.Builder()
            //如果不是在正式包，添加拦截 打印响应json
            if (Config.DEBUG) {
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("RetrofitManager", "收到响应: $message")
                    }
                })

                if (requestBuilder?.httpType == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST ||
                    requestBuilder?.httpType == RequestBuilder.HttpType.DOWNLOAD_FILE_GET
                ) {
                    logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                } else {
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                }

                builder.addInterceptor(logging)

            }

            if (requestBuilder?.httpType == RequestBuilder.HttpType.DOWNLOAD_FILE_GET) {
                builder.addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val originalResponse: Response = chain.proceed(chain.request())
                        return originalResponse.newBuilder()
                            .body(
                                DownloadProgressBody(
                                    originalResponse.body,
                                    requestBuilder,
                                    originalResponse.code
                                )
                            )
                            .build()

                    }
                })

            } else {
                if (Config.HEADERS != null && Config.HEADERS!!.isNotEmpty()) {
                    val basicParamsInterceptor = BasicParamsInterceptor.Builder()
                        .addHeaderParamsMap(Config.HEADERS)
                        .build()

                    builder.addInterceptor(basicParamsInterceptor)
                }

                if (isCache(requestBuilder?.reqType!!) && !TextUtils.isEmpty(Config.URL_CACHE) && CONTEXT != null) {
                    //设置缓存
                    val httpCacheDirectory = File(Config.URL_CACHE!!)
                    builder.cache(Cache(httpCacheDirectory, Config.MAX_MEMORY_SIZE))
                    builder.addInterceptor(getInterceptor())


                }
                if (requestBuilder.httpType == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST) {
                    builder.addInterceptor(object : Interceptor {
                        @Throws(IOException::class)
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val original = chain.request()

                            val request = original.newBuilder()
                                .method(
                                    original.method,
                                    UploadProgressBody(original.body, requestBuilder)
                                )
                                .build()

                            return chain.proceed(request)
                        }
                    })
                }

            }


            return builder.connectTimeout(Config.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(Config.READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Config.WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()

        }

        //是否需要设置Retrofit缓存
        private fun isCache(type: Int): Boolean {
            if (type == RequestBuilder.ReqType.DEFAULT_CACHE_LIST ||
                type == RequestBuilder.ReqType.DEFAULT_CACHE_MODEL
            ) {
                return true
            }
            return false
        }



        /**
         * ===============================Retrofit+OkHttp的缓存机制=========================================
         */
        private fun getInterceptor(): Interceptor {
            return object : Interceptor {
                @Throws(java.io.IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val cacheBuilder = CacheControl.Builder()
                    cacheBuilder.maxAge(0, TimeUnit.SECONDS)
                    cacheBuilder.maxStale(365, TimeUnit.DAYS)
                    val cacheControl = cacheBuilder.build()
                    var request = chain.request()
                    if (!NetworkUtils.isNetworkConnected(CONTEXT)) {
                        request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build()
                    }
                    val originalResponse = chain.proceed(request)
                    return if (NetworkUtils.isNetworkConnected(CONTEXT)) {
                        val maxAge = 0 // read from cache
                        originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public ,max-age=$maxAge")
                            .build()
                    } else {
                        val maxStale =
                            MAX_CACHE_SECONDS // tolerate 4-weeks stale
                        originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                            .build()
                    }
                }
            }
        }


        /**
         * 接口定义类
         * @param tClass
         * @param requestBuilder
         * @return
         */
        fun <T> getApiService(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            return getRetrofit(requestBuilder).create(tClass)
        }


        /**
         * 自定义接口请求
         * @param tClass
         * @return
         */
        private class SingletonHolder {
            companion object {
                val INSTANCE: CustomizeRequest = CustomizeRequest()
            }

        }

        fun getCustomizeRequest(): CustomizeRequest {
            return SingletonHolder.INSTANCE
        }
    }

    class CustomizeRequest {
        private var okHttpClient: OkHttpClient? = null

        /**
         * 自定义
         */
        fun setCustomizeOkHttpClient(okHttpClient: OkHttpClient): CustomizeRequest {
            this.okHttpClient = okHttpClient
            return this
        }


        fun <T> getCustomizeApiService(tClass: Class<T>?): T {
            return getRetrofit().create(tClass)
        }


        /**
         * 自定义默认Retrofit
         */
        private fun getRetrofit(): Retrofit {
            if (okHttpClient == null) {
                okHttpClient = getDefaultOkHttpClient()
            }
            return Retrofit.Builder().baseUrl(Config.URL_DOMAIN!!)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }


        /**
         * 自定义默认OkHttpClient
         */
        private fun getDefaultOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            //如果不是在正式包，添加拦截 打印响应json
            if (Config.DEBUG) {
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("RetrofitManager", "收到响应: $message")
                    }
                })

                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(logging)

            }

            if (Config.HEADERS != null && Config.HEADERS!!.isNotEmpty()) {
                val basicParamsInterceptor = BasicParamsInterceptor.Builder()
                    .addHeaderParamsMap(Config.HEADERS)
                    .build()

                builder.addInterceptor(basicParamsInterceptor)
            }

            if (!TextUtils.isEmpty(Config.URL_CACHE) && CONTEXT != null) {
                //设置缓存
                val httpCacheDirectory = File(Config.URL_CACHE!!)
                builder.cache(Cache(httpCacheDirectory, Config.MAX_MEMORY_SIZE))
                builder.addInterceptor(getInterceptor())
            }

            return builder.connectTimeout(
                Config.CONNECT_TIMEOUT_SECONDS.toLong(),
                TimeUnit.SECONDS
            )
                .readTimeout(Config.READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Config.WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()

        }

    }
}
