package com.example.todos;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Build;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import android.content.CursorLoader;
import com.example.todos.data.TodoContract.TodoEntry;
import android.database.Cursor;
import android.net.Uri;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_TODO_LOADER = 0;
    private CalendarView mCalendarView;
    private TextView mTitleTextView;
    private TextView mDescTextView;
    private Uri mCurrentPetUri;
    private boolean mTodoHasChanged = false;

//    Creating touch listeners for different views
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mTodoHasChanged = true;
        return false;
    }
};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        mCurrentPetUri = getIntent().getData();


        mCalendarView = findViewById(R.id.calendarView);
        mTitleTextView = findViewById(R.id.editTextTitle);
        mDescTextView = findViewById(R.id.descriptionEditText);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                calendarView.setDate(calendar.getTimeInMillis());
            }
        });



        // Setting minimum date of calendar view
        if (mCurrentPetUri == null){
//            mCalendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
            setTitle("Add a Todo");
            invalidateOptionsMenu();

        }
        else {
            setTitle("Edit Todo");
            getLoaderManager().initLoader(EXISTING_TODO_LOADER,null,this);
        }
        mTitleTextView.setOnTouchListener(mOnTouchListener);
        mCalendarView.setOnTouchListener(mOnTouchListener);
        mDescTextView.setOnTouchListener(mOnTouchListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
          TodoEntry._ID,
          TodoEntry.COLUMN_TITLE,
          TodoEntry.COLUMN_DESCRIPTION,
          TodoEntry.COLUMN_DUE_DATE
        };

        return new CursorLoader(this,mCurrentPetUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if (data == null){
            return;
        }

        if (data.moveToNext()){
            mTitleTextView.setText(data.getString(data.getColumnIndexOrThrow(TodoEntry.COLUMN_TITLE)));
            mDescTextView.setText(data.getString(data.getColumnIndexOrThrow(TodoEntry.COLUMN_DESCRIPTION)));
            mCalendarView.setDate(Long.parseLong(data.getString(data.getColumnIndexOrThrow(TodoEntry.COLUMN_DUE_DATE))));

        }
         }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mTitleTextView.setText("");
        mDescTextView.setText("");
        mCalendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentPetUri == null) {
            menu.findItem(R.id.delete_todo).setVisible(false);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.save:
                saveTodo();

                break;
            case R.id.delete_todo:
                sureToDeleteTodoDialogBox();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void saveTodo(){
        ContentValues values = new ContentValues();
        String title  = mTitleTextView.getText().toString().trim();
        if (TextUtils.isEmpty(title)){
            Toast.makeText(this,"Title cannot be Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(TodoEntry.COLUMN_TITLE,title);
        values.put(TodoEntry.COLUMN_DESCRIPTION,mDescTextView.getText().toString().trim());
        values.put(TodoEntry.COLUMN_DUE_DATE,String.valueOf(mCalendarView.getDate()));

        if (mCurrentPetUri == null){
            Uri newUri = getContentResolver().insert(TodoEntry.CONTENT_URL,values);
            if (newUri == null) {
                Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Insertion is successful", Toast.LENGTH_SHORT);
            }
        }
        else {
            int rowsAffected = getContentResolver().update(mCurrentPetUri,values,null);
            if (rowsAffected == 0){
                Toast.makeText(EditorActivity.this,"Updation failed",Toast.LENGTH_SHORT);
            }
            else {
                Toast.makeText(EditorActivity.this,"Updation Successful",Toast.LENGTH_SHORT);
            }

        }
        finish();

    }

    @Override
    public void onBackPressed() {

        if (mTodoHasChanged && !TextUtils.isEmpty(mCurrentPetUri.toString())){
            showUnsavedChanges();
            return;
        }
        super.onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void deleteTodo(){
        if (mCurrentPetUri !=null){
        getContentResolver().delete(mCurrentPetUri,null);
    }}


    private void sureToDeleteTodoDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage("Deletion Cannot be undoed");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTodo();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null){
                dialogInterface.dismiss();
            }}
        });
        builder.create().show();

    }

    private void showUnsavedChanges(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage("Do you want to save changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveTodo();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });
        builder.create().show();

    }
}