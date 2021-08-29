package com.example.todos.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TodoContract {
    public TodoContract() { }

    public static final String CONTENT_AUTHORITY  = "com.example.todos";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_TODOS = "todos";
    public static final class TodoEntry implements BaseColumns{
        public static final Uri CONTENT_URL = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_TODOS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_TODOS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.ANY_CURSOR_ITEM_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_TODOS;
        public static final String TABLE_NAME = "todos";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DUE_DATE = "date";
    }
}
