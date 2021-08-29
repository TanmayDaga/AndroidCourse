package com.example.todos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.todos.data.TodoContract.TodoEntry;

import java.net.URI;

public class TodoProvider extends ContentProvider {

    private static final int TODOS = 100; // Puri table
    private static final int TODOS_ID = 101; // Only one link
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(TodoContract.CONTENT_AUTHORITY,TodoContract.PATH_TODOS,TODOS);// full table operation

        // # -> wildcard for numeric value, * wildcard for string->
        mUriMatcher.addURI(TodoContract.CONTENT_AUTHORITY,TodoContract.PATH_TODOS+"/#",TODOS_ID); // single table operation
    }


    TodoDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new TodoDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (mUriMatcher.match(uri)){
            case TODOS:
                cursor  = db.query(TodoEntry.TABLE_NAME,projections,selection,selectionArgs,null,null,sortOrder);
                break;
            case TODOS_ID:
                selection = TodoEntry._ID+"?=";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=  db.query(TodoEntry.TABLE_NAME,projections,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri"+uri+"cannot be queried");

        }

        
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
