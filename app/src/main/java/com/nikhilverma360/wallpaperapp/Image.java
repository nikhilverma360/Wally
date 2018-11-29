package com.nikhilverma360.wallpaperapp;



public class Image {

    private String mPreviewUrl;
    private String mWebFormatUrl;
    private String mLargeImageUrl;


    public Image(String previewUrl, String webFormatUrl, String largeImageUrl) {

        mPreviewUrl = previewUrl;
        mWebFormatUrl = webFormatUrl;
        mLargeImageUrl = largeImageUrl;

    }

    public String getPreviewUrl() {
        return mPreviewUrl;
    }

    public String getWebFormatUrl() {
        return mWebFormatUrl;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

}
