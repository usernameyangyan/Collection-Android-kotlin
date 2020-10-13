package com.youngmanster.collection_kotlin.network.synchronization;

import com.youngmanster.collection_kotlin.config.Config;
import com.youngmanster.collection_kotlin.network.NetWorkCodeException;
import com.youngmanster.collection_kotlin.network.RequestBuilder;
import com.youngmanster.collection_kotlin.network.RetrofitManager;
import com.youngmanster.collection_kotlin.network.convert.GsonUtils;
import com.youngmanster.collection_kotlin.utils.NetworkUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yangy
 * 2019-12-14
 * Describe:
 */
public class OkHttpUtils {

    public static <T> void requestSyncData(RequestBuilder<T> requestBuilder) {
        try {
            OkHttpClient okHttpClient;
            Request.Builder builder = new Request.Builder();

            if ((requestBuilder.getHttpType() == RequestBuilder.HttpType.DEFAULT_GET ||
                    requestBuilder.getHttpType() == RequestBuilder.HttpType.DEFAULT_POST ||
                    requestBuilder.getHttpType() == RequestBuilder.HttpType.JSON_PARAM_POST||
                    requestBuilder.getHttpType() == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST) &&
                    (requestBuilder.getReqType() == RequestBuilder.ReqType.NO_CACHE_MODEL ||
                            requestBuilder.getReqType() == RequestBuilder.ReqType.NO_CACHE_LIST ||
                            requestBuilder.getReqType() == RequestBuilder.ReqType.DEFAULT_CACHE_MODEL ||
                            requestBuilder.getReqType() == RequestBuilder.ReqType.DEFAULT_CACHE_LIST)) {

                okHttpClient = RetrofitManager.Companion.getOkHttpClient(requestBuilder);

                String url;
                if (requestBuilder.getUrl().contains("https://") || requestBuilder.getUrl().contains("http://")) {
                    url = requestBuilder.getUrl();
                } else {
                    url = Config.Companion.getURL_DOMAIN() + requestBuilder.getUrl();
                }

                if (requestBuilder.getHttpType() == RequestBuilder.HttpType.DEFAULT_GET) {
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                            .newBuilder();
                    Set set = requestBuilder.getRequestParam().keySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        Object value = requestBuilder.getRequestParam().get(key);
                        urlBuilder.addQueryParameter(key, value.toString());
                    }
                    builder.url(urlBuilder.build()).get();

                } else if (requestBuilder.getHttpType() == RequestBuilder.HttpType.DEFAULT_POST) {
                    FormBody.Builder requestBody = new FormBody.Builder();
                    Set set = requestBuilder.getRequestParam().keySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        Object value = requestBuilder.getRequestParam().get(key);
                        requestBody.add(key, value.toString());
                    }
                    builder.post(requestBody.build());
                }else if(requestBuilder.getHttpType() == RequestBuilder.HttpType.MULTIPLE_MULTIPART_POST){

                    //上传图片需要 MultipartBody

                    MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

                    for(MultipartBody.Part part:requestBuilder.getParts()){
                        body.addPart(part);
                    }

                    Set set = requestBuilder.getRequestParam().keySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        Object value = requestBuilder.getRequestParam().get(key);
                        body.addFormDataPart(key,  value.toString());
                    }

                    RequestBody body1 = body.build();
                    builder.url(url)
                            .post(body1);

                } else {
                    String data = GsonUtils.getGsonWithoutExpose().toJson(requestBuilder.getRequestParam());
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, data);
                    builder.url(url)
                            .post(requestBody);
                }

                Request okRequest = builder.build();
                Response response = okHttpClient.newCall(okRequest).execute();

                if (response.code() == 200) {
                    String body = response.body().string();

                    T a;
                    if (requestBuilder.isReturnOriginJson()) {
                        a = (T) body;
                    } else if (Config.Companion.getMClASS() == null) {
                        a = GsonUtils.fromJsonNoCommonClass(body, requestBuilder.getTransformClass());
                    } else if (!requestBuilder.isUseCommonClass()) {
                        a = GsonUtils.fromJsonNoCommonClass(body, requestBuilder.getTransformClass());
                    } else {
                        if (requestBuilder.getReqType() == RequestBuilder.ReqType.NO_CACHE_MODEL ||
                                requestBuilder.getReqType() == RequestBuilder.ReqType.DEFAULT_CACHE_MODEL) {
                            a = GsonUtils.fromJsonObject(body, requestBuilder.getTransformClass());
                        } else {
                            a = GsonUtils.fromJsonArray(body, requestBuilder.getTransformClass());
                        }
                    }

                    requestBuilder.getRxObservableListener().onNext(a);

                } else {
                    NetWorkCodeException.ResponseThrowable e = new NetWorkCodeException.ResponseThrowable();
                    e.setCode(response.code());
                    e.setErrorMessage(response.message());
                    requestBuilder.getRxObservableListener().onError(e);
                }

            } else {
                NetWorkCodeException.ResponseThrowable e = new NetWorkCodeException.ResponseThrowable();
                e.setCode(1000);
                e.setErrorMessage("暂只支持DEFAULT_GET、DEFAULT_POST、JSON_PARAM_POST和MULTIPLE_MULTIPART_POST四种请求方式,支持NO_CACHE_MODEL、No_CACHE_LIST、DEFAULT_CACHE_MODEL以及DEFAULT_CACHE_LIST四种数据方式，其它的请求以及数据方式正在开发中");

            }
        } catch (IOException e) {
            NetWorkCodeException.ResponseThrowable e1 = new NetWorkCodeException.ResponseThrowable();
            if(!NetworkUtils.isNetworkConnected(Config.Companion.getCONTEXT())){
                e1.setCode(NetWorkCodeException.Companion.getNETWORD_ERROR());
                e1.setErrorMessage("网络错误");
            }else{
                e1.setCode(NetWorkCodeException.Companion.getHTTP_ERROR());
                e1.setErrorMessage("服务器异常");
            }


            requestBuilder.getRxObservableListener().onError(e1);
        }
    }
}
