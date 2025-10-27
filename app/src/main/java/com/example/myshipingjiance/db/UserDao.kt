package com.example.myshipingjiance.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UserDao(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // 获取当前用户信息
    fun getCurrentUser(): User? {
        val db = dbHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(
                DatabaseHelper.TABLE_USER,
                null,
                "${DatabaseHelper.COLUMN_ID} = ?",
                arrayOf("1"),
                null, null, null
            )

            if (cursor?.moveToFirst() == true) {
                return cursorToUser(cursor)
            }
        } catch (e: Exception) {
            Log.e("UserDao", "Error getting user", e)
        } finally {
            cursor?.close()
            db?.close()
        }
        return null
    }

    // 更新用户信息
    fun updateUser(username: String, description: String, avatarUri: Uri?): Boolean {
        val db = dbHelper.writableDatabase
        var rowsAffected = 0
        try {
            Log.d("UserDao", "数据库连接")
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USERNAME, username)
                put(DatabaseHelper.COLUMN_DESCRIPTION, description)

                avatarUri?.let {
                    val avatarPath = saveImageToInternalStorage(it)
                    put(DatabaseHelper.COLUMN_AVATAR_PATH, avatarPath)
                }
            }

            rowsAffected = db.update(
                DatabaseHelper.TABLE_USER,
                values,
                "${DatabaseHelper.COLUMN_ID} = ?",
                arrayOf("1")
            )
        } catch (e: Exception) {
            Log.e("UserDao", "Error updating user", e)
        } finally {
            db?.close()
        }
        return rowsAffected > 0
    }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, "profile_avatar.jpg")

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }

        return file.absolutePath
    }

    private fun cursorToUser(cursor: Cursor): User {
        val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)
        val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
        val avatarPathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AVATAR_PATH)

        val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
        val username = if (usernameIndex != -1) cursor.getString(usernameIndex) else ""
        val description = if (descriptionIndex != -1) cursor.getString(descriptionIndex) else ""
        val avatarPath = if (avatarPathIndex != -1) cursor.getString(avatarPathIndex) else null

        return User(id, username, description, avatarPath)
    }
}
