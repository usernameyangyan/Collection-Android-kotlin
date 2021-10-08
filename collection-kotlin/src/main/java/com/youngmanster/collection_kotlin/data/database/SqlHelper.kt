package com.youngmanster.collection_kotlin.data.database

import android.content.ContentValues
import com.youngmanster.collection_kotlin.config.Config
import java.util.*

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class SqlHelper {

    companion object {

        val PREFS_TABLE_VERSION_KEY = "collection_library_prefs_versioins"
        /**
         * return table version
         * @param clazz
         * @return
         */
        fun getTableVersion(): Int {
            return Config.SQLITE_DB_VERSION
        }

        /**
         * get primary key
         *
         * @param clazz
         * @return
         */
        fun <T> getPrimaryKey(clazz: Class<T>): String? {
            val fields = clazz.declaredFields
            for (field in fields) {

                if (field.name.contains("$")) {
                    continue
                }

                if (!field.isAccessible)
                    field.isAccessible = true
                val column = field.getAnnotation(Column::class.java) ?: continue
                if (column.isPrimaryKey) {
                    return field.name
                }
            }
            return null
        }

        /**
         *
         * @param queryResultList
         * @param mList
         * @param mdlType
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> parseResultSetListToModelList(
            queryResultList: List<ResultSet>?, mList: ArrayList<T>, mdlType: Class<*>
        ) {
            try {
                if (queryResultList == null || queryResultList.isEmpty())
                    return
                for (queryResult in queryResultList) {
                    val model = mdlType.newInstance()
                    parseResultSetToModel(queryResult, model)
                    mList.add(model as T)
                }
            } catch (ex: IllegalAccessException) {
                ex.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }

        }

        /**
         * use reflection to parse queryResult's value into model
         * @param queryResult
         * @param model
         */
        fun parseResultSetToModel(
            queryResult: ResultSet,
            model: Any
        ) {
            val clazz = model.javaClass
            val fields = clazz.declaredFields

            var fieldType: Class<*>?
            try {
                for (field in fields) {

                    if (field.name.contains("$")) {
                        continue
                    }

                    if (!field.isAccessible)
                        field.isAccessible = true
                    fieldType = field.type
                    if (fieldType == Short::class.javaPrimitiveType || fieldType == Short::class.java ||
                        fieldType == Int::class.javaPrimitiveType || fieldType == Int::class.java
                        || field.type.name == "java.lang.Integer" || field.type.name == "java.lang.Short"
                    ) {

                        field.set(model, queryResult.getIntValue(field.name))
                    } else if (fieldType == Long::class.javaPrimitiveType || fieldType == Long::class.java
                        || field.type.name == "java.lang.Long"
                    ) {
                        field.setLong(
                            model,
                            queryResult.getLongValue(field.name)
                        )
                    } else if (fieldType == Float::class.javaPrimitiveType || fieldType == Float::class.java ||
                        field.type.name == "java.lang.Float"
                    ) {
                        field.setFloat(
                            model,
                            queryResult.getFloatValue(field.name)
                        )
                    } else if (fieldType == Double::class.javaPrimitiveType || fieldType == Double::class.java ||
                        field.type.name == "java.lang.Double"
                    ) {
                        field.setDouble(
                            model,
                            queryResult.getDoubleValue(field.name)
                        )
                    } else if (fieldType == Boolean::class.javaPrimitiveType || fieldType == Boolean::class.java ||
                        field.type.name == "java.lang.Boolean"
                    ) {

                        field.setBoolean(
                            model,
                            queryResult.getBooleanValue(field.name)
                        )
                    } else if (fieldType == String::class.java || field.type.name == "java.lang.String") {
                        field.set(model, queryResult.getStringValue(field.name))
                    }
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }


        /**
         * 创建表
         */
        fun <T> createTable(clazz: Class<T>): String {
            val sqlBuidler = StringBuilder()
            sqlBuidler.append("CREATE TABLE IF NOT EXISTS ")

            sqlBuidler.append(getBeanName(clazz.name))
            sqlBuidler.append("(")
            val fields = clazz.declaredFields
            for (field in fields) {

                if (field.name.contains("$")) {
                    continue
                }

                if (!field.isAccessible)
                    field.isAccessible = true

                var column: Column? = null
                try {
                    column = field.getAnnotation(Column::class.java)
                } catch (e: Exception) {
                }

                sqlBuidler.append(field.name + " ")
                sqlBuidler.append(getColumType(field.type.name) + "")
                if (column?.isNull == false) {
                    sqlBuidler.append(" NOT NULL ")
                }

                if (column?.isPrimaryKey == true) {
                    sqlBuidler.append(" PRIMARY KEY ")
                }

                if (column?.isUnique == true) {
                    sqlBuidler.append(" UNIQUE ")
                }

                sqlBuidler.append(",")
            }

            sqlBuidler.deleteCharAt(sqlBuidler.lastIndexOf(","))
            sqlBuidler.append(")")

            return sqlBuidler.toString()
        }


        /**
         * 获取bean名
         */

        fun getBeanName(className: String): String {
            return className.split(".")[className.split(".").size - 1]
        }

        /**
         * 获取表属性类型
         */
        fun getColumType(clazz: String): String {
            var type = ""
            if (clazz == "java.lang.Integer" || clazz == "int" || clazz == "java.lang.Short" || clazz == "short") {
                type = "INTEGER"
            } else if (clazz == "double" || clazz == "java.lang.Double") {
                type = "REAL"
            } else if (clazz == "float" || clazz == "java.lang.Float") {
                type = "REAL"
            } else if (clazz == "boolean" || clazz == "java.lang.Boolean") {
                type = "INTEGER"
            } else if (clazz == "long" || clazz == "java.lang.Long") {
                type = "NUMERIC"
            } else if (clazz == "java.lang.String") {
                type = "TEXT"
            }

            return type
        }


        /**
         * use reflection to parse model's value to contentValues
         * @param model
         */
        fun parseModelToContentValues(
            model: Any?,
            contentValues: ContentValues
        ) {
            if (contentValues.size() > 0)
                contentValues.clear()

            val clazz = model!!.javaClass
            val fields = clazz.declaredFields

            var fieldType: Class<*>?

            for (field in fields) {

                if (field.name.contains("$")) {
                    continue
                }

                try {
                    if (!field.isAccessible)
                        field.isAccessible = true
                    fieldType = field.type

                    if (fieldType == Int::class.javaPrimitiveType || fieldType == Int::class.java || fieldType.name == "java.lang.Integer"
                        || fieldType == Short::class.javaPrimitiveType || fieldType == Short::class.java || fieldType.name == "java.lang.Short"
                    ) {
                        if(field.get(model)==null){
                            contentValues.put(field.name, 0)
                        }else{
                            contentValues.put(field.name, field.get(model) as Int)
                        }

                    } else if (fieldType == Long::class.javaPrimitiveType || fieldType == Long::class.java || fieldType.name == "java.lang.Long") {

                        if(field.get(model)==null){
                            contentValues.put(field.name, 0)
                        }else{
                            contentValues.put(field.name, field.get(model) as Long)
                        }
                    } else if (fieldType == Float::class.javaPrimitiveType || fieldType == Float::class.java || fieldType.name == "java.lang.Float") {
                        if(field.get(model)==null){
                            contentValues.put(field.name, 0)
                        }else{
                            contentValues.put(field.name, field.get(model) as Float)
                        }

                    } else if (fieldType == Double::class.java || fieldType == Double::class.javaPrimitiveType || fieldType.name == "java.lang.Double") {
                        if(field.get(model)==null){
                            contentValues.put(field.name, 0)
                        }else{
                            contentValues.put(field.name, field.get(model) as Double)
                        }
                    } else if (fieldType == Boolean::class.javaPrimitiveType || fieldType == Boolean::class.java || fieldType.name == "java.lang.Boolean") {
                        if(field.get(model)==null){
                            contentValues.put(field.name, "0")
                        }else{
                            if (field.getBoolean(model)) {
                                contentValues.put(field.name, "1")
                            } else {
                                contentValues.put(field.name, "0")
                            }
                        }


                    } else if (fieldType == String::class.java || fieldType.name == "java.lang.String") {
                        if(field.get(model)==null){
                            contentValues.put(field.name, "")
                        }else{
                            contentValues.put(field.name, field.get(model) as String)
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
        }


        /**
         * return sql of add a columm to table
         * @param table
         * @param columnInfo
         * @return
         */
        fun getAddColumnSql(table: String, columnInfo: ColumnInfo): String {
            val sbSql = StringBuilder()
            sbSql.append(
                String.format(
                    "ALTER TABLE %s ADD %s %s ",
                    table,
                    columnInfo.name,
                    columnInfo.type
                )
            )
            if (!columnInfo.isNull) {
                sbSql.append(" NOT NULL ")
            }
            if (columnInfo.isPrimaryKey) {
                sbSql.append(" PRIMARY KEY ")
            }

            if (columnInfo.isUnique) {
                sbSql.append(" UNIQUE ")
            }

            if (!columnInfo.defaultValue.equals("null")) {
                sbSql.append(" DEFAULT " + columnInfo.defaultValue)
            }

            sbSql.append(";")

            return sbSql.toString()
        }

    }


}