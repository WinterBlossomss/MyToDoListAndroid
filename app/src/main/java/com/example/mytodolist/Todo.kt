package com.example.mytodolist

import kotlin.text.isNotEmpty

class Todo {
    var todoID: Int = 0
        get () = field
        set (value){ if (value >= 0) field = value; }
    var todoTitle: String = ""
        get () = field
        set (value){ if (value.isNotEmpty()) field = value; }
    var todoDescription: String =
        ""
        get () = field
        set (value){ if (value.isNotEmpty()) field = value; }
    var todoStatus: Boolean = false;

    constructor() {}
    constructor(ID: Int, Title: String, Description: String, Status: Boolean)
    {
        this.todoID = ID;
        this.todoTitle = Title;
        this.todoDescription = Description;
        this.todoStatus = Status;
    }
}