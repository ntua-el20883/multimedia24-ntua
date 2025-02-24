# Project's Description and Report

---

## Project's Description

> This is the English translation of the project's description.  
> To view the original description, in Greek, please refer to the document [here](documentation/GREEK_description_v1.pdf).

In the context of the project a Task Management System will be implemented. The application will allow the user to create, edit, and monitor the available tasks. In addition, the user will be able to manage multiple tasks, set priorities and deadlines, and receive reminders for upcoming tasks.

### **A.1. Design and implementation of logic (40%)**
The following describes the capabilities that the application should provide to the user regarding the creation and management of tasks and reminders.

#### **Adding, modifying, and deleting tasks**
The user will be able to create new tasks. The relevant information for a task should include: title, description, category, priority, completion deadline, and status. For simplicity, the completion deadline for a task will not include a time, but will be set at the day level (e.g. 12/14/24).

The status of a task will always be one of the following: "Open", "In Progress", "Postponed", "Completed", and "Delayed". For each new task the default status will be "Open". When initializing the application, tasks that are not "Completed" and have passed the completion deadline should be detected and the status should automatically change to "Delayed".

The user will be able to modify all elements of a task as well as proceed to delete tasks. In the case of deletions, care must be taken to ensure that the possible reminders set for the task to be deleted are properly updated.

#### **Adding, modifying, and deleting a category**
The user will be able to define new categories by giving the relevant name. In addition, he/she will be able to modify the name of a category. For simplicity, we assume that there are no subcategories.
Also, he will be able to delete a category together with the automatic deletion of all tasks belonging to it. In this case the reminders for the deleted tasks should be updated appropriately.

#### **Adding, modifying, and deleting priority**
The application should include a default priority level named "Default". The user will be able to define new priority levels by giving the relevant name. In addition, it will be possible to modify the name of a priority level as well as delete priority levels.
The change name and delete functions will apply to all priority levels except the default. Also, when a priority level is deleted then all tasks belonging to the relevant level shall automatically be assigned the default priority level.

#### **Additional functions**
Setting and managing reminders: the user will be able to create reminders for tasks. A reminder will always be associated with a task, and multiple reminders can be set for a task. If a task has a status of "Completed" there will be no possibility to set reminders, and when the user changes the status of a task to "Completed" then the application will automatically delete any reminders related to that task.
The application should support the following types of reminders:
- (i) one day before the deadline,
- (ii) one week before the deadline,
- (iii) one month before the deadline,
- (iv) a specific date defined by the user.

Appropriate checks should be implemented to ensure that the selected reminder type is meaningful based on the deadline for completion of the task and that in case of an issue the user is informed with the corresponding message. Finally, the user will be able to modify and delete reminders.

#### **Search for tasks**
The user will be able to search for tasks based on any combination of the following criteria: title, category, and priority.

### **A.2. Storage and retrieval of application information (10%)**
A solution based on the use of files containing data in JSON format will be used to store and retrieve application information. JSON (JavaScript Object Notation) is a data representation format widely used for storing and transferring data. It is easy to read by humans and, at the same time, understandable by several programming languages. JSON follows a simple structure using text to represent data in a key-value format. More information can be found in the workshop slides in the "Java Networking & JSON" section.

Initially you will need to decide and define your own organization (data schema) for the JSON data and the set of files that will be used to store the application data. The application data files should be stored in a folder named "medialab".

Next you need to implement through the appropriate classes the methods that will allow you to retrieve the information that the files have and initialize the appropriate objects in your application and refresh the files so that the overall state can be maintained between successive runs of the application.

Next we describe the logic that must be implemented to retrieve and refresh the application data.
- **Application initialization**: The retrieval of all the information in the JSON files should be done and at the same time initialize the corresponding objects in your application.
- **Application execution**: The application will use the state information retrieved in program memory during initialization. All operations related to tasks and reminders managed by the application will be executed based on the information present in the program memory.
- **Application termination**: Updating JSON files with system state information will be done exclusively before application termination. The implementation shall store in the corresponding JSON files the overall state of the application at the time of termination.

### **A.3. Creation of a graphical interface (30%)**
You shall design and implement the appropriate Graphical User Interface (GUI) using the JavaFX framework [1][2].  
*Note*: The basic specifications for the GUI are presented below, for all the details of the final implementation you can make any choices you wish regarding the appearance and general user interaction with the application, without any impact on the final score. For example, you can choose a simple visualization for the various elements or combine various features from JavaFX to create an effect that corresponds to a modern application. In any case, there is no need to make this part of the task complicated.

- Initially when starting the application if there are tasks that are in "Delayed" state the user should be informed with an appropriate popup window about the number of overdue tasks.

Regarding the creation of the GUI you should follow the general instructions below:
- **Create the main "window"** of the application entitled "MediaLab Assistant" and set the appropriate dimensions.
- **Divide the window into two main parts.**
  - The upper part of the screen will display aggregated information that should be updated accordingly based on the user's actions. The information includes:
    - Total number of tasks regardless of status,
    - Number of tasks with status "Completed",
    - Number of tasks with status "Delayed",
    - Number of tasks with a completion deadline within 7 days.
  - In the other part of the screen, the various functions that the GUI must support should be implemented. There is complete freedom as to how to implement these functions, however, you should ensure that the GUI information is updated appropriately according to the user's actions.

The GUI should support the following functions:
- **Task management**: the application should present the available tasks by category. It should also be possible for the user to define a new task, modify a task and delete a task. The implementation must be done according to the corresponding logic from section A.1.
- **Category management**: the application should present the list of categories. Furthermore, the user should be able to define new categories, change the name of a category and delete categories. The implementation should be done according to the corresponding logic from section A.1.
- **Management of priority levels**: the application should present the available priority levels. The user should be able to modify the name of priority levels and delete priority levels. The implementation should be done according to the corresponding logic from section A.1.
- **Reminder management**: the application should present all active reminders. The user should be able to modify a reminder as well as delete reminders. The implementation should be in accordance with the corresponding logic from section A.1.
- **Search for tasks**: the corresponding form should be available to allow searching for tasks according to the specifications from section A.1. The results should include: title, priority level, category and deadline for completion.

Finally, your GUI implementation should ensure that when the application is terminated, the system information in the relevant files is updated according to the procedure described in section A.2.

### **A.4. Other requirements (20%)**
The implementation should follow the object-oriented programming (OOP) design principles.

In a class of your choice, each public method it contains must be documented according to the javadoc tool specification [3].

*Note*: For anything that is not clear from the pronunciation you can make your own assumptions and assumptions. The pronunciation outlines the basic requirements for the implementation, however you can make your own design assumptions trying to make the implementation more realistic without making the implementation complex.

---

### Deliverables

- The project (of the IDE of your choice) with the code to implement the application.
- A short (max 3 pages) report containing a general description of the implementation design and your design description of the information structure in the different JSON files. You will also mention any functionality you have not implemented and any assumptions you have made. Under no circumstances should you include code fragments.

### References

[1] [https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm)  
[2] [https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm](https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm)  
[3] [https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)

---

## Project's Report

> This is the English translation of the project's report.  
> To view the original description, in Greek, please refer to the document [here](documentation/GREEK_report.pdf).
