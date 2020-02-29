package com.youngmanster.collection_kotlin.data.database

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class DateUtils {

    companion object{
        private val DF_SSS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        private val DF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


        fun parseStr2Date(str: String): Date? {
            try {
                return if (str.endsWith(".SSS")) {
                    DF_SSS.parse(str)
                } else {
                    DF.parse(str)
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                return null
            }

        }

        fun formatDate2Str(date: Date): String {
            return DF_SSS.format(date)
        }
    }


}