package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.profinal.Activity.Show_Member_Home_Activity;
import com.example.goo.profinal.Item.Item_chat;
import com.example.goo.profinal.R;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-04.
 */

public class Adapter_Chatting extends ArrayAdapter<Item_chat> {

    private Activity context;
    private int id;

    ArrayList<Item_chat> array_chat;
    Uri uri;

    private int lastPosition = -1;
    public Adapter_Chatting(Activity context, int resource, ArrayList<Item_chat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_chat = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_chat chat = array_chat.get(position);

        final SharedPreferences sp = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp.getString("home", "");
        final String image = sp.getString("image", "");

        TextView txt_id = convertView.findViewById(R.id.txt_id);
        TextView txt_message = convertView.findViewById(R.id.txt_message);
        TextView txt_history = convertView.findViewById(R.id.txt_history);
        ImageView profile_image = convertView.findViewById(R.id.profile_image);

        txt_id.setText(chat.nick);

        uri = Uri.parse(chat.profile_image);
        profile_image.setImageURI(uri);


        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Show_Member_Home_Activity.class);
                String nick = array_chat.get(position).nick;
                String image = array_chat.get(position).profile_image;
                intent.putExtra("nick", nick);
                intent.putExtra("image", image);

                origin.startActivity(intent);
            }
        });
//        if(id.equals(array_chat.get(position).nick)){
//            uri = Uri.parse(image);
//            profile_image.setImageURI(uri);
//
//        }
        txt_message.setText(chat.chat_content);

        int color = Color.parseColor("#7fcaf2");
        int black = Color.BLACK;
        int white = Color.WHITE;

        txt_message.setBackground(this.getContext().getResources().getDrawable(R.drawable.talkbox1));
        txt_message.setTextColor(black);
       // txt_message.setBackground();

        txt_history.setText(chat.history);
        int yellow = Color.YELLOW;

        if (id.equals(chat.nick)) {
            txt_message.setBackground(this.getContext().getResources().getDrawable(R.drawable.qqqqqqqq));
            txt_message.setTextColor(white);

        }


        Animation animation = AnimationUtils.loadAnimation(context,
                (position>lastPosition)?R.anim.load_down_anim : R.anim.load_up_anim);
        convertView.startAnimation(animation);
        lastPosition = position;

        return convertView;
    }
}
