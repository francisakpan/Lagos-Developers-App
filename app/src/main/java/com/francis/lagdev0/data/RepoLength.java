package com.francis.lagdev0.data;

/**
 * Created by Francis on 05/09/2017.
 */

public class RepoLength {

    // No of repositories a developer has
    private static int length;

    // set the number when it queried by the QueryDevs class
    public static void setRepoLength(int len){
        length = len;
    }

    // return the number to the activity that requires it
    public static int getLength(){
        return length;
    }
}
