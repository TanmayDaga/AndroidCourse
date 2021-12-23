package com.example.android.todolist.database;

import android.provider.BaseColumns;

public class TaskContract {

    public static class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_PRIORITY = "priority";

    }
}
