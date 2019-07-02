package com.example.goo.profinal.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by Goo on 2018-02-15.
 * <프로필 화면>ㄴ
 * 기본 프로필 변경,
 * 게시글 갯수, 팔로잉,
 * 팔로워 갯수 및 목록
 * 참여중인 프로젝트 목록
 * 기본 셋팅
 */

public class Profile_Activity extends AppCompatActivity {

    LinearLayout goto_home, follower, following;
    TextView txt_content_count, txt_id, txt_following_count, txt_follower_count;
    Item_member member;
    Item_project project;
    ImageView profile_image;
    ArrayList<Item_project> array_project;
    ArrayList<Item_member> array_friend;
    private ArrayAdapter<Item_project> adapter2;

    ListView list_profile;
    Uri uri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        list_profile = (ListView) findViewById(R.id.list_profile);

        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");


        txt_id = (TextView) findViewById(R.id.txt_id);
        profile_image = findViewById(R.id.profile_image);


        loadData_demo();
        project = new Item_project();

        txt_id.setText(id);
        final String image = sp.getString(txt_id.getText().toString() + "image", "");
        uri = Uri.parse(image);
        profile_image.setImageURI(uri);
        profile_image.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        profile_image.setImageURI(Uri.parse(image));

        Intent intent = getIntent();
        member = new Item_member(txt_id.getText().toString());
        loadData();
        // loadData_my_project();
        //팔로워 수 불러오기
        ////////////////////////////////////////////////////////

        //전체선택 체크박스 객체 생성
        final CheckBox selectAll = (CheckBox) findViewById(R.id.selectAll);

        //글 추가 및 삭제 버튼
        Button btn_add = (Button) findViewById(R.id.btn_Add);
        Button btn_del = (Button) findViewById(R.id.btn_Del);

        //추가버튼 리스너
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile_Activity.this, Add_Project_Content.class);
                intent.putExtra("nick", txt_id.getText().toString());
                intent.putExtra("image", image);
                startActivityForResult(intent, 10);

            }
        });

