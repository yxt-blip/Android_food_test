package com.example.myshipingjiance.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "myapp.db"
        private const val DATABASE_VERSION = 1

        // 用户表
        const val TABLE_USER = "user"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_AVATAR_PATH = "avatar_path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 创建用户表
        val CREATE_USER_TABLE = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_AVATAR_PATH TEXT
            )
        """.trimIndent()
        
        db.execSQL(CREATE_USER_TABLE)
        
        // 创建默认用户
        val defaultUser = """
            INSERT INTO $TABLE_USER ($COLUMN_USERNAME, $COLUMN_DESCRIPTION)
            VALUES ('用户名', '这是个性签名')
        """.trimIndent()
        
        db.execSQL(defaultUser)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 更新数据库版本时执行
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }
} 