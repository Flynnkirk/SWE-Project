package com.example.todolistapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


// Class to add tasks to the To Do List
// A bottom sheet is a pop-up box at the bottom of the screen.
public class AddNewTask extends BottomSheetDialogFragment {

    // TAG necessary to identify fragment.
    public static final String TAG = "ActionBottomDialog";
    // EditText is a UI element for
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private Button addDate;



    public static AddNewTask newInstance(){
        // return object of AddNewTask to be used in main activity
        return new AddNewTask();
    }


    @Override
    // The ability to save the app UI state in a dynamic Bundle means that if something changes, the UI state is already saved and can be recalled.
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);        //Dialog Style is configured in styles.xml

    }

    @Override
    // Called to instantiate the UI view of the fragment and supply the dialog
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
       getDialog().getWindow().setDecorFitsSystemWindows(true);          //CHECK THIS!!! RESIZE!!!!
        return view;
    }

    @Override
    // Execute Java code required to make the fragment function.
    // onViewCreated is called to make sure onCreateView did its job.
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initializing variables
        newTaskText = getView().findViewById(R.id.newTaskText);   // Calls EditText fom new_task.xml
        newTaskSaveButton = getView().findViewById(R.id.newTaskTextButton); // Calls Button from new_task.xml to allow saving of added task.
        newTaskSaveButton.setEnabled(false);
        db = new DatabaseHandler(getActivity()); // Passes context to handler.
        db.openDatabase(); // Opens/creates DB in handler class.

        addDate = getView().findViewById(R.id.btnAdd);






        addDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(!newTaskText.getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, newTaskText.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, "true");

                   try{
                       startActivity(intent);
                   } catch (Exception e) {
                       Toast.makeText(view.getContext(), "These is no app that can support this action", Toast.LENGTH_SHORT).show();

                   }

                } else {
                    Toast.makeText(view.getContext(), "Please Fill Out New Task Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Differentiation between saving of new task and updating of an existing task.
        boolean isUpdated = false;
        final Bundle bundle = getArguments(); // Getting adapter data to pass to AddNewTask fragment
        // If there has been state saved for a task, we say the task has been updated with isUpdated = true and retrieve the Bundle-saved task string to edit.
        if (bundle != null) {
            isUpdated = true;       // task needs edited, not created
            String task = bundle.getString("task");    // Retrieving existing task
            newTaskText.setText(task);
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));           // Calling new_task.xml color for Button.

        }

        // Creating listeners for newTaskText and newTaskSaveButton
        newTaskText.addTextChangedListener(new TextWatcher() {       // EditText object uses TextWatcher to detect change made over itself.

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            // Check if the task is empty or not
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                // If string is empty we can't save it to DB (We don't want errors) - tested this!
                if (s.toString().equals("") || s.toString().equals(" ") || s.toString().isEmpty()) {
                    // Making the Save button unusable and grey.
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.DKGRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // NOT NEEDED FROM ABSTRACT CLASS
            }
        });

        // Creating a click listener for the save button (defining what the save button does)
        // View.OnClickListener creates the instance and wires it to the button with setOnclickListener() so the system executes what is
        // in onClick(View) when pressed.
        boolean finalIsUpdated = isUpdated;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // Update task or create new
                // Getting text entered to pass to Save Button and database.
                String text = newTaskText.getText().toString();

                // if the task exists / = 1
                if(finalIsUpdated && newTaskSaveButton.isClickable()){      // Graying out Save if there is not any text.
                    // Updating existing task in DB with Save.
                    db.updateTask(bundle.getInt("id"),text);    // Finds bundle key-value pair of the task text string.


                }else if (newTaskSaveButton.isClickable()){
                    // Creating task object in DB with Save.
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);

                }else{
                    ToDoModel task = new ToDoModel();
                    task.setStatus(0);
                }
                // Getting rid of the bottom pop-up fragment.
                dismiss();
            }
        });

    }
    // Refreshing RecyclerView list of items after updating database automatically - we have to override the dismiss method of fragment for this.
    @Override
    // A dialog prompts the user to make a decision
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();                  // Getting parent activity
        if (activity instanceof DialogCloseListener){       // Seeing if activity is an instance of the DialogCloseListener (refreshes/updates Recyclerview).
            // If the activity implements DialogCloseListener...
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}




