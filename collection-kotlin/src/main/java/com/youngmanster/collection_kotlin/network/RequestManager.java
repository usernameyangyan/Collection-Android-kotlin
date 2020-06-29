package com.youngmanster.collection_kotlin.network;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.youngmanster.collection_kotlin.config.Config;
import com.youngmanster.collection_kotlin.network.gson.GsonUtils;
import com.youngmanster.collection_kotlin.network.progress.DownloadFileHelper;
import com.youngmanster.collection_kotlin.network.rx.RxSchedulers;
import com.youngmanster.collection_kotlin.network.rx.RxSubscriber;
import com.youngmanster.collection_kotlin.network.synchronization.OkHttpUtils;
import com.youngmanster.collection_kotlin.utils.FileUtils;
import com.youngmanster.collection_kotlin.utils.LogUtils;
import com.youngmanster.collection_kotlin.utils.NetworkUtils;
import com.youngmanster.collection_kotlin.utils.ThreadPoolManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 请求管理
 * Created by yangyan
 * on 2018/3/23.
 */

public class RequestManager {

    private static RequestManager requestManager;

    public static RequestManager getInstance() {
        if (requestManager == null) {
            synchronized (RequestManager.class) {
                if (requestManager == null) {
                    requestManager = new RequestManager();
                }
            }
        }

        return requestManager;
    }

    private RequestManager() {
    }

