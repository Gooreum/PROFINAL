package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goo.profinal.Adapter.Adapter_member;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-01-29.
 */

public class Member extends AppCompatActivity {
    ListView list;
    private Adapter_member adapter_friend;
    private ArrayList<Item_member> array_friend;
    private ArrayList<Item_project> array_project;
    TextView txt_id, txt_id_2;
    Item_member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);


        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String id = sp.getString("home", "");
       // String image = sp.getString("user_image", "");
        txt_id = (TextView) findViewById(R.id.txt_id);
        //  txt_id_2 = findViewById(R.id.txt_id_2);
        // txt_id_2.setText(id);
        txt_id.setText(id);
        member = new Item_member(txt_id.getText().toString());


//        loadData2();


        loadData();


        Intent intent = getIntent();

        list = findViewById(R.id.list_friend);
        // array_friend = new ArrayList<>();

        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);

        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = findViewById(R.id.btn_chat);

        //홈화면 여는 버튼
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Member.this, RealHome.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        //추천친구 여는 버튼
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Member.this, Member.class);

                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);
            }
        });
        //프로필 여는 버튼
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Member.this, Profile_Activity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Member.this, Chatting_list.class);


                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //  adapter_friend = new Adapter_member(this, R.layout.customlist_friend, member.array_member);
        adapter_friend = new Adapter_member(this, R.layout.customlist_friend, array_friend);

        adapter_friend.notifyDataSetChanged();
        list.setAdapter(adapter_friend);


        Log.i("MY", "----Friend_onCreate----");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {
             ArrayList<Item_member> templist = new ArrayList<>();

             for(Item_member temp : array_friend){
                 if(temp.getNick().contains(s.toLowerCase())){
                     templist.add(temp);
                 }
             }
                ArrayAdapter<Item_member> adapter = new Adapter_member(Member.this, R.layout.customlist_friend, templist);
                //adapter_friend.notifyDataSetChanged();
                list.setAdapter(adapter);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    //추천친구 목록 불러오기
    private void loadData() {

        SharedPreferences sp = getSharedPreferences("memberInfo2", MODE_PRIVATE);
        Gson gson = new Gson();


        String json = sp.getString(txt_id.getText().toString() + 100, null);
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

        }
    }

    private void saveData() {

        SharedPreferences sp = getSharedPreferences("memberInfo2", MODE_PRIVATE);
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


        editor.putString(txt_id.getText().toString() + 100, json);


        //팔로잉 하는 친구들 명단 저장하기
        editor.putString(txt_id.getText().toString() + 2, json);
        editor.putString("task list3", json3);


        //총 팔로잉 하는 수    
     //   editor.putInt(txt_id.getText().toString() + 100 + "sum", sum);
        editor.apply();


        System.out.println("리스트 저장 됨");

    }



    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Friend_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Friend_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Friend_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").


        saveData();
        Log.i("MY", "----Friend_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Friend_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Friend_onDestroy----");
    }
}
