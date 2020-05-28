package com.example.android.newsfeed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String STORY_REQUEST_URL = "https://content.guardianapis.com/search?";

    /** Adapter for the list of stories */
    private StoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);

        ListView storyListView = (ListView) findViewById(R.id.list);
        TextView mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        storyListView.setEmptyView(mEmptyTextView);

        mAdapter = new StoryAdapter(this, new ArrayList<Story>());

        storyListView.setAdapter(mAdapter);

        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Story currentStory = mAdapter.getItem(position);

                Uri storyUri = Uri.parse(currentStory.getWebUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(1, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Uri.parse(STORY_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", "tech");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        View loading = findViewById(R.id.loading_indicator);
        loading.setVisibility(View.GONE);
        mAdapter.clear();
        TextView mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyTextView.setText(R.string.no_stories);
        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }
}