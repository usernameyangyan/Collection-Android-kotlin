package com.mvp.processor

import com.mvp.annotation.MvpAnnotation
import com.mvp.config.AnnotationConfig
import jdk.nashorn.internal.objects.NativeMath
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.StringBuilder
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

@SupportedAnnotationTypes("com.mvp.annotation.MvpAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(value = ["mvp_create_package_path", "project_java_path"])
class MvpAnnotationProcessor : AbstractProcessor() {
    private var messager: Messager? = null
    private var mOptionSrc: String? = null
    private var PACKAGE_FORMAT = "package %s;\n\n"
    private var IMPORT_FORMAT = "import %s;\n"
    private var IMPORT_VIEW_FORMAT = "import %s.mvp.view%s.I%sView;\n"
    private var CLASS_FORMAT = "public class %s %s %s{\n"
    private var INTERFACE_FORMAT = "public interface %s %s{\n"

    companion object {
        private const val OPTION_PACKAGE_PATH = "mvp_create_package_path"
        private const val OPTION_PROJECT_JAVA_PATH = "project_java_path"
    }

    //预留导入包的位置
    private val IMPORT_PRE_EMPTY = "import empty\n"

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager


    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val packagePath = processingEnv.options[OPTION_PACKAGE_PATH]
        val projectJavaPath = processingEnv.options[OPTION_PROJECT_JAVA_PATH]
        if (packagePath == null || packagePath=="") {
            messager!!.printMessage(
                Diagnostic.Kind.ERROR,
                "No option " + OPTION_PACKAGE_PATH +
                        " passed to annotation processor.Please config in app gradle file"
            )
            return false
        }
        mOptionSrc = projectJavaPath


        val elements=
            roundEnvironment.getElementsAnnotatedWith(
                MvpAnnotation::class.java
            )
        for (element in elements) {
            val mvpAnnotation: MvpAnnotation =
                element.getAnnotation(MvpAnnotation::class.java)


            if(mvpAnnotation.language==AnnotationConfig.LANGUAGE_KOTLIN){
                PACKAGE_FORMAT=PACKAGE_FORMAT.replace(";","")
                IMPORT_FORMAT=IMPORT_FORMAT.replace(";","")
                IMPORT_VIEW_FORMAT=IMPORT_VIEW_FORMAT.replace(";","")
                CLASS_FORMAT=CLASS_FORMAT.replace("public ","")
                INTERFACE_FORMAT=INTERFACE_FORMAT.replace("public ","")
            }

            val prefixName: String = mvpAnnotation.prefixName
            if (checkPreNameValidate(prefixName, element.simpleName.toString())) {
                createBaseViewFile(mvpAnnotation, packagePath)
                createPresenterFile(mvpAnnotation, packagePath)
            }
        }
        return true
    }

    //检查名字是否合法
    private fun checkPreNameValidate(
        name: String,
        simpleName: String
    ): Boolean {
        if (name.isEmpty()) {
            NativeMath.log(
                "The value of name cann't be empty.location --> $simpleName",
                Diagnostic.Kind.ERROR
            )
            return false
        } else {
            val ch = name[0]
            if (!Character.isLetter(ch)) {
                NativeMath.log(
                    "$simpleName---> The value of @MvpFastDagger's name must be start with letters range in [a - z] or [A - Z].",
                    Diagnostic.Kind.ERROR
                )
                return false
            }
        }
        return true
    }

    //生成presenter文件
    private fun createPresenterFile(
        mvpAnnotation: MvpAnnotation,
        packagePath: String
    ) {
        //获取最后一个"."后面的内容，作为文件的名字
        val pointIndex: Int = mvpAnnotation.prefixName.lastIndexOf(".")
        val fileN: String = mvpAnnotation.prefixName.substring(pointIndex + 1)
        val newName = toUpperFirstChar(fileN)
        val fileName = newName + "Presenter"
        val middleName =
            if (pointIndex == -1) "" else "." + mvpAnnotation.prefixName
                .substring(0, pointIndex)
        val packageName = "$packagePath.mvp.presenter$middleName"
        val fileFullName = "$packageName.$fileName"
        //先检查对应的presenter文件是否存在
        if (isFileExist(fileFullName,mvpAnnotation.language)) {
            return
        }
        var typeMirror: TypeMirror? = null
        try {
            mvpAnnotation.basePresenterClazz
        } catch (ex: MirroredTypeException) {
            typeMirror = ex.typeMirror
        }
        var strExtend: String? = null
        if (typeMirror != null && typeMirror.toString() != Class::class.java.name) {
            strExtend = typeMirror.toString()
        } else { //将typeMirror置空
            typeMirror = null
        }
        val sb = StringBuilder()

        sb.append(String.format(Locale.CHINESE, PACKAGE_FORMAT, packageName))
            .append(
                String.format(
                    Locale.CHINESE,
                    IMPORT_VIEW_FORMAT,
                    packagePath,
                    middleName,
                    newName
                )
            )
        if (strExtend != null) {
            sb.append(String.format(Locale.CHINESE, IMPORT_FORMAT, strExtend))
        }

        var headStr="extends %s<I%sView>"
        if(mvpAnnotation.language==AnnotationConfig.LANGUAGE_KOTLIN){
            headStr=": %s<I%sView>()"
        }

        //插入预留导入包位置
        sb.append(IMPORT_PRE_EMPTY).append("\n")
        sb.append(
            String.format(
                Locale.CHINESE,
                CLASS_FORMAT,
                fileName,
                if (strExtend == null) "" else String.format(
                    Locale.CHINESE,
                    headStr,
                    strExtend.substring(strExtend.lastIndexOf(".") + 1),
                    newName,
                    newName
                ),
                ""
            )
        )
            .append("\n")
            .append("}")

        //删除预留包位置
        sb.replace(
            sb.indexOf(IMPORT_PRE_EMPTY),
            sb.indexOf(IMPORT_PRE_EMPTY) + IMPORT_PRE_EMPTY.length,
            ""
        )
        writeFileInSrc(fileFullName, sb.toString(),mvpAnnotation.language)
    }

    //生成BaseView接口
    private fun createBaseViewFile(
        mvpAnnotation: MvpAnnotation,
        packagePath: String
    ) {
        //获取最后一个"."后面的内容，作为文件的名字
        createInterfaceFile(mvpAnnotation, packagePath, "view")
    }

    //生成IView
    private fun createInterfaceFile(
        mvpAnnotation: MvpAnnotation,
        packagePath: String,
        type: String
    ) {
        var type = type
        if (!type.equals("view", ignoreCase = true)) {
            NativeMath.log(
                "The createInterfaceFile's param type must be view",
                Diagnostic.Kind.ERROR
            )
        }
        type = type.toLowerCase()
        //获取最后一个"."后面的内容，作为文件的名字
        val pointIndex: Int = mvpAnnotation.prefixName.lastIndexOf("/")
        val fileN: String = mvpAnnotation.prefixName.substring(pointIndex + 1)
        val newName = toUpperFirstChar(fileN)
        val fileName = "I" + newName + toUpperFirstChar(type)
        val middleName =
            if (pointIndex == -1) "" else "." + mvpAnnotation.prefixName
                .substring(0, pointIndex)
        val packageName = packagePath + ".mvp." + type.toLowerCase() + middleName
        val fileFullName = "$packageName.$fileName"
        if (isFileExist(fileFullName,mvpAnnotation.language)) {
            return
        }
        var typeMirrors: List<TypeMirror>? = null
        try {
            if (type.equals("view", ignoreCase = true)) {
                mvpAnnotation.baseViewClazz
            } else {
                mvpAnnotation.basePresenterClazz
            }
        } catch (ex: MirroredTypesException) {
            typeMirrors = ex.typeMirrors
        }
        val sb = StringBuilder()
        sb.append(String.format(Locale.CHINESE, PACKAGE_FORMAT, packageName))
        var sbExtend: StringBuilder? = null

        var headStr="extends "
        if(mvpAnnotation.language==AnnotationConfig.LANGUAGE_KOTLIN){
            headStr=":"
        }
        if (typeMirrors != null && typeMirrors.isNotEmpty()) {
            sbExtend = StringBuilder(headStr)
            for ((i, typeMirror) in typeMirrors.withIndex()) {
                sb.append(
                    String.format(
                        Locale.CHINESE,
                        IMPORT_FORMAT,
                        typeMirror.toString()
                    )
                )
                sbExtend.append(
                    typeMirror.toString().substring(typeMirror.toString().lastIndexOf(".") + 1)
                )
                if (i < typeMirrors.size - 1) {
                    sbExtend.append(",")
                } else {
                    sb.append("\n")
                }
            }
        }
        //插入预留导入包位置
        sb.append(IMPORT_PRE_EMPTY)
        sb.append(
            String.format(
                Locale.CHINESE,
                INTERFACE_FORMAT,
                fileName,
                sbExtend?.toString() ?: ""
            )
        )
            .append("\n")
            .append("}")
        //删除预留包位置
        sb.replace(
            sb.indexOf(IMPORT_PRE_EMPTY),
            sb.indexOf(IMPORT_PRE_EMPTY) + IMPORT_PRE_EMPTY.length,
            ""
        )
        writeFileInSrc(fileFullName, sb.toString(),mvpAnnotation.language)
    }

    private fun toUpperFirstChar(name: String): String {
        return if (name.length == 1) {
            name.toUpperCase()
        } else String.format(
            Locale.CHINESE,
            "%s%s",
            name.substring(0, 1).toUpperCase(),
            name.substring(1)
        )
    }

    /**
     * 检查要生成的文件是否存在，如果存在了就不去生成了，因为生成后会进行复制覆盖原来的文件，故需要检测文件是否存在
     * @param filePath
     * @return
     */
    private fun isFileExist(filePath: String,type:Int): Boolean {
        var filePath = filePath
        var suffixName=".java"
        if(type==AnnotationConfig.LANGUAGE_KOTLIN){
            suffixName=".kt"
        }
        filePath = filePath.replace(".", "/") + suffixName
        filePath = "$mOptionSrc/$filePath"
        val file = File(filePath)
        return file.exists()
    }

    /**
     * 在src目录下生成文件
     * @param fileName
     * @param content
     */
    private fun writeFileInSrc(fileName: String, content: String,type:Int) {
        var suffixName=".java"
        if(type==AnnotationConfig.LANGUAGE_KOTLIN){
            suffixName=".kt"
        }
        var filePath: String = fileName.replace(".", "/") + suffixName
        filePath = "$mOptionSrc/$filePath"
        val file = File(filePath)
        file.parentFile.mkdirs()
        try {
            val writer = FileWriter(file)
            writer.write(content)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
        }
    }
}