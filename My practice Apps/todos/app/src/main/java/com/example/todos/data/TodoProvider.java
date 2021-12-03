package com.example.todos.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.todos.data.TodoContract.TodoEntry;

import java.net.URI;

public class TodoProvider extends ContentProvider {

    private static final int TODOS = 100; // Puri table
    private static final int TODOS_ID = 101; // Only one link
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String LOG_TAG = TodoProvider.class.getSimpleName();

    static {
        mUriMatcher.addURI(TodoContract.CONTENT_AUTHORITY, TodoContract.PATH_TODOS, TODOS);// full table operation

        // # -> wildcard for numeric value, * wildcard for string->
        mUriMatcher.addURI(TodoContract.CONTENT_AUTHORITY, TodoContract.PATH_TODOS + "/#", TODOS_ID); // single table operation
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

        switch (mUriMatcher.match(uri)) {
            case TODOS:
                cursor = db.query(TodoEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder);
                break;
            case TODOS_ID:
                selection = TodoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TodoEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri" + uri + "cannot be queried");

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case TODOS:
                return TodoEntry.CONTENT_LIST_TYPE;
            case TODOS_ID:
                return TodoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Cannot get type of uri" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String title = contentValues.getAsString(TodoEntry.COLUMN_TITLE);
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        switch (mUriMatcher.match(uri)) {
            case TODOS:
                return insertTodos(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for uri" + uri);
        }

    }

    private Uri insertTodos(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(TodoEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Insertion failed");
            return null;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (mUriMatcher.match(uri)) {
            case TODOS:
                return deleteTodos(uri, selection, selectionArgs);

            case TODOS_ID:
                selection = TodoEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteTodos(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion not supported for uri " + uri);
        }
    }

    private int deleteTodos(Uri uri, String selections, String[] selectionArgs) {
        int rowsDeleted = mDbHelper.getWritableDatabase().delete(TodoEntry.TABLE_NAME, selections, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (mUriMatcher.match(uri)){
            case TODOS:
                return updateTodos(uri,contentValues,s,strings);
            case TODOS_ID:
                s = TodoEntry._ID+"=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateTodos(uri,contentValues,s,strings);
            default:
                throw new IllegalArgumentException("Cannot perform updation for uri "+uri);
        }
    }

    private int updateTodos(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (TextUtils.isEmpty(contentValues.getAsString(TodoEntry.COLUMN_TITLE))){
            throw new IllegalArgumentException("title required");
        }

        int rowsID =  mDbHelper.getWritableDatabase().update(TodoEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        if (rowsID != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsID;

    }
}
