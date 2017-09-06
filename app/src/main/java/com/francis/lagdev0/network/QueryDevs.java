package com.francis.lagdev0.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.francis.lagdev0.R;
import com.francis.lagdev0.data.CompareDev;
import com.francis.lagdev0.data.Developer;
import com.francis.lagdev0.data.DeveloperContracts;
import com.francis.lagdev0.data.RepoLength;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Francis on 09/08/2017.
 * A custom class that does all the network queries
 */

public class QueryDevs {

    private Context mContext;

    public QueryDevs(Context context){
        mContext = context;
    }
    private static final String LOG_TAG = QueryDevs.class.getSimpleName();

    public List<Developer> fetchJSONData(String requestUrl, String dir, String sort_by){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (Exception e){
            Log.e(LOG_TAG, "Problem making http request", e);
        }

        if (dir.equals(DeveloperContracts.LOAD_DEV))
            return extractDevelopers(jsonResponse, sort_by);

        return extractRepositories(jsonResponse);

    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null ){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromJson(inputStream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the developers Json file");
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromJson(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private List<Developer> extractDevelopers(String jsonFile, String sortString) {

        if (TextUtils.isEmpty(jsonFile)){
            return null;
        }
        // Create an empty ArrayList that we can start adding developers to

        List<Developer> Developers = new ArrayList<>();

        // Try to parse the jsonFile. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // build up a list of Developer objects with the corresponding data.
            JSONObject root = new JSONObject(jsonFile);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){

                JSONObject developer = items.getJSONObject(i);

                String imageUrl = developer.getString("avatar_url");
                String username = developer.getString("login");
                String url = developer.getString("html_url");
                String repos = developer.getString("repos_url");

                Developers.add(new Developer(imageUrl, username, url, repos));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the developers JSON results", e);
        }finally {

            //Using the preference settings value to sort arraylist
            String sortDir = (mContext).getString(R.string.settings_sort_by_default);
            if (sortString.equals(sortDir))
                Collections.sort(Developers, CompareDev.compareDevAscending);
            else
                Collections.sort(Developers, CompareDev.compareDevDescending);
        }
        // Return the list of developers
        return Developers;
    }

    private static List<Developer> extractRepositories(String jsonFile) {

        if (TextUtils.isEmpty(jsonFile)){
            return null;
        }
        // Create an empty ArrayList that we can start adding repositories to

        List<Developer> Repositories = new ArrayList<>();
        ArrayList<Developer> topRepo = new ArrayList<>(2);

        // Try to parse the jsonFile. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // build up a list of repository objects with the corresponding data.
            JSONArray root = new JSONArray(jsonFile);
            RepoLength.setRepoLength(root.length());

            for (int j = 0; j < root.length(); j++) {

                JSONObject repos = root.getJSONObject(j);

                JSONObject owner = repos.getJSONObject("owner");
                String username = owner.getString("login");
                String name = repos.getString("name");
                String des = repos.getString("description");
                String url = repos.getString("html_url");
                int star = repos.getInt("stargazers_count");

                Repositories.add(new Developer(username, name, des, url, star));


            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the repository JSON results", e);
        }finally {

            /*
             * Arranging the repository array list
             * So that it returns the highest starred as the first element
             */
            Collections.sort(Repositories, CompareDev.compareRepoDescending);

            for (int i = 0; i < 2; i++){
                topRepo.add(Repositories.get(i));
            }
            Repositories.clear();
        }
        // Return the list of repositories a developer
        return topRepo;
    }
}
