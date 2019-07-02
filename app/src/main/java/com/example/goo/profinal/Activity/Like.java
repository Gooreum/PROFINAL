package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.goo.profinal.Adapter.Adapter_like;
import com.example.goo.profinal.Item.Item_like_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-25.
 */

public class Like extends AppCompatActivity {
    ListView list;
    private Adapter_like adapter_like;
    private ArrayList<Item_like_member> array_like;
    private ArrayList<Item_project> array_project;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_like);
        list = findViewById(R.id.list_like);

        loadData();


        adapter_like = new Adapter_like(this, R.layout.customlist_like, array_like);
        adapter_like.notifyDataSetChanged();
        list.setAdapter(adapter_like);
        adapter_like.notifyDataSetChanged();
        Log.i("MY", "----Friend_onCreate----");
    }

    //좋아요 명단 로드해오기
    private void loadData() {

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);


        SharedPreferences sp2 = getSharedPreferences(index + "like6", MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = sp2.getString("like", null);
        Type type = new TypeToken<ArrayList<Item_like_member>>() {
        }.getType();
        array_like = gson.fromJson(json2, type);
        // System.out.println(json2.toString());
        if (array_like == null) {
            array_like = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }


}