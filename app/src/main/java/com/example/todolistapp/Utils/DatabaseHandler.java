package com.example.todolistapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.todolistapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

// Using the default Microsoft database SQLite
// SQLiteOpenHelper manages DB creation and version mgmt
public class DatabaseHandler extends SQLiteOpenHelper {

    //Name and version of DB defined as constants because they are used everywhere (as well as DB Table and column names)
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";

    // Query defined as string to create DB
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                           + TASK + " TEXT, " + STATUS + " INTEGER)";

    // Database that we will generate
    private SQLiteDatabase db;









    // Constructor for helper class
    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);

    }

    // Called at DB creation to create new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute query to create table
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Recreate table
        onCreate(db);
    }

    // Method to be called in main activity to work with the database
    public void openDatabase(){
        db = this.getWritableDatabase(); //method calls onCreate and onUpgrade methods in this class to make/open db
    }

    public void insertTask(ToDoModel task){
        // ContentValues is map-like and matches values to string keys. It stores a set of values that the ContentResolver can process.
        // We do not need to use raw SQL queries to insert into the DB with db.insert() and ContentValues!
        ContentValues cv = new ContentValues();
        // Putting task key/value pair into cv.
        cv.put(TASK, task.getTask());   // getting task string contents from the task object
        // (Use case story fulfilled) - setting box to unchecked if new.
        cv.put(STATUS, 0);
        // Inserting task into DB table
        db.insert(TODO_TABLE, null, cv); // method returns ID for newly created row. It also puts information from content values into DB
    }

    // Method to retrieve all DB tasks and place them into an ArrayList to be shown in MainActivity RecyclerView

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;   // Cursor provides access to result set returned by DB query.
        // If we have a large no of tasks to return and to prevent error - write statements in transaction to regard DB changes individually.
        db.beginTransaction();
        try{
            // Returns all rows of DB without criteria.
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        // Setting the task ID as the auto-incremented DB ID column index.
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(TASK)));
                        task.setStatus(cur.getInt(cur.getInt(cur.getColumnIndexOrThrow(STATUS))));
                        // Adding object (with state drawn from DB) to task list to ve returned.
                        taskList.add(task);
                        // While the cursor is not empty, this will run!
                    }while(cur.moveToNext());
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());

        }
        finally{
            db.endTransaction();  // (Use case story fulfilled) allows us to store the state of the DB safely in case DB shuts off.
            cur.close();
        }
        return taskList;
    }

    // (Use case story fulfilled) Choosing to change checkmark status.
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        // Placing cv to store status (to later send to DB)
        cv.put(STATUS, status);
        // Updating the local DB with cv value (Status of 0 or 1) WHERE there is the id passed as parameter (id converted from int to String, so it can be sent).
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});

    }


    // (Use case story fulfilled) Ability to update a task in code.
    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }


    // (Use case story fulfilled) Ability to delete a task in code. We delete the task id to delete the task.
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }













}
