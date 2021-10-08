package com.youngmanster.collection_kotlin.data.database

import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.data.DataManager

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class SQLiteVersionMigrate {

    interface MigrateListener {
        fun onMigrate(oldVersion: Int, newVersion: Int)
    }

    fun setMigrateListener(migrate: MigrateListener) {

        val tableVersion = DataManager.DataForSharePreferences.getObject(
            SqlHelper.PREFS_TABLE_VERSION_KEY,
            0
        )

        if (Config.SQLITE_DB_VERSION > tableVersion!!) {
            migrate.onMigrate(tableVersion, Config.SQLITE_DB_VERSION)
        }

        DataManager.DataForSharePreferences.saveObject(SqlHelper.PREFS_TABLE_VERSION_KEY,Config.SQLITE_DB_VERSION)
    }
}