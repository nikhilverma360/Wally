package com.nikhilverma360.wallpaperapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{


    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new AllPhotosFragment();
        } else if (position == 1){
            return new CategoryPhotosFragment();
        } else {
            return new SearchPhotosFragment();
        }

    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {

        String title;

        if (position == 0) {
            title = "All";
        } else if (position == 1){
            title = "Category";
        } else {
            title = "";
        }

        return title;

    }
}
