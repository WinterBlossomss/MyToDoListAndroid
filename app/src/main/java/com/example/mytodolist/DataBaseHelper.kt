package com.example.mytodolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val TAG = "AndroidKurs"
private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "Bob"

private const val TABLE_NAME = "ToDos"
private const val COLUMN_ID = "ToDoID"
private const val COLUMN_TITLE = "ToDoTitle"
private const val COLUMN_CONTENT = "ToDoContent"
private const val COLUMN_STATUS = "ToDoStatus"


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createSql = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_CONTENT TEXT NOT NULL,
                $COLUMN_STATUS INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Simple strategy for learning projects:
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getToDoCount(): Int {
        readableDatabase.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null).use { c ->
            return if (c.moveToFirst()) c.getInt(0) else 0
        }
    }

    fun addToDoToDatabase(todo: Todo): Long {
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.todoTitle)
            put(COLUMN_CONTENT, todo.todoDescription)
            put(COLUMN_STATUS, if (todo.todoStatus) 1 else 0)
        }

        val rowId = writableDatabase.insert(TABLE_NAME, null, values)
        if (rowId != -1L) todo.todoID = rowId.toInt()

        return rowId
    }


    fun createDefaultToDos() {
        if (getToDoCount() == 0) {
            addToDoToDatabase(Todo(0, "Heimgehen", "Juhuuu", false))
            addToDoToDatabase(Todo(0, "Hunger", "Essen gehen", false))
        }
    }



    fun getAllToDos(): List<Todo> {
        val result = mutableListOf<Todo>()
        readableDatabase.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_STATUS),
            null, null, null, null,
            "$COLUMN_ID ASC"
        ).use { c ->
            val idIx = c.getColumnIndexOrThrow(COLUMN_ID)
            val titleIx = c.getColumnIndexOrThrow(COLUMN_TITLE)
            val contentIx = c.getColumnIndexOrThrow(COLUMN_CONTENT)
            val statusIx = c.getColumnIndexOrThrow(COLUMN_STATUS)

            while (c.moveToNext()) {
                val todo = Todo().apply {
                    todoID = c.getInt(idIx)
                    todoTitle = c.getString(titleIx)
                    todoDescription = c.getString(contentIx)
                    todoStatus = c.getInt(statusIx) != 0
                }
                result.add(todo)
            }
        }
        return result
    }

    fun deleteAllDatabaseEntries(): Int {
        return writableDatabase.delete(TABLE_NAME, null, null)
    }

    fun deleteNoteEntry(todoId: Int): Int {
        return writableDatabase.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(todoId.toString()))
    }

    fun editNoteEntry(todo: Todo): Int {
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, todo.todoDescription)
            put(COLUMN_STATUS, if (todo.todoStatus) 1 else 0)
            put(COLUMN_TITLE, todo.todoTitle) // optional, if you want to edit title too
        }
        return writableDatabase.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(todo.todoID.toString()))
    }
}