    public <T> DisposableObserver<ResponseBody> request(RequestBuilder<T> builder) {

        if (builder.getReqMode() == RequestBuilder.ReqMode.ASYNCHRONOUS) {
            if (builder.getReqType() == RequestBuilder.ReqType.DOWNLOAD_FILE_MODEL) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return downloadFile(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.NO_CACHE_MODEL) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadOnlyNetWorkModel(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.NO_CACHE_LIST) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadOnlyNetWorkList(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DEFAULT_CACHE_MODEL) {
                Observable<ResponseBody> observable = getRetrofit(builder, true);
                return loadOnlyNetWorkModel(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DEFAULT_CACHE_LIST) {
                Observable<ResponseBody> observable = getRetrofit(builder, true);
                return loadOnlyNetWorkList(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_LIST_LIMIT_TIME) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadFormDiskResultListLimitTime(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_MODEL_LIMIT_TIME) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadFormDiskModeLimitTime(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_NO_NETWORK_LIST) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadNoNetWorkWithCacheResultList(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_NO_NETWORK_MODEL) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadNoNetWorkWithCacheModel(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_NETWORK_SAVE_RETURN_MODEL) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadOnlyNetWorkSaveModel(builder, observable);
            } else if (builder.getReqType() == RequestBuilder.ReqType.DISK_CACHE_NETWORK_SAVE_RETURN_LIST) {
                Observable<ResponseBody> observable = getRetrofit(builder, false);
                return loadOnlyNetWorkSaveList(builder, observable);
            }
            return null;
        } else {
            OkHttpUtils.requestSyncData(builder);
            return null;
        }


    }

    private <T> Observable<ResponseBody> getRetrofit(RequestBuilder<T> builder, boolean isCache) {
        if (builder.getHttpType() == RequestBuilder.HttpType.DEFAULT_GET) {
            if (isCache) {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getWithoutHeaderApiServiceWithGet(RequestService.class, builder).getObservableWithQueryMapWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getApiServiceWithGet(RequestService.class, builder).getObservableWithQueryMap(builder.getUrl(), builder.getRequestParam());
                }
            } else {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getNoCacheAndWithoutHeadersApiServiceWithGet(RequestService.class, builder).getObservableWithQueryMapWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getNoCacheApiServiceWithGet(RequestService.class, builder).getObservableWithQueryMap(builder.getUrl(), builder.getRequestParam());
                }
            }
        } else if (builder.getHttpType() == RequestBuilder.HttpType.DEFAULT_POST) {
            if (isCache) {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getWithoutHeaderApiService(RequestService.class, builder).getObservableWithQueryMapByPostWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getApiService(RequestService.class, builder).getObservableWithQueryMapByPost(builder.getUrl(), builder.getRequestParam());
                }

            } else {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getNoCacheAndWithoutHeadersApiService(RequestService.class, builder).getObservableWithQueryMapByPostWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getNoCacheApiService(RequestService.class, builder).getObservableWithQueryMapByPost(builder.getUrl(), builder.getRequestParam());
                }
            }
        } else if (builder.getHttpType() == RequestBuilder.HttpType.FIELDMAP_POST) {
            if (isCache) {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getWithoutHeaderApiService(RequestService.class, builder).getObservableWithFieldMapWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getApiService(RequestService.class, builder).getObservableWithFieldMap(builder.getUrl(), builder.getRequestParam());
                }
            } else {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getNoCacheAndWithoutHeadersApiService(RequestService.class, builder).getObservableWithFieldMapWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getNoCacheApiService(RequestService.class, builder).getObservableWithFieldMap(builder.getUrl(), builder.getRequestParam());
                }
            }
        } else if (builder.getHttpType() == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST) {
            if (isCache) {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getWithoutHeaderApiService(RequestService.class, builder).uploadFileWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getParts(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getApiService(RequestService.class, builder).uploadFile(builder.getUrl(), builder.getRequestParam(), builder.getParts());
                }
            } else {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getNoCacheAndWithoutHeadersApiService(RequestService.class, builder).uploadFileWithHeaders(builder.getUrl(), builder.getRequestParam(), builder.getParts(), builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getNoCacheApiService(RequestService.class, builder).uploadFile(builder.getUrl(), builder.getRequestParam(), builder.getParts());
                }
            }
        } else if (builder.getHttpType() == RequestBuilder.HttpType.JSON_PARAM_POST) {

            Set set = builder.getRequestParam().keySet();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {

                String key = (String) iter.next();
                stringBuilder.append("\"");
                stringBuilder.append(key);
                stringBuilder.append("\"");
                stringBuilder.append(":");

                if (builder.getRequestParam().get(key) != null &&
                        !builder.getRequestParam().get(key).toString().equals("") && (
                        (builder.getRequestParam().get(key).toString().charAt(0) == '[' && (builder.getRequestParam().get(key).toString().charAt(builder.getRequestParam().get(key).toString().length() - 1) == ']')) ||
                                (builder.getRequestParam().get(key).toString().charAt(0) == '{' && (builder.getRequestParam().get(key).toString().charAt(builder.getRequestParam().get(key).toString().length() - 1) == '}')))) {
                    stringBuilder.append(builder.getRequestParam().get(key));
                } else {
                    stringBuilder.append("\"");
                    stringBuilder.append(builder.getRequestParam().get(key));
                    stringBuilder.append("\"");
                }
                stringBuilder.append(",");
            }
            String str = stringBuilder.toString();
            String json = str.substring(0, str.length() - 1);
            json = json + "}";

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), json);
            if (isCache) {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getWithoutHeaderApiService(RequestService.class, builder).getObservableWithQueryJsonParamWithHeaders(builder.getUrl(), body, builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getApiService(RequestService.class, builder).getObservableWithQueryJsonParam(builder.getUrl(), body);
                }
            } else {
                if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                    return RetrofitManager.Companion.getNoCacheAndWithoutHeadersApiService(RequestService.class, builder).getObservableWithQueryJsonParamWithHeaders(builder.getUrl(), body, builder.getHeaders());
                } else {
                    return RetrofitManager.Companion.getNoCacheApiService(RequestService.class, builder).getObservableWithQueryJsonParam(builder.getUrl(), body);
                }
            }
        } else if (builder.getHttpType() == RequestBuilder.HttpType.DOWNLOAD_FILE_GET) {
            Map<String, String> header = new HashMap<>();
            header.put("Accept-Encoding", "identity");
            return RetrofitManager.Companion.getDownloadApiService(RequestService.class, builder).downloadFile(builder.getUrl());
        }
        return null;
    }


    /**
     * ============================自定义的缓存机制网络+本地json缓存===========================================
     */

    /**
     * 设置缓存时间，没超过设置的时间不请求网络，只返回缓存数据
     * 返回List
     */

    @SuppressLint("CheckResult")
    public <T> DisposableObserver<ResponseBody> loadFormDiskResultListLimitTime(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {


        if (!FileUtils.isCacheDataFailure(builder.getFilePath() + "/" + builder.getFileName(), builder.getLimtHours())) {

            Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                    String json = FileUtils.ReadTxtFile(builder.getFilePath() + "/" + builder.getFileName());
                    T a;
                    if (builder.isReturnOriginJson()) {
                        a = (T) json;
                    } else if (Config.Companion.getMClASS() == null) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else if (!builder.isUserCommonClass()) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else {
                        a = GsonUtils.fromJsonArray(json, builder.getTransformClass());
                    }

                    builder.getRxObservableListener().onNext(a);
                }
            }).subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {

                }
            });

            return null;
        } else {

            final String[] json = new String[1];
            DisposableObserver<ResponseBody> observer = observable.compose(RxSchedulers.<ResponseBody>io_main())
                    .doOnNext(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            json[0] = responseBody.string();
                            FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
                        }
                    })
                    .subscribeWith(new RxSubscriber<ResponseBody>() {
                        @Override
                        public void _onNext(final ResponseBody t) {
                            T a;
                            if (builder.isReturnOriginJson()) {
                                a = (T) json[0];
                            } else if (Config.Companion.getMClASS() == null) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else if (!builder.isUserCommonClass()) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else {
                                a = GsonUtils.fromJsonArray(json[0], builder.getTransformClass());
                            }
                            builder.getRxObservableListener().onNext(a);
                        }

                        @Override
                        public void _onError(NetWorkCodeException.ResponseThrowable e) {
                            builder.getRxObservableListener().onError(e);
                        }

                        @Override
                        public void _onComplete() {
                        }
                    });

            return observer;
        }
    }

    /**
     * 设置缓存时间，没超过设置的时间不请求网络，只返回缓存数据
     * 返回Model
     */
    @SuppressLint("CheckResult")
    public <T> DisposableObserver<ResponseBody> loadFormDiskModeLimitTime(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        if (!FileUtils.isCacheDataFailure(builder.getFilePath() + "/" + builder.getFileName(), builder.getLimtHours())) {

            Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                    String json = FileUtils.ReadTxtFile(builder.getFilePath() + "/" + builder.getFileName());
                    T a;
                    if (builder.isReturnOriginJson()) {
                        a = (T) json;
                    } else if (Config.Companion.getMClASS() == null) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else if (!builder.isUserCommonClass()) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else {
                        a = GsonUtils.fromJsonObject(json, builder.getTransformClass());
                    }
                    builder.getRxObservableListener().onNext(a);

                }
            }).subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {

                }
            });

            return null;
        }

        final String[] json = new String[1];
        DisposableObserver<ResponseBody> observer = observable.compose(RxSchedulers.<ResponseBody>io_main())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        json[0] = responseBody.string();
                        FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
                    }
                })
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(final ResponseBody t) {
                        T a;
                        if (builder.isReturnOriginJson()) {
                            a = (T) json[0];
                        } else if (Config.Companion.getMClASS() == null) {
                            a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                        } else if (!builder.isUserCommonClass()) {
                            a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                        } else {
                            a = GsonUtils.fromJsonObject(json[0], builder.getTransformClass());
                        }
                        builder.getRxObservableListener().onNext(a);
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        builder.getRxObservableListener().onError(e);
                    }

                    @Override
                    public void _onComplete() {
                    }
                });

        return observer;
    }

    /**
     * 没有网络再请求缓存
     * 返回List
     */
    private <T> DisposableObserver<ResponseBody> loadNoNetWorkWithCacheResultList(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        final Observable<T> observableC = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                String json = FileUtils.ReadTxtFile(builder.getFilePath() + "/" + builder.getFileName());
                if (!TextUtils.isEmpty(json) && !json.equals("")) {
                    T a;
                    if (builder.isReturnOriginJson()) {
                        a = (T) json;
                    } else if (Config.Companion.getMClASS() == null) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else if (!builder.isUserCommonClass()) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else {
                        a = GsonUtils.fromJsonArray(json, builder.getTransformClass());
                    }
                    emitter.onNext(a);

                } else {
                    NetWorkCodeException.ResponseThrowable e = new NetWorkCodeException.ResponseThrowable();
                    e.setCode(NetWorkCodeException.Companion.getNETWORD_ERROR());
                    e.setErrorMessage("网络错误");
                    builder.getRxObservableListener().onError(e);
                }
            }
        });

        final String[] json = new String[1];
        DisposableObserver<ResponseBody> observer = observable
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        json[0] = responseBody.string();
                        FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
                    }
                })
                .compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(ResponseBody t) {
                        T a;
                        if (builder.isReturnOriginJson()) {
                            a = (T) json[0];
                        } else if (Config.Companion.getMClASS() == null) {
                            a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                        } else if (!builder.isUserCommonClass()) {
                            a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                        } else {
                            a = GsonUtils.fromJsonArray(json[0], builder.getTransformClass());
                        }
                        builder.getRxObservableListener().onNext(a);

                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        LogUtils.info("1000", "_onError");
                        observableC.subscribe(new Consumer<T>() {
                            @Override
                            public void accept(T t) throws Exception {
                                builder.getRxObservableListener().onNext(t);
                            }
                        });
                    }

                    @Override
                    public void _onComplete() {
                    }
                });

        return observer;
    }


    /**
     * 没有网络再请求缓存
     * 返回Model
     */
    private <T> DisposableObserver<ResponseBody> loadNoNetWorkWithCacheModel(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        final Observable<T> observableC = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                String json = FileUtils.ReadTxtFile(builder.getFilePath() + "/" + builder.getFileName());
                if (!TextUtils.isEmpty(json) && !json.equals("")) {
                    T a;
                    if (builder.isReturnOriginJson()) {
                        a = (T) json;
                    } else if (Config.Companion.getMClASS() == null) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else if (!builder.isUserCommonClass()) {
                        a = GsonUtils.fromJsonNoCommonClass(json, builder.getTransformClass());
                    } else {
                        a = GsonUtils.fromJsonObject(json, builder.getTransformClass());
                    }
                    emitter.onNext(a);//返回数据不继续执行

                } else {
                    NetWorkCodeException.ResponseThrowable e = new NetWorkCodeException.ResponseThrowable();
                    e.setCode(NetWorkCodeException.Companion.getNETWORD_ERROR());
                    e.setErrorMessage("网络错误");
                    builder.getRxObservableListener().onError(e);
                }
            }
        });

        final String[] json = new String[1];

        DisposableObserver<ResponseBody> observer = observable.doOnNext(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody t) throws Exception {
                json[0] = t.string();
                FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
            }
        }).compose(RxSchedulers.<ResponseBody>io_main()).subscribeWith(new RxSubscriber<ResponseBody>() {
            @Override
            public void _onNext(ResponseBody t) {
                T a;
                if (builder.isReturnOriginJson()) {
                    a = (T) json[0];
                } else if (Config.Companion.getMClASS() == null) {
                    a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                } else if (!builder.isUserCommonClass()) {
                    a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                } else {
                    a = GsonUtils.fromJsonObject(json[0], builder.getTransformClass());
                }
                builder.getRxObservableListener().onNext(a);
            }

            @Override
            public void _onError(NetWorkCodeException.ResponseThrowable e) {
                observableC.subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        builder.getRxObservableListener().onNext(t);
                    }
                });
            }

            @Override
            public void _onComplete() {
            }
        });

        return observer;
    }

    /**
     * 把结果保存到本地，根据标志是否返回数据，如果本地存在则不需要下载
     */
    private <T> DisposableObserver<ResponseBody> loadOnlyNetWorkSaveList(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        if (FileUtils.checkFileExists(builder.getFilePath() + "/" + builder.getFileName())) { // 已经在SD卡中存在
            return null;
        }

        final String[] json = new String[1];
        DisposableObserver<ResponseBody> observer = observable.doOnNext(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody t) throws Exception {
                json[0] = t.string();
                FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
            }
        }).compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(ResponseBody t) {
                        if (builder.isDiskCacheNetworkSaveReturn() == true) {
                            T a;
                            if (builder.isReturnOriginJson()) {
                                a = (T) json[0];
                            } else if (Config.Companion.getMClASS() == null) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else if (!builder.isUserCommonClass()) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else {
                                a = GsonUtils.fromJsonArray(json[0], builder.getTransformClass());
                            }

                            builder.getRxObservableListener().onNext(a);

                        }
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        if (builder.isDiskCacheNetworkSaveReturn()) {
                            builder.getRxObservableListener().onError(e);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (builder.isDiskCacheNetworkSaveReturn()) {
                        }
                    }
                });
        return observer;
    }


    /**
     * 把结果保存到本地，根据标志是否返回数据，如果本地存在则不需要下载,返回Model
     */
    private <T> DisposableObserver<ResponseBody> loadOnlyNetWorkSaveModel(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        if (FileUtils.checkFileExists(builder.getFilePath() + "/" + builder.getFileName())) { // 已经在SD卡中存在
            return null;
        }

        final String[] json = new String[1];
        DisposableObserver<ResponseBody> observer = observable.doOnNext(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody t) throws Exception {
                json[0] = t.string();
                FileUtils.WriterTxtFile(builder.getFilePath(), builder.getFileName(), json[0], false);
            }
        }).compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(ResponseBody t) {
                        if (builder.isDiskCacheNetworkSaveReturn() == true) {
                            T a;
                            if (builder.isReturnOriginJson()) {
                                a = (T) json[0];
                            } else if (Config.Companion.getMClASS() == null) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else if (!builder.isUserCommonClass()) {
                                a = GsonUtils.fromJsonNoCommonClass(json[0], builder.getTransformClass());
                            } else {
                                a = GsonUtils.fromJsonObject(json[0], builder.getTransformClass());
                            }

                            builder.getRxObservableListener().onNext(a);
                        }
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        if (builder.isDiskCacheNetworkSaveReturn() == true) {
                            builder.getRxObservableListener().onError(e);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (builder.isDiskCacheNetworkSaveReturn() == true) {
                        }
                    }
                });
        return observer;
    }

    /**
     * 只通过网络返回数据，返回list
     */
    private <T> DisposableObserver<ResponseBody> loadOnlyNetWorkList(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {
        DisposableObserver<ResponseBody> observer = observable.compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(ResponseBody t) {
                        try {
                            T a;
                            if (builder.isReturnOriginJson()) {
                                a = (T) t.string();
                            } else if (Config.Companion.getMClASS() == null) {
                                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
                            } else if (!builder.isUserCommonClass()) {
                                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
                            } else {
                                a = GsonUtils.fromJsonArray(t.string(), builder.getTransformClass());
                            }
                            builder.getRxObservableListener().onNext(a);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        builder.getRxObservableListener().onError(e);
                    }

                    @Override
                    public void _onComplete() {
                    }
                });
        return observer;
    }


    /**
     * 只通过网络返回数据，返回Model
     */
    private <T> DisposableObserver<ResponseBody> loadOnlyNetWorkModel(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {

        DisposableObserver<ResponseBody> observer = observable.compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @Override
                    public void _onNext(ResponseBody t) {
                        try {
                            T a;
                            if (builder.isReturnOriginJson()) {
                                a = (T) t.string();
                            } else if (Config.Companion.getMClASS() == null) {
                                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
                            } else if (!builder.isUserCommonClass()) {
                                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
                            } else {
                                a = GsonUtils.fromJsonObject(t.string(), builder.getTransformClass());
                            }
                            builder.getRxObservableListener().onNext(a);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        builder.getRxObservableListener().onError(e);
                    }

                    @Override
                    public void _onComplete() {
                    }
                });
        return observer;
    }


    /****
     * 下载文件
     */

    private <T> DisposableObserver<ResponseBody> downloadFile(final RequestBuilder<T> builder, Observable<ResponseBody> observable) {
        DisposableObserver<ResponseBody> observer = observable.compose(RxSchedulers.<ResponseBody>io_main())
                .subscribeWith(new RxSubscriber<ResponseBody>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void _onNext(final ResponseBody t) {

                        ThreadPoolManager.Companion.getSingleInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                DownloadFileHelper.writeFile2Disk(t, builder);
                            }
                        });
                    }

                    @Override
                    public void _onError(NetWorkCodeException.ResponseThrowable e) {
                        builder.getRxObservableListener().onError(e);
                    }

                    @Override
                    public void _onComplete() {

                    }
                });

        return observer;
    }


    /**
     * ===============================Retrofit+OkHttp的缓存机制=========================================
     */

    public static Interceptor getInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);
                cacheBuilder.maxStale(365, TimeUnit.DAYS);
                CacheControl cacheControl = cacheBuilder.build();

                Request request = chain.request();
                if (!NetworkUtils.isNetworkConnected(Config.Companion.getCONTEXT())) {
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetworkUtils.isNetworkConnected(Config.Companion.getCONTEXT())) {
                    int maxAge = 0; // read from cache
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public ,max-age=" + maxAge)
                            .build();
                } else {
                    long maxStale = Config.Companion.getMAX_CACHE_SECONDS(); // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };

        return interceptor;
    }
}
