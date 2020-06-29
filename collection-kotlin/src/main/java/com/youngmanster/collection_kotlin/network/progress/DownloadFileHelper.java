package com.youngmanster.collection_kotlin.network.progress;
import com.youngmanster.collection_kotlin.network.RequestBuilder;
import com.youngmanster.collection_kotlin.network.rx.utils.RxJavaUtils;
import com.youngmanster.collection_kotlin.network.rx.utils.RxUITask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by yangy
 * 2020-04-01
 * Describe:
 */
public class DownloadFileHelper {
    //将下载的文件写入本地存储
    public static void writeFile2Disk(ResponseBody responseBody, final RequestBuilder builder) {

        final String strFile = builder.getSaveDownloadFilePath() + "/" + builder.getSaveDownloadFileName();
        File file = new File(builder.getSaveDownloadFilePath());

        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(strFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream os = null;

        InputStream is = responseBody.byteStream(); //获取下载输入流

        try {
            os = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }

            RxJavaUtils.doInUIThread(new RxUITask<String>(strFile) {
                @Override
                public void doInUIThread(String s) {
                    builder.getRxObservableListener().onNext(s);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close(); //关闭输入流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
