package com.youngmanster.collection_kotlin.network;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yangyan
 * on 2018/4/2.
 */

public interface RequestService {

    @GET
    Observable<ResponseBody> getObservableWithQueryMap(@Url String url, @QueryMap Map<String, Object> map);

    @GET
    Observable<ResponseBody> getObservableWithQueryMapWithHeaders(@Url String url, @QueryMap Map<String, Object> map, @HeaderMap Map<String, String> header);

    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Header("Range") String start, @Url String url);


    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> getObservableWithDefaultDelete(@Url String url);
    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> getObservableWithDefaultDeleteHeaders(@Url String url, @HeaderMap Map<String, String> headers);


    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> getObservableWithQueryJsonParamDelete(@Url String url, @Body RequestBody json);

    @HTTP(method = "DELETE", hasBody = true)
    Observable<ResponseBody> getObservableWithQueryJsonParamDeleteHeaders(@Url String url, @Body RequestBody json, @HeaderMap Map<String, String> headers);

    @POST
    Observable<ResponseBody> getObservableWithQueryJsonByPost(@Url String url, @Body RequestBody json);

    @POST
    Observable<ResponseBody> getObservableWithQueryJsonByPostWithHeaders(@Url String url, @Body RequestBody json, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> getObservableWithQueryMapByPost(@Url String url, @FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> getObservableWithQueryMapByPostWithHeaders(@Url String url, @FieldMap Map<String, Object> map, @HeaderMap Map<String, String> headers);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFileByPost(@Url String url, @QueryMap Map<String, Object> map,
                                              @Part() MultipartBody.Part[] images);
    @Multipart
    @POST
    Observable<ResponseBody> uploadFileByPostWithHeaders(@Url String url, @QueryMap Map<String, Object> map,
                                                         @Part() MultipartBody.Part[] images, @HeaderMap Map<String, String> headers);

    @Multipart
    @PUT
    Observable<ResponseBody> uploadFileByPut(@Url String url, @QueryMap Map<String, Object> map,
                                             @Part() MultipartBody.Part[] images);

    @Multipart
    @PUT
    Observable<ResponseBody> uploadFileByPutWithHeaders(@Url String url, @QueryMap Map<String, Object> map,
                                                        @Part() MultipartBody.Part[] images, @HeaderMap Map<String, String> headers);

    @PUT
    Observable<ResponseBody> getObservableWithQueryJsonByPut(@Url String url, @Body RequestBody json);


    @PUT
    Observable<ResponseBody> getObservableWithQueryJsonByPutWithHeaders(@Url String url, @Body RequestBody json, @HeaderMap Map<String, String> headers);


    @PATCH
    Observable<ResponseBody> getObservableWithQueryJsonByPatch(@Url String url, @Body RequestBody json);

    @PATCH
    Observable<ResponseBody> getObservableWithQueryJsonByPatchWithHeaders(@Url String url, @Body RequestBody json, @HeaderMap Map<String, String> headers);

}
