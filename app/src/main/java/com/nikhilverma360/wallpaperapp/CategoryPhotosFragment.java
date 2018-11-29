package com.nikhilverma360.wallpaperapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryPhotosFragment extends Fragment {

    public CategoryPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_photos, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.categories_rv);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        ArrayList<Categories_Model> categories_photos_list = new ArrayList<>();
        categories_photos_list.add(new Categories_Model("fashion", R.drawable.fashion));
        categories_photos_list.add(new Categories_Model("nature", R.drawable.nature));
        categories_photos_list.add(new Categories_Model("backgrounds", R.drawable.background));
        categories_photos_list.add(new Categories_Model("science", R.drawable.science));
        categories_photos_list.add(new Categories_Model("education", R.drawable.education));
        categories_photos_list.add(new Categories_Model("people", R.drawable.people));
        categories_photos_list.add(new Categories_Model("feelings", R.drawable.feelings));
        categories_photos_list.add(new Categories_Model("religion", R.drawable.religion));
        categories_photos_list.add(new Categories_Model("health", R.drawable.health));
        categories_photos_list.add(new Categories_Model("places", R.drawable.places));
        categories_photos_list.add(new Categories_Model("animals", R.drawable.animals));
        categories_photos_list.add(new Categories_Model("industry", R.drawable.industry));
        categories_photos_list.add(new Categories_Model("food", R.drawable.food));
        categories_photos_list.add(new Categories_Model("computer", R.drawable.computer));
        categories_photos_list.add(new Categories_Model("sports", R.drawable.sports));
        categories_photos_list.add(new Categories_Model("transportation", R.drawable.transportation));
        categories_photos_list.add(new Categories_Model("travel", R.drawable.travel));
        categories_photos_list.add(new Categories_Model("buildings", R.drawable.buildings));
        categories_photos_list.add(new Categories_Model("business", R.drawable.business));
        categories_photos_list.add(new Categories_Model("music", R.drawable.music));

        CategoriesPhotosAdapter mCategoriesAdapter = new CategoriesPhotosAdapter(getContext(), categories_photos_list);

        recyclerView.setAdapter(mCategoriesAdapter);

        return view;

    }

}

