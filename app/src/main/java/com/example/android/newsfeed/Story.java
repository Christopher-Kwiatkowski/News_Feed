package com.example.android.newsfeed;

public class Story {

    private String mSectionName;

    private String mWebPublicationDate;

    private String mWebTitle;

    private String mWebUrl;

    private String mAuthor;


    public Story(String sectionName, String webPublicationDate, String webTitle, String url, String author) {
        mSectionName = sectionName;
        mWebPublicationDate = webPublicationDate;
        mWebTitle = webTitle;
        mWebUrl = url;
        mAuthor = author;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
