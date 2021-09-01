package com.example.todos;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.example.todos.data.TodoContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView mListView;
    FloatingActionButton fab;
    TodoCursorAdapter cursorAdapter;
    private static final int TODO_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CatalogActivity.this,EditorActivity.class));
            }
        });

        cursorAdapter  = new TodoCursorAdapter(this,null) ;

//        Setting list empty view
        mListView = findViewById(R.id.list_view);
        mListView.setEmptyView(findViewById(R.id.empty_view));
        mListView.setAdapter(cursorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = ContentUris.withAppendedId(TodoContract.TodoEntry.CONTENT_URL,l);
                startActivity(new Intent(CatalogActivity.this,EditorActivity.class).setData(uri));
            }
        });

        getLoaderManager().initLoader(TODO_LOADER,null,CatalogActivity.this);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projections = new String[]{
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_TITLE,
                TodoContract.TodoEntry.COLUMN_DESCRIPTION,
                TodoContract.TodoEntry.COLUMN_DUE_DATE
        };
        return new CursorLoader(this, TodoContract.TodoEntry.CONTENT_URL,projections,null,null,null);

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_todos:
                getContentResolver().delete(TodoContract.TodoEntry.CONTENT_URL,null);
                return true;
            default:
                throw new IllegalArgumentException("Cannot resolve"+item.getItemId());
        }

    }
}