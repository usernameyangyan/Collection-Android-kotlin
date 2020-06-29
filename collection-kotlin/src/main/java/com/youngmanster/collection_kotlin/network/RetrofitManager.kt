package com.youngmanster.collection_kotlin.network

import android.text.TextUtils
import android.util.Log
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.network.gson.GsonUtils
import com.youngmanster.collection_kotlin.network.interceptor.BasicParamsInterceptor
import com.youngmanster.collection_kotlin.network.progress.DownloadProgressBody
import com.youngmanster.collection_kotlin.network.progress.UploadProgressBody
import com.youngmanster.collection_kotlin.utils.LogUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
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
        private var mRetrofitWithGet: Retrofit? = null
        private var mWithoutHeadersRetrofit: Retrofit? = null
        private var mWithoutHeadersRetrofitWithGet: Retrofit? = null
        private var mNoCacheRetrofit: Retrofit? = null
        private var mNoCacheRetrofitWithGet: Retrofit? = null
        private var mNoCacheRetrofitWithoutHeaders: Retrofit? = null
        private var mNoCacheRetrofitWithoutHeadersWithGet: Retrofit? = null

        private var requestBuilder: RequestBuilder<*>? = null

        private fun getRetrofit(): Retrofit {

            if (mRetrofit == null) {

                mRetrofit = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient(isCache = true, isHeaders = true))
                    .build()
            }

            return mRetrofit!!
        }

        private fun getRetrofitWithGet(): Retrofit {

            if (mRetrofitWithGet == null) {

                mRetrofitWithGet = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientWithGet(isCache = true, isHeaders = true))
                    .build()
            }

            return mRetrofitWithGet!!
        }


        private fun getWithoutHeadersRetrofit(): Retrofit {

            if (mWithoutHeadersRetrofit == null) {

                mWithoutHeadersRetrofit = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient(isCache = true, isHeaders = false))
                    .build()
            }

            return mWithoutHeadersRetrofit!!
        }


        private fun getWithoutHeadersRetrofitWithGet(): Retrofit {

            if (mWithoutHeadersRetrofitWithGet == null) {

                mWithoutHeadersRetrofitWithGet = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientWithGet(isCache = true, isHeaders = false))
                    .build()
            }

            return mWithoutHeadersRetrofitWithGet!!
        }


        /***************************************************************没有设置缓存 */
        private fun getNoCacheRetrofit(): Retrofit {
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

        private fun getNoCacheRetrofitWithGet(): Retrofit {
            if (mNoCacheRetrofitWithGet == null) {
                mNoCacheRetrofitWithGet = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClientWithGet(isCache = false, isHeaders = true))
                    .build()
            }

            return mNoCacheRetrofitWithGet!!
        }


        private fun getNoCacheRetrofitWithoutHeaders(): Retrofit {

            if (mNoCacheRetrofitWithoutHeaders == null) {
                mNoCacheRetrofitWithoutHeaders = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(
                        getOkHttpClient(
                            isCache = false, isHeaders = false
                        )
                    )
                    .build()
            }

            return mNoCacheRetrofitWithoutHeaders!!
        }


        private fun getNoCacheRetrofitWithoutHeadersWithGet(): Retrofit {

            if (mNoCacheRetrofitWithoutHeadersWithGet == null) {
                mNoCacheRetrofitWithoutHeadersWithGet = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(
                        getOkHttpClient(
                            isCache = false, isHeaders = false
                        )
                    )
                    .build()
            }

            return mNoCacheRetrofitWithoutHeadersWithGet!!
        }


        fun getOkHttpClient(isCache: Boolean, isHeaders: Boolean): OkHttpClient {
            val builder = OkHttpClient.Builder()
            //如果不是在正式包，添加拦截 打印响应json
            if (Config.DEBUG) {
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("RetrofitManager", "收到响应: $message")
                    }
                })

                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
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


            builder.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
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


            return builder.connectTimeout(Config.CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(Config.READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Config.WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()

        }

        private var downloadRetrofit: Retrofit? = null
        private fun getDownloadRetrofit(requestBuilder: RequestBuilder<*>): Retrofit {
            if (downloadRetrofit == null) {
                downloadRetrofit = Retrofit.Builder()
                    .baseUrl(Config.URL_DOMAIN!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getDownloadOkHttpClient(requestBuilder))
                    .build()
            }

            return downloadRetrofit!!
        }

        private fun getDownloadOkHttpClient(requestBuilder: RequestBuilder<*>): OkHttpClient {
            val builder = OkHttpClient.Builder()
            if (Config.DEBUG) {
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("RetrofitManager", "收到响应: $message")
                    }
                })

                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                builder.addInterceptor(logging)

            }
            builder.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val originalResponse: okhttp3.Response = chain.proceed(chain.request())
                    return originalResponse.newBuilder()
                        .body(DownloadProgressBody(originalResponse.body, requestBuilder))
                        .build()

                }
            })

            return builder.build()
        }


        private fun getOkHttpClientWithGet(isCache: Boolean, isHeaders: Boolean): OkHttpClient {
            val builder = OkHttpClient.Builder()
            //如果不是在正式包，添加拦截 打印响应json
            if (Config.DEBUG) {
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("RetrofitManager", "收到响应: $message")
                    }
                })

                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
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
        fun <T> getApiService(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            this.requestBuilder = requestBuilder
            return getRetrofit().create(tClass)
        }

        fun <T> getApiServiceWithGet(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            this.requestBuilder = requestBuilder
            return getRetrofitWithGet().create(tClass)
        }

        /**
         * 接口定义类,获取到可以缓存的retrofit 没有设置全局的Headers
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getWithoutHeaderApiService(
            tClass: Class<T>,
            requestBuilder: RequestBuilder<*>?
        ): T {
            this.requestBuilder = requestBuilder
            return getWithoutHeadersRetrofit().create(tClass)
        }

        fun <T> getWithoutHeaderApiServiceWithGet(
            tClass: Class<T>,
            requestBuilder: RequestBuilder<*>?
        ): T {
            this.requestBuilder = requestBuilder
            return getWithoutHeadersRetrofitWithGet().create(tClass)
        }

        /**
         * 接口定义类,获取到没有缓存的retrofit
         *
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getNoCacheApiService(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            this.requestBuilder = requestBuilder
            return getNoCacheRetrofit().create(tClass)
        }

        fun <T> getNoCacheApiServiceWithGet(tClass: Class<T>, requestBuilder: RequestBuilder<*>?): T {
            this.requestBuilder = requestBuilder
            return getNoCacheRetrofitWithGet().create(tClass)
        }

        /**
         * 接口定义类,获取到没有缓存的retrofit  没有设置全局的Headers
         * @param tClass
         * @param <T>
         * @return
        </T> */
        fun <T> getNoCacheAndWithoutHeadersApiService(
            tClass: Class<T>,
            requestBuilder: RequestBuilder<*>?
        ): T {
            this.requestBuilder = requestBuilder
            return getNoCacheRetrofitWithoutHeaders().create(tClass)
        }

        fun <T> getNoCacheAndWithoutHeadersApiServiceWithGet(
            tClass: Class<T>,
            requestBuilder: RequestBuilder<*>?
        ): T {
            this.requestBuilder = requestBuilder
            return getNoCacheRetrofitWithoutHeadersWithGet().create(tClass)
        }


        /***
         * 设置上传下载
         */
        fun <T> getDownloadApiService(tClass: Class<T>, requestBuilder: RequestBuilder<*>): T {
            return getDownloadRetrofit(requestBuilder).create(tClass)
        }

    }


}
