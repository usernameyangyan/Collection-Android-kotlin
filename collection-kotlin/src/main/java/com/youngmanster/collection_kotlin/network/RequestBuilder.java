package com.youngmanster.collection_kotlin.network;

import com.youngmanster.collection_kotlin.network.rx.RxObservableListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by yangyan
 * on 2018/4/2.
 */

public class RequestBuilder<T> {

    private ReqType reqType= ReqType.DEFAULT_CACHE_LIST;
    private ReqMode reqMode= ReqMode.ASYNCHRONOUS;
    private Class clazz;
    private String url;
    private String filePath;
    private String fileName;
    private String saveDownFilePath;
    private String saveDownFileName;
    private int limtHours=1;
    private boolean isUserCommonClass=true;
    private boolean isReturnOriginJson=false;
    private HttpType httpType= HttpType.DEFAULT_GET;
    private RxObservableListener<T> rxObservableListener;
    private Map<String, Object> requestParam;
    private MultipartBody.Part []parts;
    private boolean isDiskCacheNetworkSaveReturn;
    private Map<String, String> headers;

    public enum ReqType {
        DOWNLOAD_FILE_MODEL,
        //没有缓存
        NO_CACHE_MODEL,
        NO_CACHE_LIST,
        //默认Retrofit缓存
        DEFAULT_CACHE_MODEL,
        DEFAULT_CACHE_LIST,
        //自定义磁盘缓存，返回List
        DISK_CACHE_LIST_LIMIT_TIME,
        //自定义磁盘缓存，返回Model
        DISK_CACHE_MODEL_LIMIT_TIME,
        //自定义磁盘缓存，没有网络返回磁盘缓存，返回List
        DISK_CACHE_NO_NETWORK_LIST,
        //自定义磁盘缓存，没有网络返回磁盘缓存，返回Model
        DISK_CACHE_NO_NETWORK_MODEL,
        //保存网络数据到本地磁盘，可以设定网络请求是否返回数据
        DISK_CACHE_NETWORK_SAVE_RETURN_MODEL,
        DISK_CACHE_NETWORK_SAVE_RETURN_LIST,
    }

    public enum HttpType {
        DEFAULT_GET,
        DEFAULT_POST,
        JSON_PARAM_POST,
        FIELDMAP_POST,
        //多文件上传
        MULTIPLE_MULTIPART_POST,
        DOWNLOAD_FILE_GET,
    }

    public enum ReqMode{
        ASYNCHRONOUS, //异步
        SYNCHRONIZATION //同步
    }

    public RequestBuilder(RxObservableListener<T> rxObservableListener) {
        this.rxObservableListener = rxObservableListener;
        requestParam = new HashMap<>();
        headers=new HashMap<>();
    }


    public RequestBuilder setTransformClass(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public Class getTransformClass() {
        return clazz;
    }

    public RequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RequestBuilder setSaveDownloadFilePathAndFileName(String filePath, String fileName){
        this.saveDownFilePath=filePath;
        this.saveDownFileName=fileName;
        return this;
    }

    public String getSaveDownloadFilePath() {
        return saveDownFilePath;
    }

    public String getSaveDownloadFileName() {
        return saveDownFileName;
    }

    public RequestBuilder setFilePathAndFileName(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName=fileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public RequestBuilder setLimtHours(int limtHours) {
        this.limtHours = limtHours;
        return this;
    }

    public int getLimtHours() {
        return limtHours;
    }

    public RequestBuilder setParam(String key, Object object) {
        requestParam.put(key, object);
        return this;
    }

    public RequestBuilder setHeader(String key, String value){
        headers.put(key, value);
        return this;
    }


    public RequestBuilder setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestBuilder setRequestParam(Map<String, Object> requestParam) {
        this.requestParam.putAll(requestParam);
        return this;
    }

    public Map<String, Object> getRequestParam() {
        return requestParam;
    }

    public RequestBuilder setDiskCacheNetworkSaveReturn(boolean isDiskCacheNetworkSaveReturn) {
        this.isDiskCacheNetworkSaveReturn = isDiskCacheNetworkSaveReturn;
        return this;
    }

    public boolean isDiskCacheNetworkSaveReturn() {
        return isDiskCacheNetworkSaveReturn;
    }

    public RequestBuilder setHttpTypeAndReqType(HttpType httpType, ReqType reqType) {
        this.httpType = httpType;
        this.reqType = reqType;
        return this;
    }

    public HttpType getHttpType() {
        return httpType;
    }

    public ReqType getReqType() {
        return reqType;
    }


    public RequestBuilder setFilePaths(String key, String[]imagePaths) {

        parts =new MultipartBody.Part[imagePaths.length];
        for(int i=0;i<imagePaths.length;i++){
            File file = new File(imagePaths[i]);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
            parts[i]=part;
        }
        return this;
    }

    public MultipartBody.Part[] getParts() {
        return parts;
    }

    public boolean isUserCommonClass() {
        return isUserCommonClass;
    }

    public RequestBuilder setUserCommonClass(boolean userCommonClass) {
        isUserCommonClass = userCommonClass;
        return this;
    }

    public boolean isReturnOriginJson() {
        return isReturnOriginJson;
    }

    public RequestBuilder setReturnOriginJson(boolean returnOriginJson) {
        isReturnOriginJson = returnOriginJson;
        return this;
    }


    public ReqMode getReqMode() {
        return reqMode;
    }

    public RequestBuilder setReqMode(ReqMode reqMode) {
        this.reqMode = reqMode;
        return this;
    }

    public RxObservableListener<T> getRxObservableListener() {
        return rxObservableListener;
    }

}
