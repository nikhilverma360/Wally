package com.nikhilverma360.wallpaperapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

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

        setContentView(R.layout.activity_splash);

        ImageView logoImageView = findViewById(R.id.logo);
        TextView pixabayTextView = findViewById(R.id.pixabay_tv);
        TextView descriptionTextView = findViewById(R.id.descp);
        TextView linkTextView = findViewById(R.id.descp2);
        Button button = findViewById(R.id.get_started);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(animation);
        pixabayTextView.startAnimation(animation);
        descriptionTextView.startAnimation(animation);
        linkTextView.startAnimation(animation);
        button.startAnimation(animation);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });






        pixabayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://pixabay.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });







    }
}
