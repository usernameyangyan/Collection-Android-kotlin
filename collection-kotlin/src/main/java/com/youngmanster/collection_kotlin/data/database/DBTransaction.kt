package com.youngmanster.collection_kotlin.data.database

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class DBTransaction {
    interface DBTransactionInterface {
        fun onTransact()
    }

    companion object{
        /**
         * executes sqls in a transction
         */
        fun transact(db: DbSQLite, transctionInterface: DBTransactionInterface?) {
            if (transctionInterface != null) {
                val sqliteDb = db.getSQLiteDatabase()
                sqliteDb?.beginTransaction()
                try {
                    transctionInterface.onTransact()
                    sqliteDb?.setTransactionSuccessful()
                } finally {
                    sqliteDb?.endTransaction()
                }
            }
        }
    }
}