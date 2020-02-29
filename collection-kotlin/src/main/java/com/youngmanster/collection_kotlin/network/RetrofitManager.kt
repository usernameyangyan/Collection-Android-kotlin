package com.youngmanster.collection_kotlin.network

import android.text.TextUtils
import android.util.Log
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.network.interceptor.BasicParamsInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class RetrofitManager {

    companion object {

        private var mRetrofit: Retrofit? = null
        private var mWithoutHeadersRetrofit: Retrofit? = null
        private var mNoCacheRetrofit: Retrofit? = null
        private var mNoCacheRetrofitWithoutHeaders: Retrofit? = null


        private val retrofit: Retrofit
            get() {

                if (mRetrofit == null) {

                    mRetrofit = Retrofit.Builder()
                        .baseUrl(Config.URL_DOMAIN!!)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient(isCache = true, isHeaders = true))
                        .build()
                }

                return mRetrofit!!
            }


        private val retrofitWithoutHeader: Retrofit
            get() {

                if (mWithoutHeadersRetrofit == null) {

                    mWithoutHeadersRetrofit = Retrofit.Builder()
                        .baseUrl(Config.URL_DOMAIN!!)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient(isCache = true, isHeaders = false))
                        .build()
                }

                return mWithoutHeadersRetrofit!!
            }


        /***************************************************************没有设置缓存 */
        private val noCacheRetrofit: Retrofit
            get() {

                if (mNoCacheRetrofit == null) {
                    mNoCacheRetrofit = Retrofit.Builder()
                        .baseUrl(Config.URL_DOMAIN!!)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient(isCache = false, isHeaders = true))
                        .build()
                }

                return mNoCacheRetrofit!!
            }


        private val noCacheRetrofitWithoutHeaders: Retrofit
            get() {

                if (mNoCacheRetrofitWithoutHeaders == null) {
                    mNoCacheRetrofitWithoutHeaders = Retrofit.Builder()
                        .baseUrl(Config.URL_DOMAIN!!)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getOkHttpClient(isCache = false, isHeaders = false))
                        .build()
                }

                return mNoCacheRetrofitWithoutHeaders!!
            }


        fun  getOkHttpClient(isCache: Boolean, isHeaders: Boolean): OkHttpClient {
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

            if (isHeaders && Config.HEADERS != null && Config.HEADERS!!.isNotEmpty()) {
                val basicParamsInterceptor = BasicParamsInterceptor.Builder()
                    .addHeaderParamsMap(Config.HEADERS)
                    .build()

                builder.addInterceptor(basicParamsInterceptor)
            }

            if (isCache && !TextUtils.isEmpty(Config.URL_CACHE) && Config.CONTEXT != null) {
                //设置缓存
                val httpCacheDirectory = File(Config.URL_CACHE!!)
                builder.cache(Cache(httpCacheDirectory, Config.MAX_MEMORY_SIZE))
                builder.addInterceptor(RequestManager.getInterceptor())
            }

            return builder.connectTimeout(Config.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(Config.READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Config.WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()

        }


        /**
         * 接口定义类,获取到可以缓存的retrofit
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getApiService(tClass: Class<T>): T {
            return retrofit.create(tClass)
        }

        /**
         * 接口定义类,获取到可以缓存的retrofit 没有设置全局的Headers
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getWithoutHeaderApiService(tClass: Class<T>): T {
            return retrofitWithoutHeader.create(tClass)
        }

        /**
         * 接口定义类,获取到没有缓存的retrofit
         *
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getNoCacheApiService(tClass: Class<T>): T {
            return noCacheRetrofit.create(tClass)
        }

        /**
         * 接口定义类,获取到没有缓存的retrofit  没有设置全局的Headers
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getNoCacheAndWithoutHeadersApiService(tClass: Class<T>): T {
            return noCacheRetrofitWithoutHeaders.create(tClass)
        }
    }


}
