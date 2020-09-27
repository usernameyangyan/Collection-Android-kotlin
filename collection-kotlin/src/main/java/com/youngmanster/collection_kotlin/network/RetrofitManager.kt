package com.youngmanster.collection_kotlin.network

import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.config.Config.Companion.CONTEXT
import com.youngmanster.collection_kotlin.config.Config.Companion.MAX_CACHE_SECONDS
import com.youngmanster.collection_kotlin.network.interceptor.BasicParamsInterceptor
import com.youngmanster.collection_kotlin.network.download.DownloadProgressBody
import com.youngmanster.collection_kotlin.network.progress.UploadProgressBody
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

        private const val DEFAULT_REQUEST=0
        private const val MULTIPLE_MULTIPART_POST=1
        private const val DOWNLOAD_FILE_GET=2
        private const val HEADERS_REQUEST=3
        private const val HEADERS_CACHE_REQUEST=4
        private const val CACHE_REQUEST=5

        private var mRetrofit: Retrofit? = null
        private val retrofitMap=ArrayMap<Int,Retrofit>()

        private fun getRetrofit(requestBuilder: RequestBuilder<*>?): Retrofit {

            val type=getRetrofitTypeCode(requestBuilder)
            mRetrofit=retrofitMap[type]
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder().baseUrl(Config.URL_DOMAIN!!)
                    .client(getOkHttpClient(requestBuilder))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

                retrofitMap[type]=mRetrofit
            }
            return mRetrofit!!
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
                    override fun intercept(chain: Interceptor.Chain):Response {
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

            }else {
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
                if(requestBuilder.httpType == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST){
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


        //获取当前Retrofit类型值
        private fun getRetrofitTypeCode(requestBuilder: RequestBuilder<*>?):Int{
            if(requestBuilder?.httpType == RequestBuilder.HttpType.DOWNLOAD_FILE_GET){
                return DOWNLOAD_FILE_GET
            }else{
                var isHeaders=false
                var isCache=false
                if(Config.HEADERS != null && Config.HEADERS!!.isNotEmpty()){
                    isHeaders=true
                }

                if (isCache(requestBuilder?.reqType!!) && !TextUtils.isEmpty(Config.URL_CACHE) && CONTEXT != null) {
                    isCache=true
                }

                if(requestBuilder.httpType == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST){
                    return MULTIPLE_MULTIPART_POST
                }

                if(isHeaders&&isCache){
                    return HEADERS_CACHE_REQUEST
                }
                if(isHeaders){
                    return HEADERS_REQUEST
                }
                if(isCache){
                    return CACHE_REQUEST
                }

                return DEFAULT_REQUEST
            }
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
         * 接口定义类,获取到可以缓存的retrofit
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getApiService(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            return getRetrofit(requestBuilder).create(tClass)
        }
    }
}
