package com.example.android.datafrominternet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.loader.content.Loader;
import androidx.loader.app.LoaderManager;

import androidx.loader.content.AsyncTaskLoader;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int GITHUB_SEARCH_LOADER = 22;

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;

    private TextView mErrorMessageDisplayTextview;

    private ProgressBar mLoadingIndicator;

    private static final String SEARCH_QUERY_EXTRA = "query";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSearchBoxEditText = (EditText) findViewById(R.id.search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplayTextview = (TextView) findViewById(R.id.tvShowError);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.PBLoadingIndicator);

        if (savedInstanceState!=null){
            if (savedInstanceState.containsKey(SEARCH_QUERY_EXTRA)){
                mUrlDisplayTextView.setText(savedInstanceState.getString(SEARCH_QUERY_EXTRA));

            }

        }
        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER,null,this);


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
        if (TextUtils.isEmpty(githubSearchQuery)){
            mUrlDisplayTextView.setText("No query entered, nothing to search for");
            return;
        }
        URL githubSearchUrl = NetworkUtils.buildUrl(githubSearchQuery);
        if(TextUtils.isEmpty(githubSearchUrl.toString())){
            mUrlDisplayTextView.setText("No url can be formed");
        }
        else {
            mUrlDisplayTextView.setText(githubSearchUrl.toString());

        }

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA,githubSearchQuery.toString());
        LoaderManager loaderManager = getSupportLoaderManager();
       Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        if (githubSearchLoader == null){
            loaderManager.initLoader(GITHUB_SEARCH_LOADER,queryBundle,this);
        }
        else {
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER,queryBundle,this);
        }




    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            String mGithubJson;
            @Override
            protected void onStartLoading() {
                if (bundle == null){
                    return;
                }

                if (mGithubJson != null){
                    deliverResult(mGithubJson);
                }
                else{
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();}
            }


            @Override
            public String loadInBackground() {
                   String searchUrl = bundle.getString(SEARCH_QUERY_EXTRA);
                   String jsonForm = null;
                   if (!TextUtils.isEmpty(searchUrl)){
                       try {
                           jsonForm = NetworkUtils.getResponseFromHttpUrl(new URL(searchUrl));
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       return null;
                   }
                   return null;

            }

            @Override
            public void deliverResult(@Nullable String data) {
                super.deliverResult(data);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (s != null && !s.equals("")){
            showJson();
            mSearchResultsTextView.setText(s);
        }
        else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //Not using now
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_EXTRA,mUrlDisplayTextView.getText().toString());

    }
}