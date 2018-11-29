package com.nikhilverma360.wallpaperapp;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Random;

public class FullImageActivity extends AppCompatActivity {

    private String largeImageUrl;
    private ProgressBar progressBar;
    SimpleTarget<Bitmap> bitmapSimpleTarget;
    Button setButton;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        setContentView(R.layout.activity_full_image);


        TextView errorMessage = findViewById(R.id.error_msg);

        Intent intent = getIntent();
        largeImageUrl = intent.getStringExtra("fullImageUrl");
        //  int imageId = intent.getIntExtra("imageId", 3215188);

        //  images = new ArrayList<>();
        //   Image image = images.get(imageId);

        ImageView imageView = findViewById(R.id.largeImageView);
        progressBar = findViewById(R.id.loading_indicator);
        Button downloadButton = findViewById(R.id.download_btn);


      //  imageView.setOnTouchListener(new Touch());

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;


        if (connected) {
            Glide.with(getBaseContext())
                    .load(largeImageUrl)
                    .asBitmap()
                    // .override(700, 700)
                    //0 .placeholder(R.drawable.ic_launcher_background)
                   // .error(R.drawable.img2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
            errorMessage.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "Loading images pls wait", Toast.LENGTH_SHORT).show();
        } else {
            errorMessage.setVisibility(View.VISIBLE);
            Toast.makeText(getBaseContext(), "Pls check your internet connection", Toast.LENGTH_LONG).show();
        }
        //progressBar.setVisibility(View.VISIBLE);
      //  progressBar.setVisibility(View.GONE);


        setButton = findViewById(R.id.set_button);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create WallpaperManager instance
                Glide.with(getBaseContext())
                        .load(largeImageUrl)
                        .asBitmap()
                        // .override(700, 700)
                        //0 .placeholder(R.drawable.ic_launcher_background)
                      //  .error(R.drawable.img2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bitmapSimpleTarget = new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
                                try {
                                    wallpaperManager.setBitmap(resource);
                                    Toast.makeText(getBaseContext(), "Wallpaper Set", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }


        });


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new myAsyncTask().execute(largeImageUrl);

            }
        });


    }


    @SuppressLint("StaticFieldLeak")
    private  class myAsyncTask extends AsyncTask<String, Void, Bitmap> {


        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
          //  mProgressDialog.setTitle("Downloading");
           // mProgressDialog.setMessage("Pls wait, while downloading image");
            //mProgressDialog.show();
            super.onPreExecute();

        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                java.net.URL url = new java.net.URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                SaveImage(myBitmap);

                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param bitmap The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
          //  mProgressDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "Image downloaded in gallery", Toast.LENGTH_LONG).show();
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (root, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.

     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ((item.getItemId()) == R.id.menu) {

            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
            //item.setIntent(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}