//        //삭제버튼 리스너
//        btn_del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (member.array_project.size() <= 0) {
//                    Toast.makeText(getApplicationContext(), "모집글이 없습니다.", Toast.LENGTH_SHORT).show();
//                } else
//
//                    delWork();
//
//
//            }
//        });
//
//        //전체선택 체크박스 이벤트
//        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                //데이터의 갯수가 0개 이상이라면 그 사이즈만큼 돌면서 전체선택 체크박스의 상태 여부에 따라
//                // 밑에 있는 선택박스들이 전체 선택 되거나 취소 됨.
//
//
//                if (member.array_project.size() > 0) {
//                    for (int i = 0; i < member.array_project.size(); i++) {
//                        if (isChecked == true) {
//                            member.array_project.get(i).setIsChecked(isChecked);
//
//                        } else
//                            member.array_project.get(i).setIsChecked(isChecked);
//                    }
//                    //등록된 게시물이 없을 경우 전체선택이 눌러 지지 않음
//                } else if (member.array_project.size() <= 0) {
//                    selectAll.setChecked(false);
//                    Toast.makeText(getApplicationContext(), "등록된 게시물이 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//                //어댑터에 상태변화를 계속해서 알려주어야 함.
//                adapter2.notifyDataSetChanged();
//            }
//        });


        //리스트뷰 아이템 이벤트 리스너
        list_profile.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Intent intent = new Intent(Profile_Activity.this, ShowProject.class);

                intent.putExtra("name", member.array_project.get(i).getProject_name());
                intent.putExtra("intro", member.array_project.get(i).getProject_intro());
                intent.putExtra("ability", member.array_project.get(i).getProject_ability());
                intent.putExtra("dev_duration", member.array_project.get(i).getProject_develop_duration());
                intent.putExtra("apply_duration", member.array_project.get(i).getProject_apply_duration());
                intent.putExtra("num", member.array_project.get(i).people_num);
                startActivity(intent);

            }
        });


        //게시글 갯수 가져오기
        SharedPreferences sp2 = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp2.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();
        member.array_project = gson.fromJson(json, type);
        if (member.array_project == null) {
            member.array_project = new ArrayList<>();
        }


        goto_home = findViewById(R.id.goto_home);
        follower = findViewById(R.id.follower);
        following = findViewById(R.id.following);

        txt_content_count = findViewById(R.id.txt_content_count);


        //txt_following_count = findViewById(R.id.txt_following_count);

        if (member.array_project == null) {
            txt_content_count.setText("0");
        } else {
            txt_content_count.setText(member.array_project.size() + "");
        }


        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Profile_Follower_Activity.class);
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Profile_Following_Activity.class);
                startActivity(intent);
            }
        });


        //하단 버튼 이동
        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = findViewById(R.id.btn_chat);
        Button btn_profile_modify = findViewById(R.id.btn_profile_modify);


        loadData_member();
        for(int i = 0; i <array_friend.size();i++){
            if(id.equals(array_friend.get(i).getNick())){

            }
        }
        //프로필 수정 버튼
        btn_profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Profile_Motify.class);
                for(int i = 0; i <array_friend.size();i++) {
                    if (id.equals(array_friend.get(i).getNick())) {
                        intent.putExtra("id", array_friend.get(i).getId());
                        intent.putExtra("nick", array_friend.get(i).getId());
                        intent.putExtra("image", array_friend.get(i).profileImage);
                        intent.putExtra("password", array_friend.get(i).getPassword());
                        intent.putExtra("index",i);
                    }
                }
                startActivityForResult(intent,1000);
            }
        });


        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile_Activity.this, RealHome.class);

                startActivity(intent);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Profile_Activity.this, Member.class);


                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Profile_Activity.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        goto_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Home.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity.this, Chatting_list.class);


                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        adapter2 = new Adapter_friend_home(this, R.layout.customlist_friend_home, member.array_project);


        list_profile.setAdapter(adapter2);
    }

    //모집글 삭제 메서드
    public void delWork() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile_Activity.this);
        builder.setTitle("삭제 메세지");
        builder.setMessage("삭제 하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (member.array_project.size() > 0) {
                            for (int i = 0; i < member.array_project.size(); i++) {
                                if (i > member.array_project.size()) {
                                    break;
                                }
                                if (member.array_project.get(i).isChecked()) {

                                    member.array_project.remove(i);
                                    //member.content_count--;
                                    project.content_count--;
                                    if (project.content_count < 0) {
                                        project.content_count = 0;
                                    }
                                    System.out.println("컨텐츠 수는 : " + project.content_count);
                                    i--;
                                    if (member.array_project == null) {
                                        txt_content_count.setText("0");
                                    } else {
                                        txt_content_count.setText(member.array_project.size() + "");
                                    }
                                    adapter2.notifyDataSetChanged();

                                    continue;

                                }
                            }
                            Toast.makeText(getApplicationContext(), "모집글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 20) {

            String name2 = data.getStringExtra("name");
            String intro2 = data.getStringExtra("intro");
            String ability2 = data.getStringExtra("ability");
            String dev2 = data.getStringExtra("dev");
            String apply2 = data.getStringExtra("apply");
            String num = data.getStringExtra("num");
            String nick = data.getStringExtra("nick");
            String history = data.getStringExtra("history");
            String profile_image = data.getStringExtra("image");
            Item_project item = new Item_project(name2, intro2, ability2, dev2, apply2, num, nick, history, profile_image);
            item.setProject_name(name2);
            item.setProject_intro(intro2);
            item.setProject_ability(ability2);
            item.setProject_develop_duration(dev2);
            item.setProject_apply_duration(apply2);
            item.history = history;
            item.nick = nick;
            item.people_num = num;
            item.profile_image = profile_image;
            member.array_project.add(0, item);
            if (array_project == null) {
                array_project = new ArrayList<>();
            }
            array_project.add(0, item);

            adapter2.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "모집글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();

            //수정하고 나서 startActivityForResult 값 받아오기. startActivity나 startActivityForResult는 모두 메인에서 받아와야 한다.
        } else if (requestCode == 0 && resultCode == 1) {

            String name1 = data.getStringExtra("name1");
            String intro1 = data.getStringExtra("intro1");
            String ability1 = data.getStringExtra("ability1");
            String dev1 = data.getStringExtra("dev1");
            String apply1 = data.getStringExtra("apply1");
            String num = data.getStringExtra("num");
            int index = data.getIntExtra("index", 1);

//            Item_project item = new Item_project(name1, intro1, ability1, dev1, apply1, num);
//
//
//            item.setProject_name(name1);
//            item.setProject_intro(intro1);
//            item.setProject_ability(ability1);
//            item.setProject_develop_duration(dev1);
//            item.setProject_apply_duration(apply1);
//            item.people_num = num;
//            //해당 리스트뷰 아이템 값 변경
//            member.array_project.set(index, item);
//            adapter2.notifyDataSetChanged();


        } else if (requestCode == 4 && resultCode == 6) {
            String nick = data.getStringExtra("nick");


            System.out.println("아아아아아잉시발 개시발 진자");
            System.out.println("리스트 로드 됨" + nick);
            System.out.println(data.getIntExtra("position", 0));

            int pos = data.getIntExtra("position", 0);


            if (member.array_project == null) {
                member.array_project = new ArrayList<>();
            }

        } else if (requestCode == 4 && resultCode == 50) {


            String nick = data.getStringExtra("nick");
            System.out.println("이놈의 닉네임은 " + nick);
            SharedPreferences sp = getSharedPreferences(nick, MODE_PRIVATE);

            Gson gson = new Gson();

            String json = sp.getString("task list", null);
            Type type = new TypeToken<ArrayList<Item_project>>() {
            }.getType();
            member.array_project = gson.fromJson(json, type);
//
            if (member.array_project == null) {
                member.array_project = new ArrayList<>();
            }

            System.out.println("리스트 로드 됨");
            //  adapter = new Adapter_home(this, R.layout.customlist_home, member.array_project);
            adapter2.notifyDataSetChanged();
            //  lv_List.setAdapter(adapter);

        }else if(requestCode == 1000 && resultCode == 1000){
            txt_id.setText(data.getStringExtra("id"));
            uri = Uri.parse(data.getStringExtra("image"));
            profile_image.setImageURI(uri);
        }
    }


    //arraylist 저장
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

    private void saveData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list2", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(array_project);

        editor.putString("task demo", json);

        //  editor.clear();
        editor.apply();

        System.out.println("전체 모집글 내용 : " + json.toString());
        System.out.println("리스트 저장 됨");

    }

    //전체 프로젝트 모집글
    private void loadData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list2", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task demo", null);

        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();


        array_project = gson.fromJson(json, type);
        //array_member = new ArrayList<>();


        if (array_project == null) {
            array_project = new ArrayList<>();
        }


        System.out.println("RealHome에 대한 리스트 로드 됨");
    }

    //arraylist 불러오기
    private void loadData_my_project() {

        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();

        member.array_project = gson.fromJson(json, type);


        if (member.array_project == null) {
            member.array_project = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }

    private void loadData() {
        Intent intent = getIntent();
        final SharedPreferences follower_preferences = getSharedPreferences(txt_id.getText().toString() + "follower11", MODE_PRIVATE);
        int follower_count = follower_preferences.getInt("follower_count", 0);
        txt_follower_count = findViewById(R.id.txt_follower_count);
        txt_follower_count.setText(follower_count + "");

        final SharedPreferences following_preferences = getSharedPreferences(txt_id.getText().toString() + "following4", MODE_PRIVATE);
        int following_count = following_preferences.getInt("following_count", 0);
        txt_following_count = findViewById(R.id.txt_following_count);
        txt_following_count.setText(following_count + "");


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


    private void loadData_member() {

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
        //게시글 갯수 가져오기
//        SharedPreferences sp2 = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sp2.getString("task list", null);
//        Type type = new TypeToken<ArrayList<Item_project>>() {
//        }.getType();
//        member.array_project = gson.fromJson(json, type);

        if (member.array_project == null) {
            txt_content_count.setText("0");
        } else {
            txt_content_count.setText(member.array_project.size() + "");
        }

        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Home_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();


        saveData_demo();
        // Another activity is taking focus (this activity is about to be "paused").

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