package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Login extends AppCompatActivity {

    Button btn_login, btn_register;
    EditText edit_id, edit_password;
    ArrayList<Item_member> array_member2;
    Item_member member;
    TextView txt_count;
    Handler mHandler = null;


    Handler handler;
    Runnable runnable;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mHandler = new Handler();


        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        edit_id = findViewById(R.id.edit_id);
        edit_password = findViewById(R.id.edit_pw);

        //회원 목록 데이터 불러오기.
        loadData();

        //로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id = edit_id.getText().toString();
                String password = edit_password.getText().toString();


                SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);

                String userDetails = sp.getString(id + password + "data", "Username or password is incorrect");
                String image = sp.getString("image",null);

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("home", userDetails);
                editor.putString("image", image);


                editor.commit();
                sp.getString("id", "");
                sp.getString("password", "");
                //sp.getString("user_image", "");

                //회원db에서 로그인 시도하려는 아이디와 비밀번호가 있는지 확인하기

                if (array_member2.size() > 0) {
                    for (int i = 0; i < array_member2.size(); i++) {

                        if (id.equals(array_member2.get(i).getId()) && password.equals(array_member2.get(i).getPassword())) {


                            //꽃무늬 프로그래스 로딩중
                            ACProgressFlower dialog = new ACProgressFlower.Builder(Login.this)
                                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                                    .themeColor(Color.WHITE)
                                    .text("Loading...")
                                    .fadeColor(Color.DKGRAY).build();


                            dialog.show();

                            //핸들러를 사용해서 프로그래스가 몇 초동안 진행될지를 설정한다.
                            handler = new Handler();
                            final int finalI = i;
                            runnable = new Runnable() {
                                @Override
                                public void run() {

                                    //  progress.setVisibility(View.GONE);
                                    timer.cancel();

                                    Intent intent = new Intent(Login.this, RealHome.class);

                                    startActivity(intent);
                                    Toast.makeText(Login.this, array_member2.get(finalI).getId().toString() + "님 어서오세요.", Toast.LENGTH_SHORT).show();
                                }
                            };
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(runnable);
                                }
                            }, 2000, 1000);

                            break;

                        }

                    }


                } else {
                    Toast.makeText(Login.this, "아이디와 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                if (!password.equals("123")) {

                    Toast.makeText(Login.this, "아이디와 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();


                }
            }


        });





        //회원가입 버튼
        btn_register.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Login.this, Register.class);

                startActivityForResult(intent, 2);

            }
        });
    }

    private void loadData() {

        SharedPreferences sp = getSharedPreferences("memberInfo2", MODE_PRIVATE);

        Gson gson = new Gson();
        String json2 = sp.getString("task list100", null);

        Type type = new TypeToken<ArrayList<Item_member>>() {
        }.getType();
        array_member2 = gson.fromJson(json2, type);

        if (array_member2 == null) {
            array_member2 = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == 3) {

            String id = data.getStringExtra("id");
            edit_id.setText(id);
            edit_password.requestFocus();

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----register_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----register_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----register_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        Log.i("MY", "----register_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----register_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----register_onDestroy----");
    }
}