package com.mvp.annotation
import com.mvp.config.AnnotationConfig
import kotlin.reflect.KClass

/**
 * Created by yangy
 * 2020/9/24
 * Describe:
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MvpAnnotation(
    /**
     * 模块的功能名称，会根据这个名字生成对应的presenter 、view
     * @return
     */
    val prefixName: String,
    /**
     * 要生成的presenter类的父类
     * @return
     */
    val basePresenterClazz: KClass<*>,
    /**
     * 需要生成的view接口的父接口,是一个数组，可以继承多个其他接口
     * @return
     */
    val baseViewClazz: KClass<*>,

    /**
     * 选择生成java文件还是kotlin文件
     */
    val language:Int= AnnotationConfig.LANGUAGE_KOTLIN
)