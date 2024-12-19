# Task Management System - Setup Guide

## Prerequisites

Follow these steps to set up your environment successfully.

---

### **Java**  
*Mandatory programming language for this project.*

1. **Download Java JDK 23 for Windows**  
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
> To avoid this, these dependencies are placed in the project directory:  
> `C:\Users\juant\Documents\coding\task-mng-system\lib`  
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
   - Move `javafx-sdk-23.0.1` to the project directory:  
     ```
     C:\Users\juant\Documents\coding\task-mng-system\lib
     ```

---

### **Jackson**  
*Used for reading `.json` files. Alternatively, you can use Gson.*

1. **Download Jackson Packages**  
   Download the following `.jar` files by navigating to the provided links. Choose the package that exactly matches the titles below (usually the 9th option in the list).

   - [**jackson-annotations-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.18.1/)
   - [**jackson-core-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.18.1/)
   - [**jackson-databind-2.18.1.jar**](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.18.1/)

2. **Place Files in the `lib` Directory**  
   After downloading, move the `.jar` files to:  
     ```
     C:\Users\juant\Documents\coding\task-mng-system\lib
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


# Project Description

## **Section 1: Design and Implementation of Logic**
Description of the Entities and Attributes. Inside the [] is the format or mandatory values the Attributes will take.

### **Task**
- **Title**
- **Description**
- **Category**: `["Default", ...]`
- **Priority**: `["Default", ...]`
- **Deadline**: `["dd/mm/yyyy"]`
- **Status**: `["Open", "In Progress", "Postponed", "Completed", "Delayed"]`

#### **Points to Consider**
- A new task has default status `"Open"`.
- If the current date is past the deadline, the status should automatically be updated to `"Delayed"`.
- The user can create a new Task, modify an existing one (all attributes except Status) and delete one (in which case its corresponding Reminders (if they exist) should also be deleted).

---

### **Category**
- **Name**

#### **Points to Consider**
- A Task belongs to exactly one Category.
- If the user does not specify the Category of a new Task, the Category is `"Default"`.
- The user can create new Categories, modify the name of existing ones and delete (in which case its corresponding Tasks (if they exist) should also be deleted).

---

### **Reminder**
- **Date**

#### **Points to Consider**
- Valid Reminder dates: `"1 day before Deadline"`, `"1 week before Deadline"`, `"1 month before Deadline"`, or custom dates before the Deadline.
- Reminders belong to exactly one Task.
- Tasks may have multiple Reminders.
- `"Completed"` Tasks cannot have new Reminders.
- Tasks marked as `"Completed"` automatically delete their Reminders.
- The user can create new Reminders, modify the date of existing ones and delete.

---

### **Priority**
- **Name**: `["Default", ...]`
- **Value**: `["0", ...]`

#### **Points to Consider**
- If the user doesn't assign a Priority to a new Task, its Priority will be set to the default Priority, with  `Name: "Default"` and  `Value: "0"`.
- Larger Values indicate higher Priority.
- Priority Values must be unique and integers > 0.
- Users cannot modify or delete the `"Default"` Priority.
- The user can create new Priorities (assigning a Name and Value), modify existing ones and delete (in which case its corresponding Tasks (if they exist) should have their Priority set to `"Default"`). 

---

### **Search Filters for Tasks**
Users can search Tasks using any combination of these criteria:
- **Title**
- **Category**
- **Priority**

---

## **Section 2: Storage and Retrieval of App Information**
- Data is stored in JSON files in the `medialab` directory.
- Decide  and  define  your  own  organization  (data  schema)  for  the JSON  data  and  the  set  of  files  that  will  be  used  to  store  the  App  data.  
- Implement  through  the  appropriate  classes  the  methods  that  will  allow you to retrieve the information that the files have and initialize the appropriate objects in your App and refresh the files so that the overall state can be maintained between intermittent runs of the App.

### **Logic to retrieve and update the App data**
- **Initialization**: You should be retrieving all the information in the JSON files and initializing the corresponding objects in your App at the same time.
- **Execution**: The  App  will  use  the  state  information  retrieved in  program  memory  during  initialization.  All  operations  related  to  Tasks  and Reminders managed by the app will be executed based on the information in program memory.
- **Termination**: JSON  files  with  system  state  information  will  be refreshed  exclusively  before  App  termination.  The  implementation shall store in the corresponding JSON files the total state of the App at the time of termination.

---

## **Section 3: Graphic User Interface (GUI)**
- The GUI implementation should ensure that when the App is terminated, the system information in the relevant files is updated.
- The frontend will have only one (central) window, divided in half.
- **Header**: On the top left of the central window, there should be the title/header `"MediaLab Assistant"`.
- **Layout**: Each half (top and bottom) will have 4 "cells" or areas (2x4 grid). The cells of the top half will be named for convinience 1, 2, 3 and 4 (from left to right), while the bottom's cells are 5, 6, 7 and 8 (from left to right).
 
### **Cells 1-4 (Top Half)**:
1. **Cell 1**: Total number of Tasks, regardless of `"Status"`.  
2. **Cell 2**: Total number of Tasks with `Status: "Completed"`.  
3. **Cell 3**: Tasks with status `Status: "Delayed"`.  
4. **Cell 4**: Tasks with `"Deadline"` within 7 days.  

### **Cells 5-8 (Bottom Half)**:
Each cell has a button that redirects to a page, in which the user will be able to utilize the according function.

- **Cell 5**: Task Management. The App must present the available Tasks by Category. The user can create, modify and delete Tasks (according to previous instrucions in Sections 1 and 2). The user can search with filters (according to previous instrucions in Section 1).
- **Cell 6**: Category Management. The App must present the available Categories. The user can create, modify and delete Categories (according to previous instrucions in Sections 1 and 2).
- **Cell 7**: Priority Management. The App must present the available Priorities. The user can create, modify and delete Priorities (according to previous instrucions in Sections 1 and 2).
- **Cell 8**: Reminder Management. The App must present the available Reminders. The user can create, modify and delete Reminders (according to previous instrucions in Sections 1 and 2).