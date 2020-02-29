package com.youngmanster.collection_kotlin.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class GlideUtils {

    companion object{
        /**
         * 处理Glide You cannot start a load for a destroyed activity问题
         *
         * @param context
         * @param url               网络图片
         * @param defaultLoadingImg 默认加载图片
         * @param imageView
         */
        fun loadImg(context: Context?, url: String?, defaultLoadingImg: Int, imageView: ImageView) {
            try {
                val options = RequestOptions()
                    .placeholder(defaultLoadingImg)
                    .error(defaultLoadingImg)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.HIGH)

                Glide.with(context!!)
                    .load(url)
                    .apply(options)
                    .into(imageView)


            } catch (exception: Exception) {

                return
            }

        }

        /**
         * Glide实现高斯模糊
         *
         * @param context
         * @param url
         * @param imageView
         */
        fun loadImgBlur(context: Context, url: String, defaultLoadingImg: Int, imageView: ImageView) {
            try {
                val options=RequestOptions
                    .bitmapTransform(BlurTransformation(40, 16))
                    .priority(Priority.HIGH)
                    .placeholder(defaultLoadingImg)
                    .error(defaultLoadingImg)

                Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(imageView)
            } catch (exception: Exception) {
                return
            }

        }

        fun loadImgBlur(
            context: Context,
            url: String,
            defaultLoadingImg: Int,
            imageView: ImageView,
            blurRadius: Int,
            blurSampling: Int
        ) {
            try {

                val options=RequestOptions.bitmapTransform(BlurTransformation(blurRadius, blurSampling))
                    .priority(Priority.HIGH)
                    .placeholder(defaultLoadingImg)
                    .error(defaultLoadingImg)

                Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(imageView)
            } catch (exception: Exception) {
                return
            }

        }
    }

}
