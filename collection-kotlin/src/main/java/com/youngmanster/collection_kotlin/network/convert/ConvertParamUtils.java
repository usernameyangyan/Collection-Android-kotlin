package com.youngmanster.collection_kotlin.network.convert;

import com.youngmanster.collection_kotlin.config.Config;
import com.youngmanster.collection_kotlin.network.RequestBuilder;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * Created by yangy
 * 2020/9/17
 * Describe:
 */
public class ConvertParamUtils {

    /**
     * params 转化成Json格式
     * @param builder
     * @param <T>
     * @return
     */
    public static <T> String convertParamToJson(RequestBuilder<T> builder) {
        String json="";

        if(builder.getNoKeyParam()!=null){
            json=builder.getNoKeyParam().toString();
        }else{
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
                        (builder.getRequestParam().get(key).toString().charAt(0) == '['
                                && (builder.getRequestParam().get(key).toString().charAt(builder.getRequestParam().get(key).toString().length() - 1) == ']')) ||
                                (builder.getRequestParam().get(key).toString().charAt(0) == '{'
                                        && (builder.getRequestParam().get(key).toString().charAt(builder.getRequestParam().get(key).toString().length() - 1) == '}')))) {
                    stringBuilder.append(builder.getRequestParam().get(key));
                } else {
                    Object value=builder.getRequestParam().get(key);
                    if(value instanceof Integer){
                        stringBuilder.append(value);
                    }else if(value instanceof Boolean){
                        stringBuilder.append(value);
                    }else if(value instanceof Long){
                        stringBuilder.append(value);
                    } else if(value instanceof Float){
                        stringBuilder.append(value);
                    }else if(value instanceof Double){
                        stringBuilder.append(value);
                    }else{
                        stringBuilder.append("\"");
                        stringBuilder.append(value);
                        stringBuilder.append("\"");
                    }
                }
                stringBuilder.append(",");
            }
            String str = stringBuilder.toString();
            json = str.substring(0, str.length() - 1);
            json = json + "}";

            if (json.equals("}")) {
                json = "{" + json;
            }
        }



        return json;

    }

    /**
     * 转化成list
     * @param builder
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T body2ArrayResult(RequestBuilder<T> builder, ResponseBody t) {
        try {
            T a;
            if (builder.isReturnOriginJson()) {
                a = (T) t.string();
            } else if (Config.Companion.getMClASS() == null) {
                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
            } else if (!builder.isUseCommonClass()) {
                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
            } else {
                a = GsonUtils.fromJsonArray(t.string(), builder.getTransformClass());
            }

            return a;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T json2ArrayResult(RequestBuilder<T> builder, String s) {
        T a;
        if (builder.isReturnOriginJson()) {
            a = (T) s;
        } else if (Config.Companion.getMClASS() == null) {
            a = GsonUtils.fromJsonNoCommonClass(s, builder.getTransformClass());
        } else if (!builder.isUseCommonClass()) {
            a = GsonUtils.fromJsonNoCommonClass(s, builder.getTransformClass());
        } else {
            a = GsonUtils.fromJsonArray(s, builder.getTransformClass());
        }

        return a;
    }


    /**
     * 转化成Model
     * @param builder
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T body2ObjectResult(RequestBuilder<T> builder, ResponseBody t) {
        try {
            T a;
            if (builder.isReturnOriginJson()) {
                a = (T) t.string();
            } else if (Config.Companion.getMClASS() == null) {
                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
            } else if (!builder.isUseCommonClass()) {
                a = GsonUtils.fromJsonNoCommonClass(t.string(), builder.getTransformClass());
            } else {
                a = GsonUtils.fromJsonObject(t.string(), builder.getTransformClass());
            }

            return a;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T json2ObjectResult(RequestBuilder<T> builder, String s) {
        T a;
        if (builder.isReturnOriginJson()) {
            a = (T) s;
        } else if (Config.Companion.getMClASS() == null) {
            a = GsonUtils.fromJsonNoCommonClass(s, builder.getTransformClass());
        } else if (!builder.isUseCommonClass()) {
            a = GsonUtils.fromJsonNoCommonClass(s, builder.getTransformClass());
        } else {
            a = GsonUtils.fromJsonObject(s, builder.getTransformClass());
        }

        return a;
    }
}
