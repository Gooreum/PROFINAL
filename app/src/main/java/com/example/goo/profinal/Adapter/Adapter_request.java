package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.example.goo.profinal.Item.Item_chat;
import com.example.goo.profinal.Item.Item_request_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-02.
 */

public class Adapter_request extends ArrayAdapter<Item_request_member> {

    private Activity context;

    private int id;
    ArrayList<Item_request_member> array_request;
    ArrayList<Item_chat> array_chat;
    Item_chat chat;
    Item_chat chat2;
    Uri uri;
    int sum = 0;
    int count = 0;

    public Adapter_request(Activity context, int resource, ArrayList<Item_request_member> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_request = objects;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        final Item_request_member request_member = array_request.get(position);
        //프로젝트 갯수 받아오기
        final SharedPreferences sp = context.getSharedPreferences("array_project", context.MODE_PRIVATE);
        final int array_project_size = sp.getInt("array_project", 0);
        final int array_project_position = sp.getInt("position", 0);


        Log.i("=========================================array_project의 position은 : ", array_project_size - array_project_position - 1 + "");

        System.out.println("--------------------------------" + array_project_position);


        //프로필 이미지 넣기
        final ImageView profile_image = convertView.findViewById(R.id.profile_image);
        uri = Uri.parse(request_member.profile_image);
        profile_image.setImageURI(uri);

        //아이디 넣기
        TextView txt_id = convertView.findViewById(R.id.txt_id);
        txt_id.setText(request_member.nick);


        //로그인한 아이디 가져와서 게시글 주인과 같을 경우에만 "승낙" 버튼이 뜨게 한다.

        final SharedPreferences sp5 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp5.getString("home", "");
        final String image = sp5.getString(id + "image", "");
        final String image2 = sp5.getString("image", "");

        final Button btn_ok = convertView.findViewById(R.id.btn_ok);
        Intent intent = context.getIntent();

        //모집글을 작성한 아이디일 경우에만 프로젝트 신청한 사람들을 승낙할 수 있다.

        if (id.equals(intent.getStringExtra("nick"))) {
            btn_ok.setVisibility(View.VISIBLE);

            //본인일 경우 승낙 버튼이 '본인'으로 변경

        } else {
            btn_ok.setVisibility(View.INVISIBLE);
        }

        /**승낙버튼을 누를 경우 생기는 이벤트
         * (1)승낙버튼을 누르면 취소 버튼으로 바뀌고, 해당 아이템의 아이디, 프로필 이미지를 가진 객체가 생성됨.
         *          ->이후에 프로젝트 시작 버튼을 누르면 채팅방으로 연결된다. 여기서 생성된 객체들은 채팅 리스트 목록에 보여줄 내용들임.
         * (2)취소를 누르면 그 반대. 생성된 객체가 삭제된다.
         */


        //승낙 인원수를 Request클래스에 보내줄 쉐어드
        final SharedPreferences ok_preferences = context.getSharedPreferences(array_project_size - array_project_position - 1 + "request_count2", Context.MODE_PRIVATE);
        final SharedPreferences.Editor ok_editor = ok_preferences.edit();

        request_member.accept = ok_preferences.getString(position + "request", "승낙");
        btn_ok.setText(request_member.accept);


        //승낙버튼
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request 에서 보내준 모집인원 수 가져오기
                SharedPreferences people_num_preferences = context.getSharedPreferences("people_num", context.MODE_PRIVATE);
                final String people_num = people_num_preferences.getString("people_num","");
                //========================================승낙한 사람 명단 불러오기=========================================
                SharedPreferences sp0 = context.getSharedPreferences(array_project_size - array_project_position - 1 + "accept_for_chat11", Context.MODE_PRIVATE);

                Gson gson = new Gson();
                String json2 = sp0.getString("chat3", null);

                Type type = new TypeToken<ArrayList<Item_chat>>() {
                }.getType();
                array_chat = gson.fromJson(json2, type);

                if (array_chat == null) {
                    array_chat = new ArrayList<>();
                }
                notifyDataSetChanged();

                if (btn_ok.getText().toString().equals("승낙")) {
                    request_member.accept = "취소";
                    btn_ok.setText(request_member.accept);

                    //유저가 신청한 사람들을 승낙취소하면 승낙한 인원수 감소
                    for (int i = 0; i < array_request.size(); i++) {
                        if (array_request.contains(request_member.accept.equals("취소"))) {
                        }
                    }
                    sum = sum + 1;
                    request_member.accept_count = sum;
                    Toast.makeText(context, "모집인원 : "+sum+" / "+people_num, Toast.LENGTH_SHORT).show();

                    ok_editor.putInt("request_member.accept_count", request_member.accept_count);

                    //'취소','승낙' 상태 저장
                    ok_editor.putString(position + "request", request_member.accept);

                    //  editor.clear();
                    ok_editor.commit();
                    notifyDataSetChanged();

                    //승낙한 사람들 저장하기 -> 채팅방 만들기 위함
                    SharedPreferences accept_for_chat = context.getSharedPreferences(array_project_size - array_project_position - 1 + "accept_for_chat11", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor_accept_for_chat = accept_for_chat.edit();
                    Gson gson2 = new Gson();


                    //승낙한 사람 명단 중복 막기

                    chat = new Item_chat(array_request.get(position).nick, array_request.get(position).profile_image);
                    chat2 = new Item_chat(id, image);

                    // chat2 = new Item_chat(id, image);
                    if (array_chat.size() == 0) {
                        //array_chat.add(chat);
                        array_chat.add(chat2);
                        array_chat.add(chat);

                    } else if(array_chat.size() > 0 ){
                        array_chat.add(chat);
                    }
                    String json = gson2.toJson(array_chat);
                    Log.i("=====================json : ", json.toString());
                    editor_accept_for_chat.putString("chat3", json);

                    editor_accept_for_chat.apply();

                    notifyDataSetChanged();


                } else if (btn_ok.getText().toString().equals("취소")) {
                    request_member.accept = "승낙";
                    btn_ok.setText(request_member.accept);

                    //유저가 신청한 사람들을 승낙취소하면 승낙한 인원수 감소
                    for (int i = 0; i < array_request.size(); i++) {
                        if (array_request.contains(request_member.accept.equals("승낙"))) {
                        }
                    }
                    sum = sum - 1;
                    request_member.accept_count = sum;
                    Toast.makeText(context, "모집인원 : "+sum+" / "+people_num, Toast.LENGTH_SHORT).show();

                    ok_editor.putInt("request_member.accept_count", request_member.accept_count);

                    //'취소','승낙' 상태 저장
                    ok_editor.putString(position + "request", request_member.accept);
                    ok_editor.commit();

                    SharedPreferences accept_for_chat = context.getSharedPreferences(array_project_size - array_project_position - 1 + "accept_for_chat11", Context.MODE_PRIVATE);

                    final SharedPreferences.Editor editor_accept_for_chat = accept_for_chat.edit();
                    Gson gson2 = new Gson();

                    //좋아요 취소 누르면 해당 아이디 명단 빼기
                    for (int i = 0; i < array_chat.size(); i++) {
                        if (request_member.nick.equals(array_chat.get(i).nick)) {

                            array_chat.remove(i);

                        }
                    }

                    String json = gson2.toJson(array_chat);

                    Log.i("=====================json : ", json.toString());
                    editor_accept_for_chat.putString("chat3", json);

                    editor_accept_for_chat.apply();


                    notifyDataSetChanged();

                    // Log.i("request_member.accept_count 갯수는 : ", "" + request_member.accept_count);

                }
            }
        });
        if (id.equals(txt_id.getText().toString())) {
            btn_ok.setText("본인");
        } else
            btn_ok.setText(request_member.accept);



        return convertView;
    }


}
