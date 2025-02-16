# Checklist of things that I need to implement.
- The "(...)" means that an attribute can have values set by the user.

### ***Tasks***
- Task has the attributes: "Title", "Description", "Category", "Priority", "Deadline", "Status".
- User can edit all the Task's attributes, following the rules of each attribute that are explained below:
    - "Title": must be unique. Can't be empty.
    - "Description": can't be empty.
    - "Category": a dropdown menu should be provided with the available Categories. See further instructions below.
    - "Priority": a dropdown menu should be provided with the available Priorities. See further instructions below.
    - "Deadline": can't be empty, but it can be a past date.
    - "Status": a dropdown menu should be provided with the available options: "Open", "In Progress", "Postponed", "Completed", "Delayed".
        - If the user does not select a Status option, it should be set to "Open".
        - When launching the app, Tasks that are not "Completed" and have passed the Deadline should be detected and the status should be automatically changed to "Delayed".

### ***Category***
- Category has the attributes: "Name".
- "Name" can take the values: "Default", (...).
- ***[JUAN]*** If the "Default" Category does not exist at the start of the app, the "Default" Category must be automatically created.
- ***[JUAN]*** If the user doesn't specify a Task's Category, the Task's Category must be set to "Default".
- ***[JUAN]*** User can't edit/delete the "Default" Category.
- ***[JUAN]*** User can't create a new Category with the same "Name" ("Work" and "work" are treated as equals).
- [x] If a Category is deleted, its associated Tasks must also be deleted.
    - In this case, associated Reminders with the Tasks deleted should also be deleted.
- [x] If a Category is renamed, its associated Tasks must have their "Category" attribute updated.

### ***Priority***
- Priority has the attributes: "Name".
- "Name" can take the values: "Default", (...).
- If the "Default" Priority does not exist at the start of the app, the "Default" Priority must be automatically created.
- If the user doesn't specify a Task's Priority, the Task's Priority must be set to "Default".
- User can't edit/delete the "Default" Priority.
- User can't create a new Priority with with the same "Name" ("High" and "high" are treated as equals).
- If a Priority is deleted, its associated Tasks must be set to the "Default" Priority.
- If a Priority is renamed, its associated Tasks must have their "Priority" attribute updated.

### ***Reminder***
- Reminder has the attributes: "Date".
- User must select the Task for which the Reminder will be set (from a dropdown menu pulled from tasks.json).
- User must select the Date for which the Reminder will be triggered:
    - There should be a dropdown menu offering the options: "1 day before the Deadline", "1 week before the Deadline", "1 month before the Deadline", in which case, the Date should automatically be set depending on the option.
    - Otherwise, the Date can be any date before the Task's Deadline.
- vUser can edit all attributes of a Reminder.
- ***[JUAN]*** A Task can have multiple Reminders on different days, but can't have multiple Reminders on the same day.
- User can't add Reminders to Tasks with Status "Completed".
- If a Task passes to Status "Completed", its assosiated Reminders should be deleted.
- If a Task gets deleted, its assosiated Reminders should be deleted.

### ***Search***
- The user will be able to search for tasks based on any combinations of the criteria: title, category and priority.