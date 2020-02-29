package com.youngmanster.collection_kotlin.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.youngmanster.collection_kotlin.utils.LogUtils
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class DbSQLite {
    private var mContext: Context? = null
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var dbPath: String? = null

    /**
     * constructor would create or open the database
     * @param context
     * @param dbPath the path of db file
     */


    constructor(context: Context, db: SQLiteDatabase) {
        this.mContext = context
        this.mSQLiteDatabase = db
        this.dbPath = db.path
        openDB()
    }


    fun getSQLiteDatabase(): SQLiteDatabase? {
        return mSQLiteDatabase
    }

    internal fun getContext(): Context? {
        return mContext
    }

    fun update(
        table: String,
        values: ContentValues,
        whereClause: String?,
        whereArgs: Array<out String?>
    ): Int? {
        return try {
            openDB()
            mSQLiteDatabase?.update(table, values, whereClause, whereArgs)
        } catch (ex: Exception) {
            ex.printStackTrace()
            -1
        }

    }

    /**
     * insert a record
     *
     * @param table
     * @param values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    fun insert(table: String, values: ContentValues): Long? {
        return try {
            openDB()
            mSQLiteDatabase?.insertOrThrow(table, null, values)
        } catch (ex: Exception) {
            ex.printStackTrace()
            -1
        }

    }

    /**
     *
     * insert or replace a record by if its value of primary key has exsits
     *
     * @param table
     * @param values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    fun insertOrReplace(table: String, values: ContentValues): Long? {
        try {
            openDB()
            return mSQLiteDatabase?.replaceOrThrow(table, null, values)
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        }

    }

    /**
     * insert mutil records at one time
     * @param table
     * @param listVal
     * @return success return true
     */
    fun batchInsert(table: String, listVal: List<ContentValues>): Boolean {
        try {
            openDB()
            DBTransaction.transact(this, object : DBTransaction.DBTransactionInterface {
                override fun onTransact() {
                    for (contentValues in listVal) {
                        mSQLiteDatabase?.insertWithOnConflict(table, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE)
                    }
                }
            })
            return true
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        }

    }

    /**
     * delele by the condition
     *
     * @param table table the table to delete from
     * @param whereClause whereClause the optional WHERE clause to apply when deleting. Passing null will delete all rows.
     * @param whereArgs whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
     */
    fun delete(table: String, whereClause: String?, whereArgs: Array<out String?>): Int? {
        try {
            openDB()
            return mSQLiteDatabase?.delete(table, whereClause, whereArgs)
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        }

    }

    /**
     * a more flexible query by condition
     *
     * @param table The table name to compile the query against.
     * @param columns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
     * @param groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
     * @param selectionArgs selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
     * @return if exceptions happen or no match records, then return null
     */
    fun query(
        table: String,
        columns: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): List<ResultSet>? {
        var cursor: Cursor? = null
        try {
            openDB()
            cursor = mSQLiteDatabase?.query(
                table, columns, selection,
                selectionArgs, groupBy, having, orderBy
            )
            return if (cursor!!.count < 1) {
                null
            } else {
                val resultList = ArrayList<ResultSet>()
                parseCursorToResult(cursor, resultList)
                resultList
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        } finally {
            cursor?.close()
        }
    }

    /**
     * a simple query by condition
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @return
     */
    fun query(
        table: String, columns: Array<String>,
        selection: String, selectionArgs: Array<String>
    ): List<ResultSet>? {
        return query(table, columns, selection, selectionArgs, null, null, null)
    }

    /**
     * paging query
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy cann't be null if define page and pageSize
     * @param page first page is 1
     * @param pageSize
     * @return
     */
    fun pagingQuery(
        table: String,
        columns: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?,
        page: Int,
        pageSize: Int
    ): PagingList<ResultSet> {

        if (orderBy == null && pageSize != 0)
            throw SQLException("orderBy cann't be null if define page and pageSize")

        val orderWithLimit = if (orderBy != null && pageSize != 0) {
            String.format("%s LIMIT %s , %s", orderBy, (page - 1) * pageSize, pageSize)
        } else {
            orderBy
        }

        var cursor: Cursor? = null
        var totalCursor: Cursor? = null
        try {
            openDB()

            val resultList = PagingList<ResultSet>()

            totalCursor = mSQLiteDatabase?.query(
                table, arrayOf("count(*) as totalSize"), selection,
                selectionArgs, groupBy, having, null
            )

            if (totalCursor!!.moveToNext()) {
                val totalSize = totalCursor.getInt(0)
                resultList.setTotalSize(totalSize)
            }

            cursor = mSQLiteDatabase?.query(
                table, columns, selection,
                selectionArgs, groupBy, having, orderWithLimit
            )

            if (cursor!!.count < 1) {
                return resultList
            } else {
                parseCursorToResult(cursor, resultList)
                return resultList
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        } finally {
            cursor?.close()
            totalCursor?.close()
        }
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
     *
     * @param sql
     * @param bindArgs
     * @return
     */
    fun execSQL(sql: String, vararg bindArgs: Any): Boolean {
        try {
            openDB()
            mSQLiteDatabase?.execSQL(sql, bindArgs)
            return true
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        }

    }

    /**
     * execute raw query sql
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param bindArgs You may include ?s in where clause in the query, which will be replaced by the values from selectionArgs. The values will be bound as Strings.
     * @return return result as List or null
     */
    fun execQuerySQL(sql: String, vararg bindArgs: String): List<ResultSet>? {
        var cursor: Cursor? = null
        try {
            openDB()
            cursor = mSQLiteDatabase?.rawQuery(sql, bindArgs)
            if (cursor!!.count < 1) {
                return null
            }
            val resultList = ArrayList<ResultSet>()
            parseCursorToResult(cursor, resultList)
            return resultList
        } catch (ex: SQLException) {
            ex.printStackTrace()
            throw ex
        } finally {
            cursor?.close()
        }

    }

    /**
     * open database
     */
    fun openDB() {
        if (mSQLiteDatabase == null || mSQLiteDatabase?.isOpen == false) {
            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath!!, null)
        }
    }

    /**
     * close database
     */
    fun closeDB() {
        if (mSQLiteDatabase != null && mSQLiteDatabase?.isOpen!!) {
            mSQLiteDatabase?.close()
        }
    }


    fun getDbPath(): String? {
        return dbPath
    }

    /**
     * set data in cursor to ResultSet List
     * @param cursor
     * @param resultList the data will set in it
     */
    fun parseCursorToResult(cursor: Cursor, resultList: MutableList<ResultSet>) {
        var columnCount: Int
        var columnType: Int
        var columnVal: Any?
        while (cursor.moveToNext()) {
            columnCount = cursor.columnCount
            val result = ResultSet()
            for (index in 0 until columnCount) {
                columnType = cursor.getType(index)
                when (columnType) {
                    Cursor.FIELD_TYPE_BLOB -> columnVal = cursor.getBlob(index)
                    Cursor.FIELD_TYPE_FLOAT -> columnVal = cursor.getDouble(index)
                    Cursor.FIELD_TYPE_INTEGER -> columnVal = cursor.getLong(index)
                    Cursor.FIELD_TYPE_NULL -> columnVal = null
                    else -> columnVal = cursor.getString(index)
                }
                result.setValue(cursor.getColumnName(index), columnVal!!)
            }
            resultList.add(result)
        }
    }
}