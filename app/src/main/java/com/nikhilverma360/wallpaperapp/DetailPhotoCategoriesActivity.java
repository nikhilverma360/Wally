package com.nikhilverma360.wallpaperapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class DetailPhotoCategoriesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Image>> {

    private static final String BASE_URL = "https://pixabay.com/api/?key=18343821-ea5a45b632d168d33955de45d&image_type=photo&per_page=200&category=";
    private static  String FINAL_URL;
    private ImageAdapter mImageAdapter;
    private List<Image> images = new ArrayList<>();
    private static final int LOADER_ID = 2;
    private ProgressBar progressBar;
    private TextView errorMessage;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo_categories);

        errorMessage = findViewById(R.id.error_msg);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.GONE);
        }


        MobileAds.initialize(this, "ca-app-pub-2640711858338429~4762348389");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        // set a StaggeredGridLayoutManager with 2 number of columns and vertical orientation
    //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);


        RecyclerView mRecyclerview = findViewById(R.id.details_categories_rv);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        // mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView
        mRecyclerview.setHasFixedSize(true);

        mImageAdapter = new ImageAdapter(this, images);
        mRecyclerview.setAdapter(mImageAdapter);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.pb_loading_indicator);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("category_name");

        FINAL_URL = BASE_URL + categoryName;

        getSupportLoaderManager().initLoader(LOADER_ID,null, this);


       // String image_id = intent.getStringExtra("category_image_id");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if(actionBar !=null) {
            actionBar = this.getSupportActionBar();
            assert actionBar != null;
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(categoryName);
        }

    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (item.getItemId() == R.id.menu2) {

            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.refresh2) {

            progressBar.setVisibility(View.VISIBLE);
            //finish();
            //startActivity(intent);
            restartLoader();
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartLoader() {
        errorMessage.setVisibility(View.GONE);
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Image>> onCreateLoader(int id, Bundle args) {
       return new ImageLoader(this, FINAL_URL);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> data) {

        progressBar.setVisibility(View.GONE);
        mImageAdapter.setImageData(data);

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {
        mImageAdapter.setImageData(null);
    }


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
       return true;
    }



}

