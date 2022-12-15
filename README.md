# *SWE-Project*



# Organization, Location, and Importance of the Files

Enter into ```app```, to ```src```, then to ```main``` to locate relevant project files.

The ```res``` folder holds resources for this project. Notable folders include ```drawable```, ```layout```, and ```values```. ```drawable``` contains .PNG files and .xml files containing clip art that is utilized for application button and backgrounds. ```values``` contains application color themes that form the basis of day and night mode in the app. ```layout``` includes .xml files outlining the structural basis of the application - for example, checkboxes, the task list layout, the splash screen, and the pop-up box for new tasks.

The ```java/com/example/todolistapp``` package contains all relevant classes pertinent to the To Do List Application Project. 
 * ```MainActivity.java``` is a class that provides the window in which the app draws its UI. It is the most important file, being the control center of the app.
 * ```Activity2.java``` is class that provides the second window which is used for voice-to-text
 * ```SplashActivity.java``` is a class that provides a brief window to introduce the application. It is shown for one second.
 * ```AddNewTask.java``` is a class that delivers a dialog fragment which appears at the bottom of the screen when adding a task. It includes logic for the save button and the calendar.
 * ```RecyclerItemTouchHelper.java``` is a class that helps with the edit and delete swiping functionality.
 * ```DialogCloseListener.java``` is an interface that handles whenever a dialog box closes. It is used in the MainActivity to refresh the screen after changes.
 * Under the ```Utils``` package, ```DatabaseHandler.java``` is a class which extends SQLite functionalities. It managed DB creation and version management. It also includes methods for adding tasks, updating them, and deleting them.
 * Under the ```Models``` package, ```ToDoModel.java``` is a class that defines the task attributes of ID, status, and task, which encapsulated in a ToDoModel object, can be added to lists and the SQLite database.
 * Under the ```Adapter``` package, ```ToDoAdapter.java``` is a class tbat provides a binding from the data set to views in RecyclerView. The adapter guides the way information is shown in UI.



# Sprint #1:

Covers one-third of use case stories. Functionality includes a splash screen, listing tasks, adding new tasks, and posting to a SQLite Database.


# Sprint #2:

Covers one-third of use case stories. Functionality includes a Calendar functionality, edit task functionality, and delete task functionality.


# Sprint #3:

Covers one-third of use case stories. Functionality includes Voice-to-text functionality and the addition of text originating from voice input.
