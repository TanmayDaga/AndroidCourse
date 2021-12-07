package com.example.android.waitlist.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.example.android.waitlist.data.WaitlistContract.*;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static void insertFakeData(SQLiteDatabase sqLiteDatabase){
        if (sqLiteDatabase == null){
            return;
        }
        List<ContentValues> list = new ArrayList<ContentValues>();
        ContentValues cv = new ContentValues();
        cv.put(WaitListEntry.COLUMN_GUEST_NAME,"John");
        cv.put(WaitListEntry.COLUMN_PARTY_SIZE,"12");
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitListEntry.COLUMN_GUEST_NAME,"Tim");
        cv.put(WaitListEntry.COLUMN_PARTY_SIZE,"2");
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitListEntry.COLUMN_GUEST_NAME,"Jessica");
        cv.put(WaitListEntry.COLUMN_PARTY_SIZE,"99");
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitListEntry.COLUMN_GUEST_NAME,"Larry");
        cv.put(WaitListEntry.COLUMN_PARTY_SIZE,"1");
        list.add(cv);

        cv = new ContentValues();
        cv.put(WaitListEntry.COLUMN_GUEST_NAME,"Kim");
        cv.put(WaitListEntry.COLUMN_PARTY_SIZE,"45");
        list.add(cv);

        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.delete(WaitListEntry.TABLE_NAME,null,null);

        }
    }
}
