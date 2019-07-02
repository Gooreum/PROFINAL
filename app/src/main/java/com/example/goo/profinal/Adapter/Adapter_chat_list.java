package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goo.profinal.Item.Item_chat_list;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-03.
 */

public class Adapter_chat_list extends ArrayAdapter<Item_chat_list> {

    private Activity context;

    private int id;
    ArrayList<Item_chat_list> array_chat_list;
    Uri uri;
    String nick = "";
    String nick_collect = "";
    public Adapter_chat_list(Activity context, int resource, ArrayList<Item_chat_list> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_chat_list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_chat_list chat_list = array_chat_list.get(position);

        //loadData();

        final SharedPreferences sp = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp.getString("home", "");


        TextView txt_nick = convertView.findViewById(R.id.txt_nick_collection);
        TextView txt_history = convertView.findViewById(R.id.txt_history);
        TextView txt_chat_content = convertView.findViewById(R.id.txt_chat_content);
        LinearLayout line_chat_list = convertView.findViewById(R.id.line_chat_list);


        txt_nick.setText(chat_list.nick);
        if(!txt_nick.getText().toString().contains(id)){
            line_chat_list.setVisibility(View.GONE);
        }

        txt_history.setText(chat_list.history);
        txt_chat_content.setText(chat_list.chat_content);


        return convertView;
    }


    private void loadData() {

        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp2.getString("id", "");
//
//        Intent intent = getIntent();
//        int index = intent.getIntExtra("position", 0);
//        String nick_collection = intent.getStringExtra(index + "txt_chat_people");


        SharedPreferences sp0 = context.getSharedPreferences("chat_list8", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json2 = sp0.getString("chat_list", null);

        Type type = new TypeToken<ArrayList<Item_chat_list>>() {
        }.getType();
        array_chat_list = gson.fromJson(json2, type);
        // System.out.println(json2.toString());
        if (array_chat_list == null) {
            array_chat_list = new ArrayList<>();
        }

    }
    private void saveData() {
        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp2.getString("home", "");



        SharedPreferences sp3 = context.getSharedPreferences("chat_list8", context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp3.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(array_chat_list);

        editor.putString("chat_list", json);

        //editor.clear();
        editor.apply();




    }
}