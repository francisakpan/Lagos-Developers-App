package com.francis.lagdev0.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.francis.lagdev0.data.DeveloperContracts.DeveloperSchema;
import com.francis.lagdev0.R;

/**
 * Created by Francis on 10/08/2017.
 * A custom DialogFragment
 * That displays a preview of a developer picture when clicked on
 */

public class ImageDialogFragment extends DialogFragment {
    String link;
    String username;
    String dev_link;
    String dev_repos_url;

    public ImageDialogFragment(){
        //Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle bundle = getArguments();
        link = bundle.getString(DeveloperSchema.KEY_IMAGE_URL);
        username = bundle.getString(DeveloperSchema.KEY_NAME);
        dev_link = bundle.getString(DeveloperSchema.KEY_URL);
        dev_repos_url = bundle.getString(DeveloperSchema.KEY_REPOS_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_image, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView DeveloperUserName = (TextView)view.findViewById(R.id.text_username);
        DeveloperUserName.setText(username);

        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        Glide.with(getContext())
                .load(link)
                .animate(android.R.anim.fade_in)
                .thumbnail(Glide.with(getContext()).load(link))
                .placeholder(R.drawable.thumbnail)
                .error(R.drawable.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        ImageView imageViewInfo = (ImageView)view.findViewById(R.id.action_info);
        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(DeveloperSchema.KEY_IMAGE_URL, link);
                intent.putExtra(DeveloperSchema.KEY_NAME, username);
                intent.putExtra(DeveloperSchema.KEY_URL, dev_link);
                intent.putExtra(DeveloperSchema.KEY_REPOS_URL, dev_repos_url);
                startActivity(intent);
                dismiss();
            }
        });
    }
}
