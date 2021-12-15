package com.example.android.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.sunshine.utilities.SunshineDateUtils;

public class WeatherProvider extends ContentProvider {

    private WeatherDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    /*These constant will be used to match URIs with the data they are looking for. We will take
    advantage of the UriMatcher class to make tha matching much easier than doing something ourselves,
    such as using regular expression;
    */
    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;
//        content://com.example.android.sunshine/weather/
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, CODE_WEATHER);
//        content://com.example.android.sunshine/weather/1472214172
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_WEATHER: {
                db.beginTransaction();
                int rowInserted = 0;

                try {
                    for (ContentValues value : contentValues) {
                        long weatherDate = value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("This date must be normalise" + String.valueOf(weatherDate));
                        }
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            rowInserted += 1;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
//            content://com.example.android.sunshine/weather/1472214172
            case CODE_WEATHER_WITH_DATE: {
                String normaliseUtcDateString = uri.getLastPathSegment();
                String[] selectionArguement = new String[]{normaliseUtcDateString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " =?",
                        selectionArguement,
                        null, null, sortOrder
                );

                break;
            }
            case CODE_WEATHER: {
                cursor = mOpenHelper.getWritableDatabase().query(WeatherContract.WeatherEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException(" we are not implementing get type in sunshine");
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        throw new RuntimeException(" we are not implementing insert in sunshine");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] stringsArgs) {
        int numRowsDeleted;
        if(null == selection) selection = "1";
        switch (sUriMatcher.match(uri)){
            case CODE_WEATHER:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        WeatherContract.WeatherEntry.TABLE_NAME,selection,stringsArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        if(numRowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new RuntimeException(" we are not implementing update in sunshine");
    }
}
