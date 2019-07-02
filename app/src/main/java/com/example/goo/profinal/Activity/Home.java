package com.example.goo.profinal.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Adapter.Adapter_home;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-05.
 * 내가 프로젝트를 기획하고 사람들을 모집해서 프로젝트를 진행해보는 어플리케이션이다.
 * SNS형식을 차용하여 만들어 보고자 하였음.
 * 지금의 HOME화면은 내가 프로젝트 모집글을 올리거나 사람들의 게시글을 볼수 있는 공간이다.
 * 리스트뷰를 사용하여 글들을 목록화 시킬 수 있고 다이얼로그를 통해 나의 모집글을 올리거나 수정할 수 있다.
 * 체크박스를 통해 리스트뷰에 올라온 글들을 삭제 가능하다.
 * 내가 원하는 글을 검색해서 찾아볼 수 있다.
 */

public class Home extends AppCompatActivity {

    static final int PICK_FILE_FROM_IMAGE = 1;

    private ArrayList<Item_project> array_project;
    private ArrayList<Item_project> array2;
    private ArrayAdapter<Item_project> adapter;
    Item_project project;

    ListView lv_List;
    ImageView profile_image;
    Button image_update;
    Item_member member;
    TextView txt_id;
    final int btnSrc2 = R.id.image_update;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        project = new Item_project();
        //인텐트 받기
        final Intent intent = new Intent(this.getIntent());


        //로그인화면으로부터 sharedprefence의 아이디와 닉네임 가지고 오기
        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String id = sp.getString("home", "");
        String image = sp.getString("user_image", "");
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_id.setText(id);
        // profile_image.setI(image);

        //홈화면 리스트뷰 객체생성
        lv_List = (ListView) findViewById(R.id.list_home);


        member = new Item_member(txt_id.getText().toString());

        loadData();
        //loadData_demo();

        LinearLayout list_line = (LinearLayout) findViewById(R.id.list_line);


        //프로필 이미지
        profile_image = (ImageView) findViewById(R.id.profile_image);
        image_update = (Button) findViewById(R.id.image_update);

        image_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case btnSrc2:
                        takeImageFromGallery();
                        break;
                }
            }
        });

        //홈 레이아웃에 있는 이미지 버튼 객체 생성
        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);

        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);


        //로그아웃 버튼
        Button btn_logout = findViewById(R.id.btn_logout);

        //좋아요, 댓글달기, 신청하기 버튼 객체 생성
