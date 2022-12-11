package com.example.todolistapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class MainActivity extends AppCompatActivity implements DialogCloseListener, AdapterView.OnItemSelectedListener {

    //Variables to help with the RecyclerView. RecyclerView renders data.
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;          // Defining Button variable so when clicked, we can add a task.
    private FloatingActionButton v2t;          // Voice to text button (Sprint 3)
    private List<ToDoModel> taskList;
    private DatabaseHandler db; // DB variable to be used.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Removing the navigation bar from screen top
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);   // Instantiating and opening DB
        db.openDatabase();


        //Defining the recycler view scrolling dynamic list (structured with id in activity_main.xml)
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        //Defining a linear, 1D list for the dynamic RecyclerView list
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        //Setting the adapter for the RecyclerView is important to render adapter-based views
        tasksRecyclerView.setAdapter(tasksAdapter);


        fab = findViewById(R.id.fab);                        // Button when clicked adds a task.
        v2t = findViewById(R.id.v2t);                        // Voice to text (Sprint 3)

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);            // Defining itemTouchHelper (for swipe functioning)
        
        // Code which fetches voice to text speech to add a new task out of it (Sprint 3)

        Intent intent = getIntent();
        String text = intent.getStringExtra("Spoken Text");
        System.out.println("text: " + text);
        addVoice2TextTask(text);
        
       
        taskList = db.getAllTasks();   // Getting all tasks from DB
        Collections.reverse(taskList); // Getting the newest tasks first
        tasksAdapter.setTasks(taskList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliverBottomFragment();
            }
        });
        //Triggers dialog box on first entrance to app only
        if (savedInstanceState == null) {
            onEntry();
        }
        
        // (Sprint 3)
        v2t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

    }
    
    // Method for voice to text (Sprint 3)

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
    
    
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        System.out.println("Position" + position);
        if (position == 1) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);


        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Not needed
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();   // Getting all tasks from DB
        Collections.reverse(taskList); // Getting the newest tasks first
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("To Do List App Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }


    public void onEntry() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to add a task ?");
        builder.setTitle("Welcome !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            DeliverBottomFragment();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void DeliverBottomFragment() {
        AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
    }
    
    
    // (Sprint 3)
    public void addVoice2TextTask(String text) {

        if (text != null) {

            ToDoModel task = new ToDoModel();
            task.setTask(text);
            task.setStatus(0);
            db.insertTask(task);
        }
    }
    
}




