package com.example.android.todolist.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.todolist.database.TaskContract.TaskEntry;

public class TaskProvider extends ContentProvider {

    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        UriMatcher UriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);
        UriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS);
        UriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID);
        return UriMatcher;
    }

    private TaskDbHelper mTaskDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase database = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case TASKS:
                long id = database.insert(TaskEntry.TABLE_NAME,
                        null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskEntry.CONTENT_URI, id);

                } else {
                    throw new SQLException("Failed to insert row");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projections, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase database = mTaskDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case TASKS:
                cursor = database.query(TaskEntry.TABLE_NAME,
                        projections, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);


        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;


    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        final SQLiteDatabase database = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        switch (match) {
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = database.delete(TaskEntry.TABLE_NAME, "-id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri" + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
