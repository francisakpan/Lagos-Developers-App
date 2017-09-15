package com.francis.lagdev0.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.francis.lagdev0.adapters.DeveloperAdapter;
import com.francis.lagdev0.data.Developer;
import com.francis.lagdev0.data.DeveloperContracts;
import com.francis.lagdev0.data.DeveloperContracts.DeveloperSchema;
import com.francis.lagdev0.fragments.AboutFragment;
import com.francis.lagdev0.network.CheckNetworkConn;
import com.francis.lagdev0.network.LoadDevelopers;
import com.francis.lagdev0.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Developer>>{

    TextView mEmpty;
    Snackbar snackbar;
    ImageView mNoNetwork;
    View loadingIndicator;
    DeveloperAdapter adapter;
    SwipeRefreshLayout refresh_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize snack bar and set action.
        snackbar = Snackbar.make(findViewById(R.id.activity_main),
                getString(R.string.no_network), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.setting_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));

        //Initialize swipe refresh layout and set listener
        refresh_me = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh_me.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);
        refresh_me.setOnRefreshListener(listener);

        //Initialize views
        loadingIndicator = findViewById(R.id.progressBar);
        ListView list = (ListView) findViewById(R.id.listDevelopers);
        mEmpty = (TextView) findViewById(R.id.empty_text);
        mNoNetwork = (ImageView) findViewById(R.id.no_network);

        //set adapter and attach it to list view
        adapter = new DeveloperAdapter(this, new ArrayList<Developer>());
        list.setAdapter(adapter);
        list.setEmptyView(mNoNetwork);
        list.setEmptyView(mEmpty);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Developer currentDev = adapter.getItem(position);
                Bundle args = new Bundle();
                args.putString(DeveloperSchema.KEY_NAME, currentDev.getmDevName());
                args.putString(DeveloperSchema.KEY_IMAGE_URL, currentDev.getmImageUrl());
                args.putString(DeveloperSchema.KEY_URL, currentDev.getmDevUrl());
                args.putString(DeveloperSchema.KEY_REPOS_URL, currentDev.getmDevReposUrl());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        //Check for network connection using A custom class CheckNetworkConn.
        if (CheckNetworkConn.isConnected(this)){

            //initialize the loader
            getSupportLoaderManager().initLoader(DeveloperContracts.DEVELOPERS_LOADER_ID, null, this);
        }
        else{

            // if no network set loading view to inform
            loadingIndicator.setVisibility(View.GONE);
            mEmpty.setVisibility(View.GONE);
            mNoNetwork.setImageResource(R.drawable.no_network);
            snackbar.show();
        }

    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //refresh the loader
            if (CheckNetworkConn.isConnected(MainActivity.this)) {
                refresh_me.setRefreshing(true);
                getSupportLoaderManager().initLoader(DeveloperContracts.DEVELOPERS_LOADER_ID, null,
                        MainActivity.this);
                snackbar.dismiss();
                return;
            }

            refresh_me.setRefreshing(false);
            if (!snackbar.isShown()) {
                snackbar.show();
            }
        }
    };

    /**
     *  set menu in action bar
     * @param menu view to inflate
     * @return the inflated menu view
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     *
     * @param item each item/action on the menu
     * @return selected item in the action bar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                finish(); //exit app
                return true;
            case R.id.settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.about:
                AboutFragment ab = new AboutFragment();
                ab.setCancelable(false);
                ab.show(getSupportFragmentManager(), "About");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        if (!CheckNetworkConn.isConnected(this)){  //Check for network connection to
            snackbar.show();                     // if no network display snackBar
            mEmpty.setVisibility(View.GONE);
            mNoNetwork.setVisibility(View.VISIBLE);
            mNoNetwork.setImageResource(R.drawable.no_network);
        }
        super.onResume();
    }

    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sort_by = sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        /*return Developers from the api call
          @param DeveloperContracts.JSON_RESPONSE_URL the api url
          @param DeveloperContracts.LOAD_DEV loads developers using the JSON_RESPONSE_URL
          @param sort_by set the preference value to the
         */
        return new LoadDevelopers(MainActivity.this, DeveloperContracts.JSON_RESPONSE_URL,
                DeveloperContracts.LOAD_DEV, sort_by);
    }

    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> data) {
        adapter.clear(); //clear adapter before populating it with data

        // set progress bar to not display
        loadingIndicator.setVisibility(View.GONE);

        // stop refreshing.
        refresh_me.setRefreshing( false );

        // set empty listView textView visibility as GONE
        mEmpty.setVisibility(View.GONE);

        if (CheckNetworkConn.isConnected(this)) {
            // set no network image to not display
            mNoNetwork.setVisibility(View.GONE);

            //set no developer text to display since no developer was return
            mEmpty.setVisibility(View.VISIBLE);
            mEmpty.setText(R.string.no_developer);

            // Check if data is available and populate the adapter
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        // Clear adapter on loader reset
        adapter.clear();
    }

}
