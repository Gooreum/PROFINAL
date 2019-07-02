package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Activity.Show_Member_Home_Activity;
import com.example.goo.profinal.Item.Item_follower;
import com.example.goo.profinal.Item.Item_following;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-02-12.
 */

public class Adapter_member extends ArrayAdapter<Item_member> {

    private Activity context;
    private int id;
    ArrayList<Item_member> array_friend;
    ArrayList<Item_project> array_project;
    Item_member member;
    Uri uri;
    Item_follower follower;
    Item_following following;
    ArrayList<Item_follower> array_follower;
    ArrayList<Item_following> array_following;

    public Adapter_member(Activity context, int resource, ArrayList<Item_member> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_friend = objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);


        }
//        LayoutInflater inflater = context.getLayoutInflater();
//        convertView = inflater.inflate(id,null);

        final Item_member item = array_friend.get(position);


        final TextView txt_nick = convertView.findViewById(R.id.txt_nick);

        ImageView friend_image = convertView.findViewById(R.id.friend_image);

        uri = Uri.parse(item.profileImage);
        friend_image.setImageURI(uri);
        txt_nick.setText(item.getNick());


        final SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");


        SharedPreferences sp2 = context.getSharedPreferences(item.getNick(), MODE_PRIVATE);
        SharedPreferences sp3 = context.getSharedPreferences(id, MODE_PRIVATE);

        Log.i("------------ 로그인한 아이디 ------------ : ", id);
        Log.i("------------ 리스트뷰에 있는 아이디 ------------ : ", item.getNick());

        Gson gson = new Gson();

        String json2 = sp2.getString("task list", null);
        String json3 = sp3.getString("task list", null);


        // System.out.println(json3.toString());

        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();

        array_project = gson.fromJson(json2, type);

        member = new Item_member(txt_nick.getText().toString());
        if (member.array_project == null) {
            member.array_project = new ArrayList<>();
        }


        final Button btn_add_friend = convertView.findViewById(R.id.btn_add_friend);
        btn_add_friend.setTag(position);


        final Button btn_cancel_friend = convertView.findViewById(R.id.btn_cancel_friend);
        btn_cancel_friend.setTag(position);


        LinearLayout line_member =convertView.findViewById(R.id.line_member);
