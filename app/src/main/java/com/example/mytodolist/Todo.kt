package com.example.mytodolist

import java.sql.Date

class Todo {
    var Title : String = "";
    var Description : String = "";
    var Status : Boolean = false;
    var Datum : String = "";

    constructor(Title: String, Description:String, Status : Boolean)
    {
        this.Title = Title;
        this.Description = Description;
        this.Status = Status;
    }
}