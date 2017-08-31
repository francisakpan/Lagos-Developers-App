package com.francis.lagdev0.main;

import android.app.Activity;
import android.content.Context;import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.francis.lagdev0.data.Developer;
import com.francis.lagdev0.data.DeveloperContracts.DeveloperSchema;
import com.francis.lagdev0.R;

import java.util.ArrayList;

/**
 * Created by Francis on 08/08/2017.
 * A custom developer adaper class
 * Used to puplate the list view in the main activity
 */

public class DeveloperAdapter extends ArrayAdapter<Developer>{
    private Context mContext; //declare context

    public DeveloperAdapter(Activity context, ArrayList<Developer> developers){
        super(context, 0, developers);
        mContext = context; // initialize context
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the listView to display on the parent layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_developers, parent, false);
        }

        // get a developer object at a position
        final Developer currentDeveloper = getItem(position);

        //declare fragment manager
        final FragmentManager fm = ((MainActivity)mContext).getSupportFragmentManager();

        //declare dev image view to display a developers image in main activity
        final ImageView avatar = (ImageView) convertView.findViewById(R.id.dev_image);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogFragment im = new ImageDialogFragment();
                Bundle args = new Bundle();
                args.putString(DeveloperSchema.KEY_NAME, currentDeveloper.getmDevName());
                args.putString(DeveloperSchema.KEY_IMAGE_URL, currentDeveloper.getmImageUrl());
                args.putString(DeveloperSchema.KEY_URL, currentDeveloper.getmDevUrl());
                args.putString(DeveloperSchema.KEY_REPOS_URL, currentDeveloper.getmDevReposUrl());
                im.setArguments(args);
                im.setCancelable(true);
                im.show(fm, getContext().getResources().getString(R.string.name_dialog));
            }
        });

        // Using the Glide library for image lifting
        Glide.with(getContext())
                .load(currentDeveloper.getmImageUrl())
                .asBitmap()
                .animate(android.R.anim.fade_in)
                .placeholder(R.drawable.no_picture)
                .error(R.drawable.no_picture)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(new BitmapImageViewTarget(avatar){ // make image circular
                    @Override
                    protected void setResource(Bitmap resource){
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(
                                getContext().getResources(), Bitmap.createScaledBitmap(resource, 100,
                                        100, false));
                        drawable.setCircular(true);
                        avatar.setImageDrawable(drawable);
                    }
                });

        // declare and set username of eacch developer
        TextView usernameTextView = (TextView) convertView.findViewById(R.id.username);
        usernameTextView.setText(currentDeveloper.getmDevName());

        return convertView;
    }
}
