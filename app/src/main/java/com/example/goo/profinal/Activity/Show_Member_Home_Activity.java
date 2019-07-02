package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goo.profinal.Adapter.Adapter_friend_home;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-14.
 */

public class Show_Member_Home_Activity extends AppCompatActivity {

    LinearLayout goto_home, follower, following;
    TextView txt_content_count, txt_following_count, txt_follower_count;

    private ArrayList<ArrayList<Item_project>> group_project;
    private ArrayList<Item_project> array_project;
    private ArrayList<Item_member> array_member;
    private ArrayAdapter<Item_project> adapter;

    ListView list;
    ImageView profile_image;
    Item_member member;
    TextView txt_id;

    Uri uri;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_home);

        //친구목록에서 닉네임 가져오기
        Intent intent = getIntent();
        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        txt_id = findViewById(R.id.txt_id);
        txt_id.setText(intent.getStringExtra("nick"));
        String image = intent.getStringExtra("image");
        profile_image = findViewById(R.id.profile_image);
        uri = Uri.parse(image);
        profile_image.setImageURI(uri);
        String nickname = txt_id.getText().toString() ;
        list = findViewById(R.id.list_home);
        member = new Item_member(nickname);


        //게시글 갯수 가져오기
        SharedPreferences sp2 = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
       // SharedPreferences sp2 = getSharedPreferences("project", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp2.getString(txt_id.getText().toString(), null);
        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();
        member.array_project = gson.fromJson(json, type);



        goto_home = findViewById(R.id.goto_home);
        follower = findViewById(R.id.follower);
        following = findViewById(R.id.following);


        txt_content_count = findViewById(R.id.txt_content_count);
        //txt_follower_count = findViewById(R.id.txt_follower_count);
        //txt_following_count = findViewById(R.id.txt_following_count);



        //홈 레이아웃에 있는 이미지 버튼 객체 생성
        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = (ImageButton) findViewById(R.id.btn_chat);
        goto_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Member_Home_Activity.this, Home.class);
                intent.putExtra("nick",txt_id.getText().toString());
                startActivity(intent);
            }
        });

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Member_Home_Activity.this, Follower_Friend.class);
                intent.putExtra("nick",txt_id.getText().toString());
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Member_Home_Activity.this, Following_Friend.class);
                intent.putExtra("nick",txt_id.getText().toString());
                startActivity(intent);
            }
        });

        //닉네임별로 가지고 있는 글목록 가지고 오기
        loadData();
        if (member.array_project == null) {
            txt_content_count.setText("0");
        } else {
            txt_content_count.setText(member.array_project.size() + "");
        }

      //  adapter = new Adapter_friend_home(this, R.layout.customlist_friend_home, member.array_project, txt_id.getText().toString());
        adapter = new Adapter_friend_home(this, R.layout.customlist_friend_home, member.array_project);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Intent intent = new Intent(Show_Member_Home_Activity.this, ShowProject.class);

                intent.putExtra("name", member.array_project.get(i).getProject_name());
                intent.putExtra("intro", member.array_project.get(i).getProject_intro());
                intent.putExtra("ability", member.array_project.get(i).getProject_ability());
                intent.putExtra("dev_duration", member.array_project.get(i).getProject_develop_duration());
                intent.putExtra("apply_duration", member.array_project.get(i).getProject_apply_duration());

                startActivity(intent);

            }
        });

        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Show_Member_Home_Activity.this, RealHome.class);

                startActivity(intent);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Show_Member_Home_Activity.this, Member.class);


                startActivityForResult(intent, 4);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Member_Home_Activity.this, Profile_Activity.class);

                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Member_Home_Activity.this, Chatting_list.class);


                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    private void loadData() {
        Intent intent = getIntent();
        final SharedPreferences follower_preferences = getSharedPreferences(txt_id.getText().toString()+"follower11", MODE_PRIVATE);
        int follower_count =  follower_preferences.getInt("follower_count",0);
        txt_follower_count = findViewById(R.id.txt_follower_count);
        txt_follower_count.setText(follower_count+"");

        final SharedPreferences following_preferences = getSharedPreferences(txt_id.getText().toString()+"following4", MODE_PRIVATE);
        int following_count =  following_preferences.getInt("following_count",0);
        txt_following_count = findViewById(R.id.txt_following_count);
        txt_following_count.setText(following_count+"");

        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
        //SharedPreferences sp = getSharedPreferences("project", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task list", null);


        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();

        member.array_project = gson.fromJson(json, type);


        // System.out.println(array_project.toString());
        if (member.array_project == null) {
            member.array_project = new ArrayList<>();

        }
        //adapter.notifyDataSetChanged();
        System.out.println("Show_member_home_activity에서 리스트 로드 됨");
    }

//    private void loadData() {
//        Intent intent = getIntent();
//        String nick = intent.getStringExtra("nick");
//
//
//        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
//        //SharedPreferences sp = getSharedPreferences("project", MODE_PRIVATE);
//
//        Gson gson = new Gson();
//
//        String json = sp.getString("task list", null);
//     //   String json = sp.getString(txt_id.getText().toString(), null);
//        String json2 = sp.getString("task list5", null);
//        String json3 = json + json;
//        //String json2 = sp.getString("task list2", null);
//        if (json2 != null) {
//            System.out.println("json3" + json2.toString());
//        } else
//            System.out.println("");
//        Type type = new TypeToken<ArrayList<Item_project>>() {
//        }.getType();
//
//
//        // array_project = gson.fromJson(json, type);
//
//        member.array_project = gson.fromJson(json, type);
//
//
//        // System.out.println(array_project.toString());
//        if (member.array_project == null) {
//            member.array_project = new ArrayList<>();
//
//        }
//        //adapter.notifyDataSetChanged();
//        System.out.println("Show_member_home_activity에서 리스트 로드 됨");
//    }
}