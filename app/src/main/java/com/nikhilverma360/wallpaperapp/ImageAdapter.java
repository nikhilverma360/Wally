package com.nikhilverma360.wallpaperapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {



     private Activity mContext;
     private List<Image> mImages;

    public ImageAdapter(Activity context, List<Image> images) {
        super();
        this.mContext = context;
        mImages = images;

    }



    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the ViewHolder#itemView to reflect the item at the given
     * position.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ImageAdapter.MyViewHolder holder, final int position) {

       Image image = mImages.get(position);


        Glide.with(mContext)
                .load(image.getWebFormatUrl())
                .asBitmap()
                .override(700, 700)
               //0 .placeholder(R.drawable.ic_launcher_background)
               // .error(R.drawable.applogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imagesImageView);

        holder.imagesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FullImageActivity.class);
                intent.putExtra("fullImageUrl", mImages.get(position).getLargeImageUrl());
                mContext.startActivity(intent);
            }
        });

    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (mImages != null) {
            return mImages.size();
        } else {
            return 0;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        final ImageView imagesImageView;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagesImageView = itemView.findViewById(R.id.imageView);
            progressBar = itemView.findViewById(R.id.indicator);
        }

    }


    public void setImageData(List<Image> imageData) {
        mImages = imageData;
        notifyDataSetChanged();
    }


    }






