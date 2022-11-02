package com.example.todolistapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolistapp.AddNewTask;
import com.example.todolistapp.MainActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.example.todolistapp.Utils.DatabaseHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// Adapters provide a binding from the data set to views that are in RecyclerView
//  The adapter is a component that stands between the data model we want to show in our app UI and the
//  UI component that renders this information. In other words, an adapter guides the way the information are shown in the UI.
// The adapter must extend a class called RecyclerView.Adapter, passing our class that implements the ViewHolder pattern:
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {


    private List<ToDoModel> todoList;       //List of tasks in To Do list.
    private MainActivity activity;          //Defining activity context


    private DatabaseHandler db;             // Defining DB variable to be used.



    //Adapter constructor to pass activity context for the adapter (And the Database itself)
    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }


    //Set the tasks created in mainActivity
    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;      // Pulling in mainActivity list to adapter list (important for rendering)
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    //RecyclerView calls to create a ViewHolder and its view (not filled yet)
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())     //LayoutInflater .inflate creates a new View from our XML layout files. (getContext starts an activity)
        .inflate(R.layout.task_layout, parent, false );   //attachToRoot: false means we will add the child view to parent later with addView() method.
        return new ViewHolder(itemView);
    }

    public Context getContext() {
        return activity;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }



    //RecyclerView calls to fill a ViewHolder with data
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        db.openDatabase();                  // Including database so that we can make changes thee according to set holder changes below.
        //Searching list for task object
        ToDoModel item = todoList.get(position);

        //important
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                }else{
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    //Helper function since ToDoModel.setChecked() returns 0 or 1 in onBindViewHolder()
    private boolean toBoolean(int n){
        return n != 0;
    }

    //RecyclerView calls to get the size of the data set (helpful for it to know if no more can be displayed)
    public int getItemCount(){
        return todoList.size();
    }











}
