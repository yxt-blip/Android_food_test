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
//            db?.close()
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
//            db?.close()
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
    // 保存（插入）一个新的用户到数据库
    fun saveUser(user: User): Long {
        val db = dbHelper.writableDatabase
        var insertedId: Long = -1 // 用于存储新插入行的ID，-1代表插入失败

        db.beginTransaction() // 开始事务，确保操作的原子性
        try {
            val values = ContentValues().apply {
                // 将User对象的所有信息放入ContentValues
                // 注意：通常不建议直接存储明文密码，这里仅为示例
                put(DatabaseHelper.COLUMN_ID, user.id) // 假设id也是由外部传入
                put(DatabaseHelper.COLUMN_USERNAME, user.username)
                put(DatabaseHelper.COLUMN_PASSWORD, user.password)
                put(DatabaseHelper.COLUMN_PHONE, user.phone)
                put(DatabaseHelper.COLUMN_DESCRIPTION, user.description)
                put(DatabaseHelper.COLUMN_AVATAR_PATH, user.avatarPath)
            }

            // 执行插入操作
            // db.insert()会返回新插入行的ID，如果失败则返回-1
            insertedId = db.insert(DatabaseHelper.TABLE_USER, null, values)

            if (insertedId != -1L) {
                db.setTransactionSuccessful() // 如果插入成功，则标记事务成功
            }
        } catch (e: Exception) {
            Log.e("UserDao", "Error saving user", e)
        } finally {
            db.endTransaction() // 结束事务，如果已标记成功则提交，否则回滚
//            db.close() // 关闭数据库连接
        }
        return insertedId // 返回新用户的ID，调用者可以通过判断它是否为-1来确认是否成功
    }

    // 智能保存或更新用户方法 (Upsert)
    fun saveOrUpdateUser(user: User): Long {
        val db = dbHelper.writableDatabase

        // 先尝试根据ID更新用户
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USERNAME, user.username)
            put(DatabaseHelper.COLUMN_PASSWORD, user.password)
            put(DatabaseHelper.COLUMN_PHONE, user.phone)
            put(DatabaseHelper.COLUMN_DESCRIPTION, user.description)
            put(DatabaseHelper.COLUMN_AVATAR_PATH, user.avatarPath)
        }

        val rowsAffected = db.update(
            DatabaseHelper.TABLE_USER,
            values,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(user.id.toString()) // 根据传入的user.id进行更新
        )

        // 如果更新影响的行数大于0，说明用户已存在且更新成功
        if (rowsAffected > 0) {
            Log.d("UserDao", "User updated successfully for ID: ${user.id}")
            return user.id.toLong() // 返回用户的ID表示成功
        }

        // 如果更新影响的行数为0，说明该ID的用户不存在，现在执行插入操作
        Log.d("UserDao", "User not found for update, attempting to insert with ID: ${user.id}")
        return db.insert(DatabaseHelper.TABLE_USER, null, values.apply {
            put(DatabaseHelper.COLUMN_ID, user.id) // 确保ID也被包含在内
        })
    }




    private fun cursorToUser(cursor: Cursor): User {
        val idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)
        val descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)
        val avatarPathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AVATAR_PATH)
        val passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD) // 假设列名在 DatabaseHelper 中定义
        val phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)

        val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
        val username = if (usernameIndex != -1) cursor.getString(usernameIndex) else ""
        val description = if (descriptionIndex != -1) cursor.getString(descriptionIndex) else ""
        val avatarPath = if (avatarPathIndex != -1) cursor.getString(avatarPathIndex) else null

        val password = if (passwordIndex != -1) cursor.getString(passwordIndex) else ""
        val phone = if (phoneIndex != -1) cursor.getString(phoneIndex) else ""

        return User(id, username, description, avatarPath,password, phone)
    }
}
