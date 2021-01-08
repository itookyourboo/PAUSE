package com.insaze.pause.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $BLOCKED_TABLE (" +
                    "id INTEGER PRIMARY KEY, " +
                    "packageName TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getBlockedPackages(): List<String> {
        val cursor = readableDatabase.query(
            BLOCKED_TABLE,
            arrayOf("id", "packageName"),
            null,
            null,
            null,
            null,
            null
        )
        val list = mutableListOf<String>()
        with(cursor) {
            while (moveToNext())
                list.add(getString(getColumnIndexOrThrow("packageName")))
        }

        return list
    }

    fun insert(packageName: String) {
        val values = ContentValues().apply {
            put("packageName", packageName)
        }
        writableDatabase.insert(BLOCKED_TABLE, null, values)
    }

    fun delete(packageName: String) {
        writableDatabase.delete(BLOCKED_TABLE, "packageName = ?", arrayOf(packageName))
    }

    fun isBlocked(packageName: String): Boolean {
        val cursor = readableDatabase.query(
            BLOCKED_TABLE,
            arrayOf("id", "packageName"),
            "packageName = ?",
            arrayOf(packageName),
            null,
            null,
            null
        )
        return cursor.moveToNext().also { cursor.close() }
    }

    companion object {
        const val DATABASE_NAME = "database.db"
        const val DATABASE_VERSION = 1
        const val BLOCKED_TABLE = "blocked_table"
    }
}