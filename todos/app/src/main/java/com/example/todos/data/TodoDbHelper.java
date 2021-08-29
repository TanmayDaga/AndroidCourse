package com.example.todos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.todos.data.TodoContract.TodoEntry;

public class TodoDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "work.db";
    public static final int DATABASE_VERSION = 1;
    public  TodoDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TODOS_TABLE = "CREATE TABLE "+TodoEntry.TABLE_NAME+"("+
                TodoEntry._ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TodoEntry.COLUMN_TITLE+" TEXT NOT NULL,"+
                TodoEntry.COLUMN_DESCRIPTION+" TEXT,"+
                TodoEntry.COLUMN_DUE_DATE+"TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
