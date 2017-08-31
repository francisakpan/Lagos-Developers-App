package com.francis.lagdev0.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.francis.lagdev0.data.Developer;
import com.francis.lagdev0.R;

import java.util.ArrayList;

/**
 * Created by Francis on 15/08/2017.
 */

public class RepositoryAdapter extends ArrayAdapter<Developer> {

    public RepositoryAdapter(Activity context, ArrayList<Developer> repos){
        super(context, 0, repos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_repositories, parent, false);
        }

        Developer repository = getItem(position);

        TextView reposName = (TextView)convertView.findViewById(R.id.repos_name);
        reposName.setText(repository.getReposName());

        TextView reposDescription = (TextView)convertView.findViewById(R.id.repos_description);
        reposDescription.setText(repository.getDescription());

        TextView starCount = (TextView)convertView.findViewById(R.id.star_count);
        starCount.setText(String.valueOf(repository.getStarCount()));

        return convertView;
    }
}
