package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.profinal.Activity.Show_Member_Home_Activity;
import com.example.goo.profinal.Item.Item_following;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-03-06.
 */

public class Adapter_following extends ArrayAdapter<Item_following> {

    private Activity context;

    private int id;
    ArrayList<Item_following> array_following;
    ArrayList<Item_member> array_friend;
    Uri uri;

    public Adapter_following(Activity context, int resource, ArrayList<Item_following> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_following = objects;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_following following = array_following.get(position);
        ImageView friend_image = convertView.findViewById(R.id.friend_image);
        TextView txt_nick = convertView.findViewById(R.id.txt_nick);
        Button btn_follow = convertView.findViewById(R.id.btn_add_friend);
        txt_nick.setText(following.nick);
        uri = Uri.parse(following.image);
        friend_image.setImageURI(uri);
        btn_follow.setText(following.follow);

        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");




//프로필 사진 누르면 해당 친구의 게시글 목록으로 이동
        friend_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Show_Member_Home_Activity.class);
                String nick = array_following.get(position).nick;
                String image = array_following.get(position).image;
                intent.putExtra("nick", nick);
                intent.putExtra("image", image);

                origin.startActivity(intent);
            }
        });


        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //팔로우 취소할 경우 팔로우 수 줄이기
                final SharedPreferences following_preferences = context.getSharedPreferences(user + "following4", MODE_PRIVATE);
                final SharedPreferences.Editor editor_following = following_preferences.edit();
                Gson gson3 = new Gson();

                array_following.remove(position);

                String json3 = gson3.toJson(array_following);
                int sum3 = array_following.size();
                editor_following.putString("following", json3);
                editor_following.putInt("following_count", sum3);
                editor_following.apply();
                notifyDataSetChanged();

                loadData();

                for (int i = 0; i < array_friend.size(); i++) {
                    if (array_following.size() == 0) {
                        array_friend.get(i).following = "팔로우";
                        notifyDataSetChanged();
                    } else if (!array_friend.get(i).getNick().equals(array_following.get(position).nick)) {
                        array_friend.get(i).following = "팔로우";
                        notifyDataSetChanged();
                    }
                }
                saveData();


            }
        });


        return convertView;
    }

    private void loadData() {
        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");

        SharedPreferences sp = context.getSharedPreferences("memberInfo2", context.MODE_PRIVATE);
        Gson gson = new Gson();


        String json = sp.getString(user + 100, null);
        String json2 = sp.getString("task list100", null);
        int sum = 0;
        int count = 0;
        //추천친구 목록 불러오기
        if (json.length() < json2.length()) {
            Type type = new TypeToken<ArrayList<Item_member>>() {
            }.getType();
            array_friend = gson.fromJson(json2, type);

            if (array_friend == null) {
                array_friend = new ArrayList<>();
            }

        } else {


        Type type = new TypeToken<ArrayList<Item_member>>() {
        }.getType();
        array_friend = gson.fromJson(json, type);

        if (array_friend == null) {
            array_friend = new ArrayList<>();
        }
        Log.i("친구 명단은 : ", json.toString());
    }
     }

    private void saveData() {
        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");

        SharedPreferences sp = context.getSharedPreferences("memberInfo2", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //for문까지는 몇명까지 팔로우 하는지 세는 코드!
//        int sum = 0;
//        int count = 0;
//
//        for (int i = 0; i < array_friend.size(); i++) {
//
//            if (array_friend.get(i).following.toString().equals("팔로우 취소")) {
//
//                count++;
//            }
//
//
//        }
//        sum = sum + count;

        Gson gson = new Gson();

        String json = gson.toJson(array_friend);
        String json3 = gson.toJson(array_friend);


        editor.putString(user + 100, json);


        //팔로잉 하는 친구들 명단 저장하기
        editor.putString(user + 2, json);
        editor.putString("task list3", json3);


        //총 팔로잉 하는 수
        //   editor.putInt(txt_id.getText().toString() + 100 + "sum", sum);
        editor.apply();


        System.out.println("리스트 저장 됨");

    }

}
