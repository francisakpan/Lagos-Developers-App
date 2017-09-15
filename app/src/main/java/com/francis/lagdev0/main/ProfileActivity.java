package com.francis.lagdev0.main;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.francis.lagdev0.adapters.RepositoryAdapter;
import com.francis.lagdev0.data.Developer;
import com.francis.lagdev0.data.DeveloperContracts;
import com.francis.lagdev0.data.DeveloperContracts.DeveloperSchema;
import com.francis.lagdev0.data.RepoLength;
import com.francis.lagdev0.network.CheckNetworkConn;
import com.francis.lagdev0.network.LoadDevelopers;
import com.francis.lagdev0.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements LoaderCallbacks<List<Developer>>{

    //Declare views and Strings
    String repos_url, share_text;
    ProgressBar reposProgressBar;
    TextView emptyRepos, noOfRepo;
    RepositoryAdapter reposAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        noOfRepo = (TextView) findViewById(R.id.no_of_repo);

        //declare and set up a support toolsBar for profile activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolsBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get values sent in an intent
        final Intent intent = getIntent();
        final String url = intent.getStringExtra(DeveloperSchema.KEY_URL);
        String name = intent.getStringExtra(DeveloperSchema.KEY_NAME);
        String link = intent.getStringExtra(DeveloperSchema.KEY_IMAGE_URL);
        repos_url = intent.getStringExtra(DeveloperSchema.KEY_REPOS_URL);

        // the share intent text
        share_text = "Check out this awesome developer @" + name + ", " + url;

        // declaring the collapsing tools bar to set title
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout)findViewById(R.id.layout);
        layout.setTitleEnabled(true);
        layout.setTitle(name);

        // Using glide to display developers picture in profile activity
        ImageView profileImage = (ImageView)findViewById(R.id.imageView);
        Glide.with(getApplicationContext())
                .load(link)
                .thumbnail(Glide.with(ProfileActivity.this).load(link))
                .priority(Priority.IMMEDIATE)
                .placeholder(R.drawable.thumbnail)
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

        //set up floating action button to serve as a share button
        FloatingActionButton shareButton = (FloatingActionButton)findViewById(R.id.floatingButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        /* set developer url and allow for click event which loads the
            developer github page in a browser*/
        TextView gitUrlText = (TextView) findViewById(R.id.gitUrl);
        gitUrlText.setText(url);
        gitUrlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri devUri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, devUri);
                startActivity(intent);
            }
        });

        reposProgressBar = (ProgressBar) findViewById(R.id.repository_progressBar);
        reposAdapter = new RepositoryAdapter(this, new ArrayList<Developer>());
        emptyRepos = (TextView) findViewById(R.id.no_repository_text);
        ListView reposList = (ListView) findViewById(R.id.list_repos);
        reposList.setEmptyView(emptyRepos);
        reposList.setAdapter(reposAdapter);
        reposList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Developer currentRepos = reposAdapter.getItem(position);
                Uri dev_repos_uri = Uri.parse(currentRepos.getReposUrl());
                Intent dev_repos = new Intent(Intent.ACTION_VIEW, dev_repos_uri);
                startActivity(dev_repos);
            }
        });

        if (CheckNetworkConn.isConnected(this)){
            getSupportLoaderManager().initLoader(DeveloperContracts.REPOSITORY_LOADER_ID, null, this);
        }else{
            reposProgressBar.setVisibility(View.GONE);
            emptyRepos.setText(R.string.no_network);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                share();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* a function for share intent
        activated when the floating action button is clicked
     */
    private void share(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, share_text);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_text)));
    }


    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {
        return new LoadDevelopers(ProfileActivity.this, repos_url, DeveloperContracts.LOAD_REPOS, null);
    }

    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> data) {
        reposAdapter.clear();
        reposProgressBar.setVisibility(View.GONE);
        emptyRepos.setText(R.string.no_repository);

        if (data != null && !data.isEmpty()) {
            // get the number of repositories a developer has
            noOfRepo.setText(String.format(getString(R.string.num_of_repositories),
                    RepoLength.getLength()));
            reposAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        reposAdapter.clear();
    }
}
