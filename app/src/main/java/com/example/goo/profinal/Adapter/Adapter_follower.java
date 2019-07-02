package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.profinal.Activity.Show_Member_Home_Activity;
import com.example.goo.profinal.Item.Item_follower;
import com.example.goo.profinal.Item.Item_following;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-03-02.
 */

public class Adapter_follower extends ArrayAdapter<Item_follower> {

    private Activity context;

    private int id;
    ArrayList<Item_follower> array_follower;
    ArrayList<Item_following> array_following;
    Item_following following;
    Uri uri;

    public Adapter_follower(Activity context, int resource, ArrayList<Item_follower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_follower = objects;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_follower follower = array_follower.get(position);
        ImageView friend_image = convertView.findViewById(R.id.friend_image);
        TextView txt_nick = convertView.findViewById(R.id.txt_nick);
        final Button btn_add_friend = convertView.findViewById(R.id.btn_add_friend);
        txt_nick.setText(follower.nick);



//프로필 사진 누르면 해당 친구의 게시글 목록으로 이동
        friend_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Show_Member_Home_Activity.class);
                String nick = array_follower.get(position).nick;
                String image = array_follower.get(position).image;
                intent.putExtra("nick", nick);
                intent.putExtra("image", image);

                origin.startActivity(intent);
            }
        });
        //해당 아이디가 팔로우 하는 친구들 불러오기. 팔로워 중에서 팔로우 하는 사람이 아니면 팔로우 할 수 있도록 하거나, 그 반대의 경우도 마찬가지..
        loadData();
        for (int i = 0; i < array_following.size(); i++) {
            if (array_following.get(i).nick.equals(array_follower.get(position).nick)) {
                array_follower.get(position).following = "팔로우 취소";
                btn_add_friend.setText("팔로우 취소");
            }
        }
        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");

        //팔로워 중에서 팔로우 하는 친구가 아니면 팔로우를 추가할 수 있도록 해보자.
        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_add_friend.getText().toString().equals("팔로우")) {
                    array_follower.get(position).following = "팔로우 취소";
                    btn_add_friend.setText("팔로우 취소");


                    //팔로우할 경우 해당 팔로우 수 늘리기 및 팔로우 친구 추가
                    final SharedPreferences following_preferences = context.getSharedPreferences(user + "following4", MODE_PRIVATE);
                    final SharedPreferences.Editor editor_following = following_preferences.edit();
                    Gson gson3 = new Gson();

                    following = new Item_following(array_follower.get(position).nick, array_follower.get(position).image);
                    array_following.add(following);
                    int sum3 = array_following.size();

                    String json3 = gson3.toJson(array_following);

                    editor_following.putString("following", json3);
                    editor_following.putInt("following_count", sum3);
                    editor_following.apply();
                    notifyDataSetChanged();


                }else if(btn_add_friend.getText().toString().equals("팔로우 취소")){
                    array_follower.get(position).following = "팔로우";
                    btn_add_friend.setText("팔로우");

                    //팔로우 취소할 경우 팔로우 수 줄이기
                    final SharedPreferences following_preferences = context.getSharedPreferences(user + "following4", MODE_PRIVATE);
                    final SharedPreferences.Editor editor_following = following_preferences.edit();
                    Gson gson3 = new Gson();

                    for(int i = 0; i<array_following.size();i++){
                        if(array_follower.get(position).nick.equals(array_following.get(i).nick)){
                            array_following.remove(i);
                        }
                    }


                    String json3 = gson3.toJson(array_following);
                    int sum3 = array_following.size();
                    editor_following.putString("following", json3);
                    editor_following.putInt("following_count", sum3);
                    editor_following.apply();
                    notifyDataSetChanged();

                }
            }
        });



        uri = Uri.parse(follower.image);
        friend_image.setImageURI(uri);
        return convertView;
    }

    private void loadData() {

        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");

        final SharedPreferences sp = context.getSharedPreferences(user + "following4", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = sp.getString("following", null);
        //추천친구 목록 불러오기
        Type type = new TypeToken<ArrayList<Item_following>>() {
        }.getType();
        array_following = gson.fromJson(json2, type);

        if (array_following == null) {
            array_following = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");

    }
}
