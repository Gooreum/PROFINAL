package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.profinal.Item.Item_like_member;
import com.example.goo.profinal.R;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-25.
 */

public class Adapter_like extends ArrayAdapter<Item_like_member> {

    private Activity context;

    private int id;
    ArrayList<Item_like_member> array_like;
    Uri uri;

    public Adapter_like(Activity context, int resource, ArrayList<Item_like_member> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_like = objects;
        //this.nick = nick;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_like_member like_member = array_like.get(position);
        ImageView img_profile = convertView.findViewById(R.id.img_profile);
        TextView txt_nick = convertView.findViewById(R.id.txt_nick);

        txt_nick.setText(like_member.nick);

        uri = Uri.parse(like_member.profile_image);
        img_profile.setImageURI(uri);
        return convertView;
    }
}