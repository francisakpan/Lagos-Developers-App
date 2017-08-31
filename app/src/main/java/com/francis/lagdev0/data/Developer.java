package com.francis.lagdev0.data;

/**
 * Created by Francis on 08/08/2017.
 */

public class Developer{

    private String mImageUrl;
    private String mDevName;
    private String mDevUrl;
    private String mDevReposUrl;
    private String reposName;
    private String description;
    private String reposUrl;
    private int stars;

    /**
     * @param imgUrl developers image url
     * @param username developers username
     * @param devUrl developers url
     * @param devReposUrl developers repositories url
     */
    public Developer(String imgUrl, String username, String devUrl, String devReposUrl){
        mImageUrl = imgUrl;
        mDevName = username;
        mDevUrl = devUrl;
        mDevReposUrl = devReposUrl;
    }

    /**
     *
     * @param username developers username
     * @param name repository name
     * @param des repository description
     * @param url repository url
     * @param count repository star count
     */
    public Developer(String username, String name, String des, String url, int count){
        mDevName = username;
        reposName = name;
        description = des;
        reposUrl = url;
        stars = count;
    }

    // getter methods
    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmDevName() {
        return mDevName;
    }

    public String getmDevUrl() {
        return mDevUrl;
    }

    public String getmDevReposUrl(){
        return mDevReposUrl;
    }

    public String getReposName(){
        return reposName;
    }

    public String getDescription() {
        return description;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public int getStarCount() {
        return stars;
    }

}
