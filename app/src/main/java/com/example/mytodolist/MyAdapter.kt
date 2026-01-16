package com.example.mytodolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val todoList : MutableList<Todo>, private val contextExt: Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_card,parent,false)
        return MyViewHolder(view);
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val actualToDo = todoList[position]
        holder.tvTitle.text = actualToDo.Title;
        holder.tvDescription.text = actualToDo.Description;
        holder.cbCheck.isChecked = actualToDo.Status;
        holder.cbCheck.setOnCheckedChangeListener { v, b -> run{
            actualToDo.Status = holder.cbCheck.isChecked;
        } }
        holder.btnDelete.setOnClickListener {
            val pos = holder.layoutPosition
            if (pos != RecyclerView.NO_POSITION) {
                todoList.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, todoList.size)
            }
        }

    }

    override fun getItemCount(): Int {
        return todoList.size;
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle);
        val tvDescription : TextView = itemView.findViewById(R.id.tvDescription);
        val cbCheck : CheckBox = itemView.findViewById(R.id.cbCheck);
        val btnDelete : Button = itemView.findViewById(R.id.btnDelete);
    }
}

