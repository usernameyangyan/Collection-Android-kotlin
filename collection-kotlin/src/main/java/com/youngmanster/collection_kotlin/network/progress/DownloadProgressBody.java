package com.youngmanster.collection_kotlin.network.progress;

import android.util.Log;

import com.youngmanster.collection_kotlin.network.RequestBuilder;
import com.youngmanster.collection_kotlin.utils.LogUtils;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by yangy
 * 2020/6/24
 * Describe:
 */
public class DownloadProgressBody extends ResponseBody {
    //实际的待包装响应体
    private  ResponseBody responseBody;
    //进度回调接口
    private RequestBuilder requestBuilder;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody     待包装的响应体
     */
    public DownloadProgressBody(ResponseBody responseBody, RequestBuilder requestBuilder) {
        this.responseBody = responseBody;
        this.requestBuilder = requestBuilder;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            float preValue=-1;
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }

                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                if(requestBuilder != null){
                    DecimalFormat fmt = new DecimalFormat("#0.00");
                    fmt.setRoundingMode(RoundingMode.DOWN);
                    String s = fmt.format((float) totalBytesRead /responseBody.contentLength());
                    float progress = Float.parseFloat(s);
                    if(preValue!=progress){
                        requestBuilder.getRxObservableListener().onDownloadProgress(contentLength, progress);
                        preValue=progress;
                    }
                }
                return bytesRead;
            }
        };
    }
}
