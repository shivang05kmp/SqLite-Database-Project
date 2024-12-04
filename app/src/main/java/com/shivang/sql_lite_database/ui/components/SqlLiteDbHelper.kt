package com.shivang.sql_lite_database.ui.components

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqlLiteDbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                AGE_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addName(name: String, age: String) {
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(AGE_COL, age)
        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getName(): Cursor? {
        val db = this.readableDatabase

        return db.query(
            TABLE_NAME, null, null, null,
            null, null, null
        )
//        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun updateData(id: Int, name: String, age: String? = null): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(AGE_COL, age)

        val rowsUpdated = db.update(
            TABLE_NAME,
            values,
            "$ID_COL = ?",
            arrayOf(id.toString())
        )

        db.close()

        return rowsUpdated
    }

    fun deleteData(id: Int): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted
    }

    companion object {
        private val DATABASE_NAME = "SQL_LITE_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "SHIVANG_SQL_LITE_DATABASE_TABLE"
        val ID_COL = "id"
        val NAME_COl = "name"
        val AGE_COL = "age"
    }
}
