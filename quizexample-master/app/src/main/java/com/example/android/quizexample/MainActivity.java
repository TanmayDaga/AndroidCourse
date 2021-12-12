package com.example.android.quizexample;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtermsprovider.DroidTermsExampleContract;


/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */

public class MainActivity extends AppCompatActivity {

    // The current state of the app
    private int mCurrentState;


    private Button mButton;

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private final int STATE_HIDDEN = 0;

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private final int STATE_SHOWN = 1;

    private Cursor mData;

    private TextView mDefinitionTextView;
    private TextView mWordTextView;

    private int mDefCol, mWordCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the views
        mButton = (Button) findViewById(R.id.button_next);
        mWordTextView = (TextView) findViewById(R.id.text_view_word);
        mDefinitionTextView = (TextView) findViewById(R.id.text_view_definition);

        Uri myUri = DroidTermsExampleContract.CONTENT_URI;

        new WordFetch().execute();
    }

    /**
     * This is called from the layout when the button is clicked and switches between the
     * two app states.
     *
     * @param view The view that was clicked
     */
    public void onButtonClick(View view) {

        // Either show the definition of the current word, or if the definition is currently
        // showing, move to the next word.
        switch (mCurrentState) {
            case STATE_HIDDEN:
                showDefinition();
                break;
            case STATE_SHOWN:
                nextWord();
                break;
        }
    }

    public void nextWord() {

        if (mData != null) {
            if (!mData.moveToNext()) {
                mData.moveToFirst();

            }
        }
        mDefinitionTextView.setVisibility(View.INVISIBLE);
        // Change button text
        mButton.setText(getString(R.string.show_definition));
        mWordTextView.setText(mData.getString(mWordCol));
        mDefinitionTextView.setText(mData.getString(mDefCol));
        mCurrentState = STATE_HIDDEN;

    }

    public void showDefinition() {

        if (mData != null) {

            mDefinitionTextView.setVisibility(View.VISIBLE);
            mButton.setText(getString(R.string.next_word));

            mCurrentState = STATE_SHOWN;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mData.close();
    }

    public class WordFetch extends AsyncTask<Void, Void, Cursor> {


        @Override
        protected Cursor doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(DroidTermsExampleContract.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mData = cursor;
            if(mData!=null){
                mDefCol = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
                mWordCol = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD);
            }



            // Set initial state

        }

    }
}
