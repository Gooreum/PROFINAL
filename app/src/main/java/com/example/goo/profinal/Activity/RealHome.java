package com.example.goo.profinal.Activity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Adapter.Adapter_friend_home;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-02-18.
 */

public class RealHome extends AppCompatActivity {
    private ArrayList<Item_project> array_project;
    // private ArrayList<Item_project> array_project2;
    private ArrayList<Item_member> array_member;
    private ArrayAdapter<Item_project> adapter;
    Item_member member;
    Item_member member2;
    ListView list;
    ImageView profile_image;
    Button btn_message;
    TextView txt_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_home);


        //친구목록에서 닉네임 가져오기
        Intent intent = getIntent();
        //로그인화면으로부터 sharedprefence의 아이디와 닉네임 가지고 오기
        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String id = sp.getString("home", "");

        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_id.setText(id);

        member = new Item_member(txt_id.getText().toString());


        list = findViewById(R.id.list_home);
        Button btn_logout = findViewById(R.id.btn_logout);
        //홈 레이아웃에 있는 이미지 버튼 객체 생성
        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = findViewById(R.id.btn_chat);

        //닉네임별로 가지고 있는 글목록 가지고 오기

        loadData_demo();

        adapter = new Adapter_friend_home(this, R.layout.customlist_friend_home, array_project);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Intent intent = new Intent(RealHome.this, ShowProject.class);

                intent.putExtra("name", array_project.get(i).getProject_name());
                intent.putExtra("intro", array_project.get(i).getProject_intro());
                intent.putExtra("ability", array_project.get(i).getProject_ability());
                intent.putExtra("dev_duration", array_project.get(i).getProject_develop_duration());
                intent.putExtra("apply_duration", array_project.get(i).getProject_apply_duration());
                intent.putExtra("num", array_project.get(i).people_num);
                startActivity(intent);

            }
        });
        //로그아웃 버튼 리스너
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RealHome.this);
                builder.setTitle("로그아웃 메세지");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RealHome.this, Login.class);

                                startActivity(intent);
                            }
                        });
                builder.show();

            }
        });
        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RealHome.this, RealHome.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(RealHome.this, Member.class);

                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);


            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RealHome.this, Profile_Activity.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RealHome.this, Chatting_list.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });

    }


    /**
     * 메서드 모음
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Item_project> templlist = new ArrayList<>();

                for (Item_project temp : array_project) {
                    if (temp.getProject_name().contains(s.toLowerCase()) || temp.getProject_ability().contains(s.toLowerCase())) {
                        templlist.add(temp);
                    }
                }
                ArrayAdapter<Item_project> adapter = new Adapter_friend_home(RealHome.this, R.layout.customlist_friend_home, templlist);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //수정하고 나서 startActivityForResult 값 받아오기. startActivity나 startActivityForResult는 모두 메인에서 받아와야 한다.

        if (requestCode == 0 && resultCode == 1) {

            String name1 = data.getStringExtra("name1");
            String intro1 = data.getStringExtra("intro1");
            String ability1 = data.getStringExtra("ability1");
            String dev1 = data.getStringExtra("dev1");
            String apply1 = data.getStringExtra("apply1");
            String num = data.getStringExtra("num");
            int index = data.getIntExtra("index", 1);
            String history = data.getStringExtra("history");
            String nick = data.getStringExtra("nick");
            String image = data.getStringExtra("image");
            Item_project item = new Item_project(name1, intro1, ability1, dev1, apply1, num, nick, history, image);


            item.setProject_name(name1);
            item.setProject_intro(intro1);
            item.setProject_ability(ability1);
            item.setProject_develop_duration(dev1);
            item.setProject_apply_duration(apply1);
            item.people_num = num;
            item.nick = nick;
            item.history = history;
            item.profile_image = image;
            //해당 리스트뷰 아이템 값 변경
            array_project.set(index, item);

            adapter.notifyDataSetChanged();

        }



    }

    private void loadData_member() {

        SharedPreferences sp = getSharedPreferences("memberInfo", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("task list100", null);

        Type type = new TypeToken<ArrayList<Item_member>>() {
        }.getType();
        array_member = gson.fromJson(json, type);

        if (array_member == null) {
            array_member = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }

    private void loadData() {


        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);


        Gson gson = new Gson();

        String json = sp.getString("task list", null);


        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();


        //array_project = gson.fromJson(json3, type);

        member.array_project = gson.fromJson(json, type);
        // member.array_project = gson.fromJson(json4, type);


        // System.out.println(array_project.toString());
        if (member.array_project == null) {
            member.array_project = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }

    //전체 프로젝트 모집글
    private void loadData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list2", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task demo", null);

        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();


        array_project = gson.fromJson(json, type);

        if (array_project == null) {
            array_project = new ArrayList<>();
        }


        System.out.println("RealHome에 대한 리스트 로드 됨");
    }

    private void saveData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list2", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(array_project);

        editor.putString("task demo", json);
        // editor.putString("task list5", json+json);

        editor.apply();

        System.out.println("전체 모집글 내용 : " + json.toString());
        System.out.println("리스트 저장 됨");
    }

    private void saveData() {

        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(member.array_project);

        editor.putString("task list", json);

        editor.putString("project", String.valueOf(member.array_project));
        editor.apply();


        System.out.println("리스트 저장 됨");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Home_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Home_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        //   loadData();


        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Home_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences history = getSharedPreferences("history", MODE_PRIVATE);
        SharedPreferences.Editor history_editor = history.edit();
        for (int i = 0; i < array_project.size(); i++) {
            history_editor.putInt(array_project.get(i).second + "second", array_project.get(i).second);
            history_editor.putInt(array_project.get(i).min + "min", array_project.get(i).min);
            history_editor.putInt(array_project.get(i).hour + "hour", array_project.get(i).hour);
            history_editor.commit();
        }
        history_editor.commit();
        saveData_demo();
        // Another activity is taking focus (this activity is about to be "paused").
        // saveData();
        Log.i("MY", "----Home_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Home_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Home_onDestroy----");
    }


}

