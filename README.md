# Task Management System

In the context of the project a Task Management System will be implemented. The application will allow the user to create, edit, and monitor the available tasks.  In addition, the user will be able to manage multiple tasks, set priorities and deadlines, and receive reminders for upcoming tasks.

---

## Prerequisites - Setup Guide for Windows

Follow these steps to set up your environment successfully.

---

### **Java**  
*Mandatory programming language for this project.*

1. **Download Java JDK 23 (for Windows)**  
   [Download JDK-23](https://download.oracle.com/java/23/latest/jdk-23_windows-x64_bin.exe)

2. **Install Java**  
   - Set the installation folder to:  
     ```
     C:\Program Files\Java\jdk-23
     ```

3. **Update PATH Variables** (if not auto-configured):  
   - Press the **Windows key**, search for **"Edit the system environment variables"**, and open it.  
   - Click **Environment Variables** and locate the `Path` variable under both **User Variables** and **System Variables**.  
   - Select the `Path` variable, click **Edit**, then **New**, and add:  
     ```
     C:\Program Files\Java\jdk-23\bin
     ```

4. **Restart Your Terminals**  
   - Restart all open terminals (e.g., Command Prompt, VS Code) to apply the changes.

---

### **JavaFX**  
*Frontend-related components.*

> **Important:**  
> JavaFX and Jackson encounter issues when stored in paths with white spaces or non-English characters.  
> To avoid this, these dependencies are placed in the project directory. For example, mine was:  
> `C:\Users\juant\Documents\Java_Projects\multimedia24-ntua\lib`  
> Ensure the path to `lib` has no spaces and uses only English characters.

1. **Download JavaFX SDK**  
   - Visit the [Gluon JavaFX Website](https://gluonhq.com/products/javafx/) and configure the following:  
     - **JavaFX version:** `23.0.1`  
     - **Operating System:** `Windows`  
     - **Architecture:** `x64`  
     - **Type:** `SDK`  
   - Click **Download** to get the `.zip` file:  
     ```
     openjfx-23.0.1_windows-x64_bin-sdk
     ```

2. **Extract and Place Files**  
   - Unzip the downloaded file. Inside, locate the folder:  
     ```
     javafx-sdk-23.0.1
     ```
   - Move `javafx-sdk-23.0.1` to the project directory (you'll need to create the lib folder in the project's root):  
     ```
     C:\path\to\multimedia24-ntua\lib
     ```

---

### **Jackson**  
*Used for reading `.json` files.*

1. **Download Jackson Packages**  
   Download the following `.jar` files by navigating to the provided links. Choose the package that exactly matches the titles below (usually the 9th option in the list).

   - [**jackson-annotations-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.18.1/)
   - [**jackson-core-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.18.1/)
   - [**jackson-databind-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.18.1/)
   - [**jackson-datatype-j*sr310**](https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.18.1/jackson-datatype-jsr310-2.18.1.jar)

2. **Place Files in the `lib` Directory**  
   After downloading, move the `.jar` files to:  
     ```
     C:\path\to\multimedia24-ntua\lib
     ```

---

### Side Note: VS-Code
If you're using VS-Code, ***Extension Pack for Java*** is a collection of popular extensions that can help you. The pack includes the following extensions:

- 📦 Language Support for Java™ by Red Hat
   - Code Navigation
   - Auto Completion
   - Refactoring
   - Code Snippets
- 📦 Debugger for Java
   - Debugging
- 📦 Test Runner for Java
   - Run & Debug JUnit/TestNG Test Cases
- 📦 Maven for Java
   - Project Scaffolding
   - Custom Goals
- 📦 Gradle for Java
   - View Gradle tasks and project dependencies
   - Gradle file authoring
   - Import Gradle projects via Gradle Build Server
- 📦 Project Manager for Java
   - Manage Java projects, referenced libraries, resource files, packages, classes, and class members
- 📦 Visual Studio IntelliCode
   - AI-assisted development
   - Completion list ranked by AI

---

## Javadoc Documentation

All classes within ```/multimedia24-ntua/src/``` were described in Javadoc. However, I recommend the [MainController.java](src/controllers/MainController.java) as it has more public classes.

To see all the classes in detail, you can open the [index.html](docs/index.html) file in your favorite browser.

In case there is a problem with the file, run the following command at ```/multimedia24-ntua/src/```:
   ```
   Javadoc -d ../docs -sourcepath . -subpackages controllers:model:view:storage -classpath "..\lib\javafx-sdk-23.0.1\lib\*;. \lib\jackson-annotations-2.18.1.jar;..\lib\jackson-core-2.18.1.jar;..\lib\jackson-databind-2.18.1.jar;..\lib\jackson-datatype-jsr310-2.18.1.jar"
   ```

---

## Project's Description and Report

> For the original description of the project's requirements, please refer to the original document available [here](documentation/GREEK_description_v1.pdf).  
> For the project's report, in Greek, please refer to the document [here](documentation/GREEK_report.pdf).   
> For the English translation of both the project's description and report (also provided below), please refer to the document [here](documentation/README.md).   

### Project's Description 

In the context of the project a Task Management System will be implemented. The application will allow the user to create, edit, and monitor the available tasks. In addition, the user will be able to manage multiple tasks, set priorities and deadlines, and receive reminders for upcoming tasks.

#### **A.1. Design and implementation of logic (40%)**
The following describes the capabilities that the application should provide to the user regarding the creation and management of tasks and reminders.

##### **Adding, modifying, and deleting tasks**
The user will be able to create new tasks. The relevant information for a task should include: title, description, category, priority, completion deadline, and status. For simplicity, the completion deadline for a task will not include a time, but will be set at the day level (e.g. 12/14/24).

The status of a task will always be one of the following: "Open", "In Progress", "Postponed", "Completed", and "Delayed". For each new task the default status will be "Open". When initializing the application, tasks that are not "Completed" and have passed the completion deadline should be detected and the status should automatically change to "Delayed".

The user will be able to modify all elements of a task as well as proceed to delete tasks. In the case of deletions, care must be taken to ensure that the possible reminders set for the task to be deleted are properly updated.

##### **Adding, modifying, and deleting a category**
The user will be able to define new categories by giving the relevant name. In addition, he/she will be able to modify the name of a category. For simplicity, we assume that there are no subcategories.
Also, he will be able to delete a category together with the automatic deletion of all tasks belonging to it. In this case the reminders for the deleted tasks should be updated appropriately.

##### **Adding, modifying, and deleting priority**
The application should include a default priority level named "Default". The user will be able to define new priority levels by giving the relevant name. In addition, it will be possible to modify the name of a priority level as well as delete priority levels.
The change name and delete functions will apply to all priority levels except the default. Also, when a priority level is deleted then all tasks belonging to the relevant level shall automatically be assigned the default priority level.

##### **Additional functions**
Setting and managing reminders: the user will be able to create reminders for tasks. A reminder will always be associated with a task, and multiple reminders can be set for a task. If a task has a status of "Completed" there will be no possibility to set reminders, and when the user changes the status of a task to "Completed" then the application will automatically delete any reminders related to that task.
The application should support the following types of reminders:
- (i) one day before the deadline,
- (ii) one week before the deadline,
- (iii) one month before the deadline,
- (iv) a specific date defined by the user.

Appropriate checks should be implemented to ensure that the selected reminder type is meaningful based on the deadline for completion of the task and that in case of an issue the user is informed with the corresponding message. Finally, the user will be able to modify and delete reminders.

##### **Search for tasks**
The user will be able to search for tasks based on any combination of the following criteria: title, category, and priority.

#### **A.2. Storage and retrieval of application information (10%)**
A solution based on the use of files containing data in JSON format will be used to store and retrieve application information. JSON (JavaScript Object Notation) is a data representation format widely used for storing and transferring data. It is easy to read by humans and, at the same time, understandable by several programming languages. JSON follows a simple structure using text to represent data in a key-value format. More information can be found in the workshop slides in the "Java Networking & JSON" section.

Initially you will need to decide and define your own organization (data schema) for the JSON data and the set of files that will be used to store the application data. The application data files should be stored in a folder named "medialab".

Next you need to implement through the appropriate classes the methods that will allow you to retrieve the information that the files have and initialize the appropriate objects in your application and refresh the files so that the overall state can be maintained between successive runs of the application.

Next we describe the logic that must be implemented to retrieve and refresh the application data.
- **Application initialization**: The retrieval of all the information in the JSON files should be done and at the same time initialize the corresponding objects in your application.
- **Application execution**: The application will use the state information retrieved in program memory during initialization. All operations related to tasks and reminders managed by the application will be executed based on the information present in the program memory.
- **Application termination**: Updating JSON files with system state information will be done exclusively before application termination. The implementation shall store in the corresponding JSON files the overall state of the application at the time of termination.

#### **A.3. Creation of a graphical interface (30%)**
You shall design and implement the appropriate Graphical User Interface (GUI) using the JavaFX framework [[1]](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.html), [[2]](https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.html).  
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

#### **A.4. Other requirements (20%)**
The implementation should follow the object-oriented programming (OOP) design principles.

In a class of your choice, each public method it contains must be documented according to the javadoc tool specification [[3]](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html).

*Note*: For anything that is not clear from the pronunciation you can make your own assumptions and assumptions. The pronunciation outlines the basic requirements for the implementation, however you can make your own design assumptions trying to make the implementation more realistic without making the implementation complex.

---

#### Deliverables

- The project (of the IDE of your choice) with the code to implement the application.
- A short (max 3 pages) report containing a general description of the implementation design and your design description of the information structure in the different JSON files. You will also mention any functionality you have not implemented and any assumptions you have made. Under no circumstances should you include code fragments.

#### References

[1] [https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.html](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm)  
[2] [https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.html](https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm)  
[3] [https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)

---

### Project's Report

> This is the English translation of the project's report.  
> To view the original description, in Greek, please refer to the document [here](GREEK_report.pdf).

---

#### Multimedia Technology
**Semester Work Report**  
Ioannis Tsantilas, 03120883  
**Repository & Setup**

The project implementation includes specific `.jar` files, which are too large to be pushed to the repository. For this reason, I have created a README file in which I give instructions on how to setup the project [here](../README.md).

---

#### Database
My database consists of four `.json` files at `multimedia24-ntua/multimedia/`. Specifically, these files are:
- **categories.json**: takes care of storing the categories. The categories have the following fields: `name`.
- **priorities.json**: takes care of storing the priorities. The priorities have the following fields: `name`.
- **reminders.json**: takes care of storing the reminders. Reminders have the following fields: `taskTitle`, `date`.
- **tasks.json**: takes care of storing the tasks. Tasks have the following fields: `title`, `description`, `category`, `priority`, `deadline`, `status`.

---

#### Structure - Assumptions
The project structure is characterized by four corresponding entities: **task**, **category**, **priority** and **reminder**.

##### Task
- The mandatory information that a user must provide for a task is its **title** and its **deadline**.
- The title of each task should be unique (not case-sensitive).
- The deadline cannot be a past date.
- In case the deadline is not given:
  - **Description** will be left blank.
  - **Category** will be set to its default value, `Default`.
  - **Priority** will be set to its default value, `Default`.
  - **Status** will be set to its default value, `Open`.
    - Unlike category and priority, the user cannot edit statuses. They can only choose from the available ones: `Open`, `In Progress`, `Completed`, `Delayed`, `Postponed`.
    - So, in case the user sets a `Delayed` status on a task due in 7 days, this task will be counted in both "Delayed Tasks" and "Tasks due in 7 days".

##### Category & Priority
- The mandatory information a user must provide for a **Category/Priority** is their **name**.
- The name of each Category/Priority should be unique (not case-sensitive).
- The user cannot delete or edit the default Category/Priority, `Default`.
- If at application launch the default Category/Priority does not exist in `categories.json`/`priorities.json`, the application automatically creates it.

##### Reminder
- The mandatory information a user must provide for a **Reminder** is the **task** (`taskTitle`) and the **date** (`date`).
- Because the reminder is set at the day level (not the time level), for the same task there cannot be multiple reminders on the same day.
- The date of the reminder cannot be before the current date, nor after the deadline of the selected task.
  - This means that if the user selects a predefined option (e.g. "one week before the deadline") and it falls before the current date, an error window will appear.
  - Of course, the same will happen if the user selects any date and it falls outside the allowed limits.
- If the user changes the deadline of a task for which a reminder is dated after the new deadline, the application will notify the user that if they proceed, that reminder will be deleted.
  - For example, a task with a deadline of `3/20/25` and a reminder of `3/15/25` – if the user changes the deadline to `10/03/25`, the reminder will be deleted since it is out of bounds.
- If the user changes the deadline of a task for which a reminder is dated before the new deadline and the user has selected it through the predefined options, the application will automatically update the date.
  - For example, a task with a deadline of `20/03/25` and a reminder of `13/03/25` (via the "1 week before" option) – if the user changes the deadline to `19/03/25`, the reminder will change to `12/03/25`.
- There is a sub-case in this rule: if the deadline is set with a default option, there is a chance that the new reminder date will be in the past. In this case, the user is notified with a corresponding window and encouraged to check his/her reminders.
  - For example, if today is `10/02/25`, a task with a deadline of `20/03/25` and a reminder of `20/02/25` (via the "1 month before" option) – if the user changes the deadline to `09/03/25`, then the reminder will change to `09/02/25`, which is in the past. This reminder will remain, and the user will be prompted to check their reminders.

#### Task Search 
The user has the option to search for tasks (within Task Management) based on the filters: title, category, priority, status and deadline, in which case they select a date and all tasks with a deadline **up to** that date are displayed. 

---

#### Implementation
The implementation followed the rules of object-oriented programming, separating functionality into distinct classes, each of which incorporates its own data and behavior.

As shown in the code structure within `/multimedia24-ntua/src/`, the implementation follows the MVC model:
- **Model classes** (such as `Task`, `Category`, `Priority`, and `Reminder`) wrap attributes with private fields and expose functionality via getters and setters, ensuring data integrity and encapsulation.
- **Controllers** (such as `TaskController` and `CategoryController`) manage business logic and coordinate user actions without exposing internal implementations, which reinforces the Single Responsibility Principle. In addition, they implement input validation and update routines (e.g. checking for duplicate tasks or updating reminder status).
- The use of **JavaFX** for the view part (with classes such as `TaskView` and `MainView`) demonstrates how the user interface is kept distinct from the underlying data logic.
- Additionally, the `DataStore` class implements the Singleton pattern, ensuring that all parts of the application have access to a single, consistent data source.  
  Polymorphism is exploited through the dynamic behavior of UI elements (for example, cells that override the `updateItem` method), and abstraction is evident in methods such as `loadAllData()` and `saveAllData()`, which hide the complexity of JSON serialization using Jackson.

The retrieval and refreshing of the application data was done according to the provided instructions. At application initialization, appropriate checks are made (e.g. to inform the user if there are Delayed tasks, if the default category and priority exist, if there are tasks with a past deadline, etc.), and with each new change the database is refreshed as soon as the user clicks "Save" (e.g. when creating a new task, deleting a category, or editing a priority).