package com.example.mytodolist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etTitle : EditText = findViewById(R.id.etTitle);
        val etDescription : EditText = findViewById(R.id.etDescription);
        val addToDo: Button = findViewById(R.id.btnAdd);
        val todoList : MutableList<Todo> = ArrayList();
        val recycler : RecyclerView = findViewById(R.id.rvListe);

        addToDo.setOnClickListener { v ->  run{
            todoList.add(Todo(
                etTitle.text.toString(), etDescription.text.toString(), false
            ))
            recycler.layoutManager = LinearLayoutManager(this);
            val adapter = MyAdapter(todoList,this );
            recycler.adapter = adapter;
        } }

        //To Do
        todoList.add(Todo(
            "Title", "Desc", true
        ))


        recycler.layoutManager = LinearLayoutManager(this);
        val adapter = MyAdapter(todoList,this );
        recycler.adapter = adapter;
    }
}