package com.example.roomexampleapp.Entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemClass {

    @PrimaryKey
    @NonNull private Long id;
    private String taskName;
    private String taskDesc;

    @Ignore
    public ItemClass(@NonNull Long id, String taskName, String taskDesc) {
        this.id = id;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public ItemClass()
    {
        this.taskName = "taskName";
        this.taskDesc = "taskDesc";
    }


}
