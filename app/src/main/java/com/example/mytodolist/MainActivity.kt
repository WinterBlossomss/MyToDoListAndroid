package com.example.mytodolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DataBaseHelper
    private lateinit var adapter: MyAdapter
    private val todoList: MutableList<Todo> = mutableListOf()

    private var editingPosition: Int = RecyclerView.NO_POSITION

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        db = DataBaseHelper(this)
        db.createDefaultToDos()

        val etTitle: EditText = findViewById(R.id.etTitle)
        val etDescription: EditText = findViewById(R.id.etDescription)
        val btnAddOrSave: Button = findViewById(R.id.btnAdd)
        val recycler: RecyclerView = findViewById(R.id.rvListe)

        todoList.clear()
        todoList.addAll(db.getAllToDos())

        recycler.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(
            todoList = todoList,
            onDelete = { todo, position ->
                // If you delete the item currently being edited -> exit edit mode
                if (position == editingPosition) {
                    editingPosition = RecyclerView.NO_POSITION
                    btnAddOrSave.text = "Add"
                    etTitle.text.clear()
                    etDescription.text.clear()
                } else if (position < editingPosition) {
                    // Keep edit position aligned after removals above it
                    editingPosition -= 1
                }

                db.deleteNoteEntry(todo.todoID)
                adapter.removeAt(position)
            },
            onStatusChanged = { todo, _ ->
                db.editNoteEntry(todo)
            },
            onEdit = { todo, position ->
                // Toggle: click edit again to cancel edit mode
                if (editingPosition == position) {
                    editingPosition = RecyclerView.NO_POSITION
                    btnAddOrSave.text = "Add"
                    etTitle.text.clear()
                    etDescription.text.clear()
                    return@MyAdapter
                }

                editingPosition = position
                etTitle.setText(todo.todoTitle)
                etDescription.setText(todo.todoDescription)
                btnAddOrSave.text = "Save"
            }
        )

        recycler.adapter = adapter

        btnAddOrSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDescription.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Title required"
                return@setOnClickListener
            }
            if (desc.isEmpty()) {
                etDescription.error = "Description required"
                return@setOnClickListener
            }

            if (editingPosition == RecyclerView.NO_POSITION) {
                // ADD mode
                val todo = Todo(0, title, desc, false)
                db.addToDoToDatabase(todo) // important: sets todo.todoID in your helper
                todoList.add(todo)
                adapter.notifyItemInserted(todoList.size - 1)
            } else {
                // EDIT mode
                val todo = todoList[editingPosition]
                todo.todoTitle = title
                todo.todoDescription = desc

                db.editNoteEntry(todo)
                adapter.notifyItemChanged(editingPosition)

                // Exit edit mode
                editingPosition = RecyclerView.NO_POSITION
                btnAddOrSave.text = "Add"
            }

            etTitle.text.clear()
            etDescription.text.clear()
        }
    }
}

