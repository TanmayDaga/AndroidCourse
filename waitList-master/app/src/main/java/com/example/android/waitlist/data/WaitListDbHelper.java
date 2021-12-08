package com.example.android.waitlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.waitlist.data.WaitlistContract.*;


public class WaitListDbHelper extends SQLiteOpenHelper {

    private static final  String DATABASE_NAME = "waitlist.db";
    private static final int DATABASE_VERSION = 1;

    public WaitListDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = " CREATE TABLE "+WaitListEntry.TABLE_NAME+
                "("+WaitListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                WaitListEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, "+
                WaitListEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, "+
                WaitListEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT  CURRENT_TIMESTAMP"+"); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ WaitListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
