package com.example.todolistapp.Model;

public class ToDoModel {
    //Defining model variables
    //Task attributes are id, status, and task which are to be placed in the DB
    private int id;        // DB identifier
    private int status = 0;    // 0 (not done) or 1 (done)
    private String task;   //text of task


    //Getters and setters for list object instance views



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
