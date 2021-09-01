package com.example.todos;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.todos.data.TodoContract;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor c ) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.titleOfTodo)).setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_TITLE)));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry.COLUMN_DESCRIPTION));

        ((TextView) view.findViewById(R.id.descriptionOfTodo)).setText(!(TextUtils.isEmpty(description))?description:context.getString(R.string.no_description));
    }
}
