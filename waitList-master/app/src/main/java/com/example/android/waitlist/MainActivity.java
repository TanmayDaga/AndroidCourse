package com.example.android.waitlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.waitlist.data.TestUtils;
import com.example.android.waitlist.data.WaitListDbHelper;
import com.example.android.waitlist.data.WaitlistContract;

public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mSqLiteDatabase;

    private EditText mNewGuestEditText;
    private EditText mNewPArtySizeEditText;

    private final static String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;
        mNewGuestEditText = (EditText) findViewById(R.id.person_name_edit_text);
        mNewPArtySizeEditText = (EditText) findViewById(R.id.party_count_edit_text);

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an adapter for that cursor to display the data


        WaitListDbHelper dbHelper = new WaitListDbHelper(this);
        mSqLiteDatabase = dbHelper.getWritableDatabase();


        Cursor cursor = getAllGuests();

        mAdapter = new GuestListAdapter(this, cursor);


        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeGuest(id);
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView);

    }


    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {
        if (mNewGuestEditText.getText().length() == 0 &&
                mNewPArtySizeEditText.getText().length() == 0) {
            return;
        }
        int partySize = 1;
        try {
            partySize = Integer.parseInt(mNewPArtySizeEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(TAG, "Failed to convert party size");
        }
        addNewGuest(mNewGuestEditText.getText().toString(), partySize);
        mAdapter.swapCursor(getAllGuests());

        mNewPArtySizeEditText.clearFocus();
        mNewGuestEditText.getText().clear();
        mNewPArtySizeEditText.getText().clear();
    }

    private Cursor getAllGuests() {
        return mSqLiteDatabase.query(
                WaitlistContract.WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitListEntry.COLUMN_TIMESTAMP
        );
    }

    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();

        cv.put(WaitlistContract.WaitListEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitListEntry.COLUMN_PARTY_SIZE, partySize);

        return mSqLiteDatabase.insert(WaitlistContract.WaitListEntry.TABLE_NAME, null, cv);
    }

    private boolean removeGuest(Long id) {
        return mSqLiteDatabase.delete(WaitlistContract.WaitListEntry.TABLE_NAME,
                WaitlistContract.WaitListEntry._ID + "=" + id, null) > 0;
    }
}
