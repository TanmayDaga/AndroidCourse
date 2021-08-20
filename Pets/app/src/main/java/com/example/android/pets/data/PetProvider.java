package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.android.pets.data.PetContract.PetEntry;

import java.net.URI;
import java.sql.RowId;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    private static final int PETS = 100; // Puri table
    private static final int PETS_ID = 101; // Only one link
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // default value


    // runs first when something called from class
    static {
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);// full table operation

        // # -> wildcard for numeric value, * wildcard for string->
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PETS_ID); // single table operation
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();


    PetDbHelper mDbHelper = new PetDbHelper(getContext());
    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {


        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case PETS:
                cursor = db.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PETS_ID:
                selection  = PetEntry._ID+ "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))}; // parsing "/#"
                cursor = db.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri"+uri);

        }


        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // checking name not null
        String name  = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null){
            throw new IllegalArgumentException("Pet name required");
        }


        // Checking if valid gender
        Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender) ){
            throw  new IllegalArgumentException("Invalid gender");
        }

        // Checking weight
        Integer weight= contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight == null ||  weight<0){
            throw  new IllegalArgumentException("Invalid pet weight");
        }

        switch (uriMatcher.match(uri)){
            case PETS:
                return insertPets(uri,contentValues);
            default:
                throw new IllegalArgumentException("insertion is not supported"+uri);
        }


    }
    private Uri insertPets(Uri uri,ContentValues contentValues){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(PetEntry.TABLE_NAME,null,contentValues);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert");
            return null;
        }
        return ContentUris.withAppendedId(uri,id);

    }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)){
            case PETS:
                return updatePets(uri,contentValues,selection,selectionArgs);
            case PETS_ID:
                // in this case selection will be from id and the particular id will be updated
                selection = PetEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePets(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for "+ uri);


        }
    }

    private int updatePets(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs){

        if (contentValues.containsKey(PetEntry.COLUMN_PET_NAME)){
            String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null){throw new IllegalArgumentException("Pet name required");}
        }

       if (contentValues.containsKey(PetEntry.COLUMN_PET_GENDER)){
           Integer gender  =contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
           if (gender == null || !PetEntry.isValidGender(gender)){
               throw new IllegalArgumentException("Gender required");
           }
       }
       if (contentValues.containsKey(PetEntry.COLUMN_PET_WEIGHT)){
           Integer weight= contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
           if (weight == null ||  weight<0){
               throw  new IllegalArgumentException("Invalid pet weight");
           }
       }
       if (contentValues.size() == 0){
           return 0;
       }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
       return db.update(PetEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }
    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case PETS:
                return db.delete(PetEntry.TABLE_NAME,selection,selectionArgs);
            case PETS_ID:
                selection = PetEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(PetEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for"+uri);
        }
    }


    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PETS_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Cannot fetch type for"+uri);
        }


    }
}