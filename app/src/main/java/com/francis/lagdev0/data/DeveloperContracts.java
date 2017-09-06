package com.francis.lagdev0.data;

/**
 * Created by Francis on 15/08/2017.
 * Class static final Strings used in the application
 */

public class DeveloperContracts {

    public DeveloperContracts(){
        //Empty constructor
    }

    public static final String JSON_RESPONSE_URL =
            "https://api.github.com/search/users?q=location:lagos+language:java";
    public static final int DEVELOPERS_LOADER_ID = 1;
    public static final int REPOSITORY_LOADER_ID = 2;
    public static final String LOAD_DEV = "developers";
    public static final String LOAD_REPOS = "repositories";

    public static final class DeveloperSchema{

        public static String KEY_IMAGE_URL = "imageUrl";
        public static final String KEY_NAME = "username";
        public static final String KEY_URL = "url";
        public static final String KEY_REPOS_URL = "reposUrl";

    }

}
