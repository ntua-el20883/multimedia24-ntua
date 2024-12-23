# Checklist of things that I need to implement.
- The "(...)" means that an attribute can have values set by the user.

### ***Tasks***
- Task has the attributes: "Title", "Description", ***"Category"***, ***"Priority"***, ***"Deadline"***, ***"Status"***.
- User can edit all the Task's attributes, following the rules of each attribute that are explained below.
- When editing a Task or creating a new Task, "Title" field can't be empty.
- When editing a Task or creating a new Task, "Description" can be empty.

### ***Category***
- [x] Category has the attributes: "Name".
- [x] "Name" can take the values: "Default", (...).
- [x] If the "Default" Category does not exist at the start of the app, the "Default" Category must be automatically created.
- [x] If the user doesn't specify a Task's Category, the Task's Category must be set to "Default".
- [x] User can't edit/delete the "Default" Category.
- [x] User can't create a new Category with the same "Name" ("Work" and "work" are treated as equals).
- [x] If a Category is deleted, its associated Tasks must also deleted.
- [x] If a Category is renamed, its associated Tasks must have their "Category" attribute updated.

### ***Priority***
- [x] Priority has the attributes: "Name".
- [x] "Name" can take the values: "Default", (...).
- [x] If the "Default" Priority does not exist at the start of the app, the "Default" Priority must be automatically created.
- [x] If the user doesn't specify a Task's Priority, the Task's Priority must be set to "Default".
- [x] User can't edit/delete the "Default" Priority.
- [x] User can't create a new Priority with with the same "Name" ("High" and "high" are treated as equals).
- [x] If a Priority is deleted, its associated Tasks must also deleted.
- [x] If a Priority is renamed, its associated Tasks must have their "Priority" attribute updated. 

### ***Deadline***
- [x] Deadline has the attributes: "Date".
- [x] The user must specify a Task's Deadline.
- [x] "Date" must be a future date.

### ***Status***
- Status has the attributes: "Name".
- "Name" can take the values: "Open", "In Progress", "Postponed", "Delayed". 
- If the user doesn't specify a Task's Status, the Task's Status must be set to "Open".
- User can't edit/delete the Statuses.
- User can't create new Statuses.
- If the current date is past the Deadline, the Task's Status is automatically set to "Delayed".

### ***Reminder***
- Reminder has the attributes: "Date".
- User must select the Task for which the Reminder will be set (from a dropdown menu pulled from tasks.json).
- User must select the Date for which the Reminder will be triggered:
    - There should be a dropdown menu offering the options: "1 day before the Deadline", "1 week before the Deadline", "1 month before the Deadline", in which case, the Date should automatically be set depending on the option.
    - Otherwise, the Date can be any date before the Task's Deadline (and after the current date).
- User can edit all attributes of a Reminder.
- A Task can have multiple Reminders on different days, but can't have multiple Reminders on the same day.
- User can't add Reminders to Tasks with Status "Completed".
- If a Task passes to Status "Completed", its assosiated Reminders should be deleted.
- If a Task gets deleted, its assosiated Reminders should be deleted.