//프로필 사진 누르면 해당 친구의 게시글 목록으로 이동
        friend_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Show_Member_Home_Activity.class);
                String nick = array_friend.get(position).getNick();
                String image = array_friend.get(position).profileImage;
                intent.putExtra("nick", nick);
                intent.putExtra("image", image);

                origin.startActivity(intent);
            }
        });


        //로그인한 아이디가 가지고 있는 팔로잉 명단 가지고 오기
        loadData();
        for(int j = 0; j<array_friend.size();j++) {
            for (int i = 0; i < array_following.size(); i++) {
                if (array_following.get(i).nick.equals(array_friend.get(j).getNick())) {
                    array_friend.get(j).following = "팔로우 취소";
                    btn_add_friend.setText("팔로우 취소");
                    notifyDataSetChanged();
                }
            }
        }

        //팔로우하기 버튼 누르기
        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //팔로워 불러오기
                final SharedPreferences load_follower = context.getSharedPreferences(array_friend.get(position).getNick() + "follower11", MODE_PRIVATE);
                Gson gson5 = new Gson();
                String json5 = load_follower.getString("follower", null);


                Type type5 = new TypeToken<ArrayList<Item_follower>>() {
                }.getType();
                array_follower = gson5.fromJson(json5, type5);

                if (array_follower == null) {
                    array_follower = new ArrayList<>();
                }

                //팔로잉 불러오기
                final SharedPreferences load_following = context.getSharedPreferences(id + "following4", MODE_PRIVATE);
                Gson gson6 = new Gson();
                String json6 = load_following.getString("following", null);

                //팔로워 목록 불러오기

                Type type6 = new TypeToken<ArrayList<Item_following>>() {
                }.getType();
                array_following = gson6.fromJson(json6, type6);

                if (array_following == null) {
                    array_following = new ArrayList<>();
                }


                if (btn_add_friend.getText().equals("팔로우")) {
                    item.following = "팔로우 취소";
                    btn_add_friend.setText(item.following);

                    Intent intent = new Intent();
                    Activity origin = (Activity) context;


                    String nick = array_friend.get(position).getNick();

                    intent.putExtra("nick", nick);
                    intent.putExtra("position", position);

                    origin.setResult(10, intent);


                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), item.getNick() + " 님을 팔로우 합니다.", Toast.LENGTH_SHORT).show();

                    //지금 로그인해 있는 아이디와 멤버의 아이디가 같을 경우 해당 아이디의 팔로우 버튼을 '본인'으로 변경해주기 위해 가져온 sharedpreferences
                    final SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
                    String id = sp.getString("home", "");
                    String image = sp.getString(id + "image", "");

                    SharedPreferences sp2 = context.getSharedPreferences(item.getNick(), MODE_PRIVATE);
                    SharedPreferences sp3 = context.getSharedPreferences(id, MODE_PRIVATE);

                    Gson gson = new Gson();

                    String json2 = sp2.getString("task list", null);


                    Type type = new TypeToken<ArrayList<Item_project>>() {
                    }.getType();

                    array_project = gson.fromJson(json2, type);


                    /**로그인한 아이디가 리스트뷰 아이템에 있는 아이디를 팔로우 할 경우,
                     * 아이템에 있는 아이디와 관련된 객체를 생성해서, 로그인한 아이디를 담는다.
                     *그리고 아이템에 있는 아이디가 로그인 하고 팔로워 액티비티에 들어갈 경우 저장해둔 어레이리스트를 뿌린다.
                     */

                    //팔로우할 경우 해당 아이디의 팔로워 수 늘리기
                    final SharedPreferences follower_preferences = context.getSharedPreferences(array_friend.get(position).getNick() + "follower11", MODE_PRIVATE);
                    final SharedPreferences.Editor editor_follower = follower_preferences.edit();
                    Gson gson2 = new Gson();

                    follower = new Item_follower(id, image);
                    array_follower.add(follower);
                    int sum = array_follower.size();
                    String json = gson2.toJson(array_follower);

                    editor_follower.putString("follower", json);
                    editor_follower.putInt("follower_count", sum);
                    editor_follower.apply();
                    notifyDataSetChanged();


                    //팔로우할 경우 해당 팔로우 수 늘리기 및 팔로우 친구 추가
                    final SharedPreferences following_preferences = context.getSharedPreferences(id + "following4", MODE_PRIVATE);
                    final SharedPreferences.Editor editor_following = following_preferences.edit();
                    Gson gson3 = new Gson();


                    following = new Item_following(array_friend.get(position).getNick(), array_friend.get(position).profileImage);
                    array_following.add(following);
                    int sum3 = array_following.size();

                    String json3 = gson3.toJson(array_following);

                    editor_following.putString("following", json3);
                    editor_following.putInt("following_count", sum3);
                    editor_following.apply();
                    notifyDataSetChanged();


                } else if (btn_add_friend.getText().equals("팔로우 취소")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("확인 메세지");
                    builder.setMessage("팔로우를 취소 하시겠습니까?");


                    builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            item.following = "팔로우";
                            btn_add_friend.setText(item.following);

                            //팔로우 취소할 경우 해당 아이디의 팔로워 수 줄이기
                            final SharedPreferences follower_preferences = context.getSharedPreferences(array_friend.get(position).getNick() + "follower11", MODE_PRIVATE);
                            final SharedPreferences.Editor editor_follower = follower_preferences.edit();
                            Gson gson2 = new Gson();

                            for (int i = 0; i < array_follower.size(); i++) {
                                if (id.equals(array_follower.get(i).nick)) {

                                    array_follower.remove(i);

                                }
                            }

                            String json = gson2.toJson(array_follower);
                            int sum = array_follower.size();
                            editor_follower.putString("follower", json);
                            editor_follower.putInt("follower_count", sum);

                            editor_follower.apply();
                            notifyDataSetChanged();


                            Toast.makeText(getContext(), item.getNick() + " 팔로우를 취소합니다.", Toast.LENGTH_SHORT).show();

                            notifyDataSetChanged();

                            //팔로우 취소할 경우 팔로우 수 줄이기
                            final SharedPreferences following_preferences = context.getSharedPreferences(id + "following4", MODE_PRIVATE);
                            final SharedPreferences.Editor editor_following = following_preferences.edit();
                            Gson gson3 = new Gson();

                            for (int i = 0; i < array_following.size(); i++) {
                                if (array_friend.get(position).getNick().equals(array_following.get(i).nick)) {

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
                    });
                    builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    builder.create().show();


                }
                notifyDataSetChanged();


                Log.d("친구추가", "친구추가" + btn_add_friend.getTag());

            }
        });


        if (id.equals(txt_nick.getText().toString())) {
            btn_add_friend.setText(item.me);
        }
        else {
            btn_add_friend.setText(item.following);
        }

//        if(item.following.equals("팔로우 취소")){
//            line_member.setVisibility(View.GONE);
//        }else if(item.following.equals("팔로우")){
//            line_member.setVisibility(View.VISIBLE);
//        }
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

    private void saveData() {

        final SharedPreferences sp2 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String user = sp2.getString("home", "");

        SharedPreferences sp = context.getSharedPreferences("memberInfo2",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();

        String json = gson.toJson(array_friend);
        String json3 = gson.toJson(array_friend);


        editor.putString(user + 100, json);


        //팔로잉 하는 친구들 명단 저장하기
        editor.putString(user + 2, json);
        editor.putString("task list3", json3);


        //총 팔로잉 하는 수
        // editor.putInt(txt_id.getText().toString() + 100 + "sum", sum);
        editor.apply();


        System.out.println("리스트 저장 됨");

    }
}

