package com.orderhub.driver

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OrderDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "order_hub.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "orders"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PLATFORM = "platform"
        private const val COLUMN_NOMINAL = "nominal"
        private const val COLUMN_PICKUP = "pickup"
        private const val COLUMN_TUJUAN = "tujuan"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_RAW = "raw"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_PLATFORM TEXT, " +
                "$COLUMN_NOMINAL TEXT, " +
                "$COLUMN_PICKUP TEXT, " +
                "$COLUMN_TUJUAN TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_RAW TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertOrder(platform: String, nominal: String, pickup: String, tujuan: String, time: String, raw: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLATFORM, platform)
            put(COLUMN_NOMINAL, nominal)
            put(COLUMN_PICKUP, pickup)
            put(COLUMN_TUJUAN, tujuan)
            put(COLUMN_TIME, time)
            put(COLUMN_RAW, raw)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllOrders(): List<OrderModel> {
        val orderList = ArrayList<OrderModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val order = OrderModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    platform = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORM)),
                    nominal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMINAL)),
                    pickup = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICKUP)),
                    tujuan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUJUAN)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                    rawText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RAW))
                )
                orderList.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orderList
    }

    fun deleteOrder(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