//        Button btn_like = (Button) findViewById(R.id.btn_like);
//        Button btn_reply = findViewById(R.id.btn_reply);
//        Button btn_share = findViewById(R.id.btn_request);

        //글번호 오름순 내림순 버튼
        //Button btn_number = (Button) findViewById(R.id.btn_number);

        //서치뷰
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        //전체선택 체크박스 객체 생성
        final CheckBox selectAll = (CheckBox) findViewById(R.id.selectAll);

        //글 추가 및 삭제 버튼
        Button btn_add = (Button) findViewById(R.id.btn_Add);
        Button btn_del = (Button) findViewById(R.id.btn_Del);

        //로그아웃 버튼 리스너
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Home.this);
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
                                Intent intent = new Intent(Home.this, Login.class);

                                startActivity(intent);
                            }
                        });
                builder.show();

            }
        });

        //추가버튼 리스너
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, Add_Project_Content.class);
                startActivityForResult(intent, 10);
                //AddWork();
                //  saveData();
            }
        });

        //삭제버튼 리스너
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (member.array_project.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "모집글이 없습니다.", Toast.LENGTH_SHORT).show();
                } else

                    delWork();


            }
        });


        //전체선택 체크박스 이벤트
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //데이터의 갯수가 0개 이상이라면 그 사이즈만큼 돌면서 전체선택 체크박스의 상태 여부에 따라
                // 밑에 있는 선택박스들이 전체 선택 되거나 취소 됨.


                if (member.array_project.size() > 0) {
                    for (int i = 0; i < member.array_project.size(); i++) {
                        if (isChecked == true) {
                            member.array_project.get(i).setIsChecked(isChecked);

                        } else
                            member.array_project.get(i).setIsChecked(isChecked);
                    }
                    //등록된 게시물이 없을 경우 전체선택이 눌러 지지 않음
                } else if (member.array_project.size() <= 0) {
                    selectAll.setChecked(false);
                    Toast.makeText(getApplicationContext(), "등록된 게시물이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                //어댑터에 상태변화를 계속해서 알려주어야 함.
                adapter.notifyDataSetChanged();
            }
        });

        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, RealHome.class);

                startActivity(intent);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Home.this, Member.class);


                startActivityForResult(intent, 4);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Profile_Activity.class);

                startActivity(intent);
            }
        });

       // adapter = new Adapter_home(this, R.layout.customlist_home, member.array_project, txt_id.getText().toString());
        adapter = new Adapter_home(this, R.layout.customlist_home, member.array_project);
        adapter.notifyDataSetChanged();
        lv_List.setAdapter(adapter);


        //리스트뷰 아이템 이벤트 리스너
        lv_List.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Intent intent = new Intent(Home.this, ShowProject.class);

                intent.putExtra("name", member.array_project.get(i).getProject_name());
                intent.putExtra("intro", member.array_project.get(i).getProject_intro());
                intent.putExtra("ability", member.array_project.get(i).getProject_ability());
                intent.putExtra("dev_duration", member.array_project.get(i).getProject_develop_duration());
                intent.putExtra("apply_duration", member.array_project.get(i).getProject_apply_duration());
                intent.putExtra("num", member.array_project.get(i).people_num);
                startActivity(intent);

            }
        });


        //검색
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Item_project> templist = new ArrayList<>();

                for (Item_project temp : member.array_project) {
                    if (temp.getProject_ability().contains(s.toLowerCase())) {
                        templist.add(temp);
                    }
                }


               // ArrayAdapter<Item_project> adapter = new Adapter_home(Home.this, R.layout.customlist_home, templist, txt_id.getText().toString());
                ArrayAdapter<Item_project> adapter = new Adapter_home(Home.this, R.layout.customlist_home, templist);
                lv_List.setAdapter(adapter);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }
        });
        Log.i("MY", "----Home_onCreate----");

    }


    //모집글 삭제 메서드
    public void delWork() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Home.this);
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

                                    adapter.notifyDataSetChanged();

                                    continue;

                                }
                            }
                            Toast.makeText(getApplicationContext(), "모집글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        builder.show();
    }


    //이미지관련
    public void takeImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");

        startActivityForResult(
                Intent.createChooser(intent, "Choose an image")
                , PICK_FILE_FROM_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_FROM_IMAGE) {
            profile_image.setImageURI(data.getData());


        } else if (requestCode == 10 && resultCode == 20) {

            String name2 = data.getStringExtra("name");
            String intro2 = data.getStringExtra("intro");
            String ability2 = data.getStringExtra("ability");
            String dev2 = data.getStringExtra("dev");
            String apply2 = data.getStringExtra("apply");
            String num = data.getStringExtra("num");
            String nick = data.getStringExtra("nick");


            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "모집글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();

            //수정하고 나서 startActivityForResult 값 받아오기. startActivity나 startActivityForResult는 모두 메인에서 받아와야 한다.
        } else if (requestCode == 0 && resultCode == 1) {

//            String name1 = data.getStringExtra("name1");
//            String intro1 = data.getStringExtra("intro1");
//            String ability1 = data.getStringExtra("ability1");
//            String dev1 = data.getStringExtra("dev1");
//            String apply1 = data.getStringExtra("apply1");
//            String num = data.getStringExtra("num");
//            int index = data.getIntExtra("index", 1);
//
//            Item_project item = new Item_project(name1, intro1, ability1, dev1, apply1,num);
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
//            adapter.notifyDataSetChanged();
//
//            //   adapter.notifyDataSetChanged();


        } else if (requestCode == 4 && resultCode == 6) {
            String nick = data.getStringExtra("nick");


            System.out.println("리스트 로드 됨" + nick);
            System.out.println(data.getIntExtra("position", 0));

            int pos = data.getIntExtra("position", 0);


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
            adapter.notifyDataSetChanged();
            //  lv_List.setAdapter(adapter);

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

        editor.apply();


        System.out.println("리스트 저장 됨");
    }

    private void saveData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(member.array_project);

        editor.putString("task demo", json);
        // editor.putString("task list5", json+json);

        editor.apply();

        System.out.println("전체 모집글 내용 : " + json.toString());
        System.out.println("리스트 저장 됨");

    }

    //arraylist 불러오기
    private void loadData() {

        SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();

        member.array_project = gson.fromJson(json, type);


        if (array_project == null) {
            array_project = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }

    private void loadData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sp.getString("task demo", null);
        Type type = new TypeToken<ArrayList<Item_project>>() {
        }.getType();

        member.array_project = gson.fromJson(json, type);


        if (member.array_project == null) {
            member.array_project = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
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

        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Home_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();


       // saveData_demo();
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
