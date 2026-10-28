package com.example.rezepteessen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "rezepteEssen.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE rezepteEssen (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "essenName TEXT," +
            "rezepte TEXT," +
            "noteZuEnde TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS rezepteEssen")
        onCreate(db)
    }


    fun createNewEssen(item: Item): Long {
        val values = ContentValues().apply {
            put("essenName", item.essenName)
            put("rezepte", item.rezepte)
            put("noteZuEnde", item.noteZuEnde)
        }
        return writableDatabase.insert("rezepteEssen", null, values)
    }

    fun getAllItems(): List<Item> {
        val list = mutableListOf<Item>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM rezepteEssen", null)
        while (cursor.moveToNext()) {
            val item = Item(
                id = cursor.getInt(0),
                essenName = cursor.getString(1),
                rezepte = cursor.getString(2),
                noteZuEnde = cursor.getString(3)
            )
            list.add(item)
        }
        cursor.close()
        return list
    }

    fun updateEssen(item: Item): Int {
        val values = ContentValues().apply {
            put("essenName", item.essenName)
            put("rezepte", item.rezepte)
            put("noteZuEnde", item.noteZuEnde)
        }
        return writableDatabase.update("rezepteEssen", values, "id = ?", arrayOf(item.id.toString())
        )
    }
}