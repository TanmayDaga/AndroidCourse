package com.example.android.todolist;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.todolist.database.AppDatabase;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;

    public AddTaskViewModelFactory (AppDatabase database, int TaskId)
    {
        mDb = database;
        mTaskId = TaskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass)
    {
        return (T) new AddTaskViewModel(mDb,mTaskId);
    }
}
