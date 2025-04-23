package com.example.hoang_movie.login;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TasksResponse {

    @SerializedName("tasks")
    private List<Task> tasks;

    // Constructor, getters và setters
    public TasksResponse() {}

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
