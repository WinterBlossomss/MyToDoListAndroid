package com.example.mytodolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val todoList: MutableList<Todo>,
    private val onDelete: (Todo, Int) -> Unit,
    private val onStatusChanged: (Todo, Boolean) -> Unit,
    private val onEdit: (Todo, Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todo = todoList[position]

        holder.tvTitle.text = todo.todoTitle
        holder.tvDescription.text = todo.todoDescription

        holder.cbCheck.setOnCheckedChangeListener(null)
        holder.cbCheck.isChecked = todo.todoStatus
        holder.cbCheck.setOnCheckedChangeListener { _, isChecked ->
            if (todo.todoStatus != isChecked) {
                todo.todoStatus = isChecked
                onStatusChanged(todo, isChecked)
            }
        }

        holder.btnDelete.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) onDelete(todoList[pos], pos)
        }

        holder.btnEdit.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) onEdit(todoList[pos], pos)
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun removeAt(position: Int) {
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val cbCheck: CheckBox = itemView.findViewById(R.id.cbCheck)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
    }
}



