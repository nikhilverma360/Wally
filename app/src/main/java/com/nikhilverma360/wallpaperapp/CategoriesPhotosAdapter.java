package com.nikhilverma360.wallpaperapp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoriesPhotosAdapter extends RecyclerView.Adapter<CategoriesPhotosAdapter.CategoriesViewHolder> {

    private List<Categories_Model> mCategoriesArray;
    private Context mContext;


    public CategoriesPhotosAdapter(Context context, List<Categories_Model> categoriesArray) {
        super();
        mContext = context;
        mCategoriesArray = categoriesArray;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_photos_list, parent, false);

        return new CategoriesViewHolder(view, mContext, mCategoriesArray);

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
    public void onBindViewHolder(final CategoriesViewHolder holder, final int position) {

        Glide.with(mContext).load(mCategoriesArray.get(position).getImageId()).into(holder.image);
        holder.name.setText(mCategoriesArray.get(position).getCategory_name());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.mContext, DetailPhotoCategoriesActivity.class);
                intent.putExtra("category_name", mCategoriesArray.get(position).getCategory_name());
                intent.putExtra("category_image_id", mCategoriesArray.get(position).getImageId());
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
        if (mCategoriesArray != null) {
            return mCategoriesArray.size();
        } else {
            return 0;
        }
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        Context mContext;
        List<Categories_Model> arrayList;

        public CategoriesViewHolder(View itemView, Context context, List<Categories_Model> categoriesModelArrayList) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            image = itemView.findViewById(R.id.category_image);
            mContext = context;
            arrayList = categoriesModelArrayList;

        }
    }




}
