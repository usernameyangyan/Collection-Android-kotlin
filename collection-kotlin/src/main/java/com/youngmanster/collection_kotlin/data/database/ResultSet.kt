package com.youngmanster.collection_kotlin.data.database

import java.io.Serializable
import java.util.*

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */
class ResultSet:Serializable{
    private  val serialVersionUID = 2510654675439416448L

    private val nameValueMap = LinkedHashMap<String, Any?>()

    private val indexValueMap = LinkedHashMap<Int, Any?>()

    private val columnNameList = ArrayList<String>()

    private var index = 0


    internal fun setValue(columnName: String, columnValue: Any?) {
        var column = columnName
        column = column.toLowerCase()
        columnNameList.add(column)
        nameValueMap[column] = columnValue
        indexValueMap[index++] = columnName
    }

    fun changeValue(index: Int, value: Any) {
        if (indexValueMap.containsKey(index)) {
            indexValueMap[index] = value
            nameValueMap[columnNameList[index]] = value
        }
    }

    fun getValue(columnName: String): Any? {
        return nameValueMap[columnName.toLowerCase()]
    }

    fun getBooleanValue(columnName: String): Boolean {
        val value = getValue(columnName) ?: return false
        val strVal = value.toString().toLowerCase()
        if (strVal == "true" || strVal == "1") {
            return true
        } else if (strVal == "false" || strVal == "0") {
            return false
        }
        throw ClassCastException(String.format("invalid boolean value : %s ", value))
    }


    /**
     *
     * get long type value
     *
     * @param columnName
     */
    fun getLongValue(columnName: String): Long {
        return getDoubleValue(columnName).toLong()
    }

    /**
     *
     * get int type value
     *
     * @param columnName
     */
    fun getIntValue(columnName: String): Int {
        return getLongValue(columnName).toInt()
    }

    /**
     *
     * get short type value
     *
     * @param columnName
     */
    fun getShortValue(columnName: String): Short {
        return getIntValue(columnName).toShort()
    }

    /**
     *
     * get float type vlue
     *
     * @param columnName
     */
    fun getFloatValue(columnName: String): Float {
        return getDoubleValue(columnName).toFloat()
    }

    /**
     * get double type value
     *
     * @param columnName
     * @return null return 0
     * @exception ClassCastException
     */
    fun getDoubleValue(columnName: String): Double {
        val value = getValue(columnName)
        if (value == null) {
            return 0.0
        } else if (value is Double) {
            return (value as Double?)!!
        } else if (value is Float) {
            return (value as Float?)!!.toDouble()
        } else if (value is Long) {
            return (value as Long?)!!.toDouble()
        } else if (value is Int) {
            return (value as Int?)!!.toDouble()
        } else if (value is Short) {
            return (value as Short?)!!.toDouble()
        } else if (value is String) {
            if (isNum((value as String?)!!)) {
                return java.lang.Double.parseDouble((value as String?)!!)
            }
        }
        throw ClassCastException(String.format("invalid number %s ", value))
    }

    /**
     *
     * get String type value
     *
     * @param columnName
     */
    fun getStringValue(columnName: String): String? {
        val value = getValue(columnName)
        return value?.toString()
    }

    /**
     * get Date type value
     * @param columnName
     * @return
     */
    fun getDateValue(columnName: String): Date? {
        val value = getStringValue(columnName)
        return if (value == null) null else DateUtils.parseStr2Date(value)
    }

    /**
     * get byte[] type value
     * @param columnName
     * @return
     */
    fun getBlobValue(columnName: String): ByteArray {
        return getValue(columnName) as ByteArray
    }

    /**
     * get value by index
     *
     * @param columnIndex
     * @return
     */
    fun getValue(columnIndex: Int): Any? {
        return indexValueMap[columnIndex]
    }

    /**
     * the size of columns
     * @return
     */
    fun getSize(): Int {
        return nameValueMap.size
    }

    /**
     * no result
     * @return
     */
    fun isEmpty(): Boolean {
        return nameValueMap.isEmpty()
    }

    /**
     * get column name by its index
     * @param columnNum
     * @return
     */
    fun getColumnName(columnNum: Int): String {
        return columnNameList[columnNum]
    }

    /**
     * get index of this column name
     * @param columnName
     * @return if the column name didn't exsits, return -1
     */
    fun indexOfColumnName(columnName: String): Int {
        return columnNameList.indexOf(columnName.toLowerCase())
    }

    override fun toString(): String {
        return "Result [nameValueMap=$nameValueMap]"
    }

    /**
     * is this str a valid number
     *
     * @param str
     */
    private fun isNum(str: String): Boolean {
        return str != "" && str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$".toRegex())
    }

}