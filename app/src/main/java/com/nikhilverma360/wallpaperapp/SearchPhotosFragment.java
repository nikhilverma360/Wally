package com.nikhilverma360.wallpaperapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPhotosFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>> {

    private static String BASE_URL = "https://pixabay.com/api/?key=18343821-ea5a45b632d168d33955de45d&image_type=photo&per_page=200&q=";
    private static String FINAL_URL;
    private TextView errorMessageTextView;
    private TextView errorMessageTextViewTwo;
    private ProgressBar loadingIndicator;
    private List<Image> images = new ArrayList<>();
    private static final int LOADER_ID = 3;
    private ImageAdapter mImageAdapter;
    private RecyclerView searchRecyclerView;
    private static final String LOG_TAG = SearchPhotosFragment.class.getSimpleName();
    private SearchView mSearchView;
   // private Fragment context = SearchPhotosFragment.this;


    public SearchPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_photos, container, false);

      //  searchImageView = (ImageView) view.findViewById(R.id.search_image);

        errorMessageTextView = view.findViewById(R.id.error_message);
        loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        mSearchView = view.findViewById(R.id.edit_query_searchView);
        errorMessageTextViewTwo = view.findViewById(R.id.error_message_two);

        errorMessageTextViewTwo.setVisibility(View.GONE);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            hideBothTextViews();
           // errorMessageTextView.setVisibility(View.GONE);
            //errorMessageTextViewTwo.setVisibility(View.GONE);

        } else
            connected = false;
        showNoInternetTextView();




        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        searchRecyclerView = view.findViewById(R.id.recView);
        searchRecyclerView.setLayoutManager(layoutManager);
        //  mRecyclerview.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mImageAdapter = new ImageAdapter(getActivity(), images);
        searchRecyclerView.setAdapter(mImageAdapter);


       // loadingIndicator.setVisibility(View.GONE);
        getLoaderManager().initLoader(LOADER_ID, null, this);

           mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   loadingIndicator.setVisibility(View.VISIBLE);
                   restartLoader();
                   return false;
               }


               @Override
               public boolean onQueryTextChange(String newText) {

                   CharSequence queryString = mSearchView.getQuery();
                   String string = queryString.toString();

                   Uri builtUri = Uri.parse(BASE_URL)
                           .buildUpon()
                           .appendQueryParameter("q", string)
                           .build();

                 // String updatedUrl = builtUri.toString();
                   FINAL_URL = builtUri.toString();

                 //  FINAL_URL = BASE_URL+string;
                   Log.e(LOG_TAG, "Url Updated");

                   //restart the loader
                 // restartLoader();

                   return true;
               }
           });

        //  }
        //});


        return view;
    }

    private void showNoInternetTextView() {

        errorMessageTextView.setVisibility(View.VISIBLE);
        errorMessageTextViewTwo.setVisibility(View.GONE);
    }

    private void showNoPhotosTextView() {
        errorMessageTextViewTwo.setText(R.string.no_result);
        errorMessageTextViewTwo.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.GONE);
    }

    private void hideBothTextViews() {
        errorMessageTextView.setVisibility(View.GONE);
        errorMessageTextViewTwo.setVisibility(View.GONE);
    }

    private void restartLoader() {

        errorMessageTextViewTwo.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
       // if (mLoader == null) {
         //   errorMessageTextViewTwo.setVisibility(View.VISIBLE);
        //}
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

        errorMessageTextViewTwo.setVisibility(View.GONE);

        loadingIndicator.setVisibility(View.VISIBLE);
        return new ImageLoader(getContext(), FINAL_URL);
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

        loadingIndicator.setVisibility(View.GONE);

        if (data == null || data.isEmpty()) {
            Log.e(LOG_TAG, "null received");
            searchRecyclerView.setVisibility(View.GONE);
            showNoPhotosTextView();
            // errorMessageTextViewTwo.setText(R.string.no_result);
            //errorMessageTextViewTwo.setVisibility(View.VISIBLE);

        } else {

            mImageAdapter.setImageData(data);
            searchRecyclerView.setVisibility(View.VISIBLE);
            errorMessageTextViewTwo.setVisibility(View.GONE);
        }


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
        loadingIndicator.setVisibility(View.VISIBLE);

    }
}
