package com.example.todolistapp;

import android.content.DialogInterface;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolistapp.Adapter.ToDoAdapter;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    //Variables to help with the RecyclerView. RecyclerView renders data.
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;          // Defining Button variable so when clicked, we can add a task.
    private List<ToDoModel> taskList;
    private DatabaseHandler db; // DB variable to be used.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Removing the navigation bar from screen top
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);   // Instantiating and opening DB
        db.openDatabase();


        //Defining the recycler view scrolling dynamic list (structured with id in activity_main.xml)
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        //Defining a linear, 1D list for the dynamic RecyclerView list
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        //Setting the adapter for the RecyclerView is important to render adapter-based views
        tasksRecyclerView.setAdapter(tasksAdapter);


        fab = findViewById(R.id.fab);                        // Button when clicked adds a task.

        taskList = db.getAllTasks();   // Getting all tasks from DB
        Collections.reverse(taskList); // Getting the newest tasks first
        tasksAdapter.setTasks(taskList);


        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                        AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                                   }
                               });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();   // Getting all tasks from DB
        Collections.reverse(taskList); // Getting the newest tasks first
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }




}