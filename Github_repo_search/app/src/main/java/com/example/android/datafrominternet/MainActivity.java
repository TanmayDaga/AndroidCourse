package com.example.android.datafrominternet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplayTextview;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSearchBoxEditText = (EditText) findViewById(R.id.search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplayTextview = (TextView) findViewById(R.id.tvShowError);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.PBLoadingIndicator);


    }

    private void showJson(){
        mErrorMessageDisplayTextview.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);

    }
    private void showErrorMessage(){
        mErrorMessageDisplayTextview.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }

    private void makeGithubSearchQuery(){
        String githubSearchQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubSearchQuery);
        if(TextUtils.isEmpty(githubSearchUrl.toString())){
            mUrlDisplayTextView.setText("No url can be formed");
        }
        else {
            mUrlDisplayTextView.setText(githubSearchUrl.toString());
            new GithubQueryClass().execute(githubSearchUrl);
        }





    }

    public class GithubQueryClass extends AsyncTask<URL, Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResult = null;
            try {
                githubSearchResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResult;
        }

        @Override
        protected void onPostExecute(String s) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(s)){

                mSearchResultsTextView.setText(s);
                showJson();
            }
            else {
                showErrorMessage();
            }
        }
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int itemThatWasClickedId = item.getItemId();
       if (itemThatWasClickedId == R.id.action_search){
           makeGithubSearchQuery();

           return true;

       }
        return super.onOptionsItemSelected(item);
    }
}