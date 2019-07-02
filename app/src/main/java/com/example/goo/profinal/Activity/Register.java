package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Register extends AppCompatActivity {
    ArrayList<Item_member> array_member;
    Item_member member;
    Button btn_confirm;
    EditText edit_id, edit_nick, edit_password, edit_passwordCheck;
    ImageView profile_image;

    static final int PICK_FILE_FROM_IMAGE = 1;
    final int btnSrc2 = R.id.image_update;

    EditText id;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        member = new Item_member();
        loadData();

        //아이디, 별명, 암호 ,프로필 이미지 객체 만들기
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_nick = (EditText) findViewById(R.id.edit_nickname);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_passwordCheck = (EditText) findViewById(R.id.edit_passwordCheck);
        profile_image = findViewById(R.id.profile_image);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImagefromGallery(view);
            }
        });
        //메인화면의 회원가입 버튼으로부터 인텐트 가져오기
        Intent intent = getIntent();

        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        //회원가입란 작성 후 확인 버튼 이벤트 리스너
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //회원가입 유효성 검사
                if (edit_id.getText().toString().equals("")) {
                    edit_id.requestFocus();
                    Toast.makeText(Register.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (edit_nick.getText().toString().isEmpty()) {
                    edit_nick.requestFocus();
                    Toast.makeText(Register.this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (edit_password.getText().toString().equals("")) {
                    edit_password.requestFocus();
                    Toast.makeText(Register.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (edit_passwordCheck.getText().toString().equals("")) {
                    edit_passwordCheck.requestFocus();
                    Toast.makeText(Register.this, "확인 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!edit_password.getText().toString().equals(edit_passwordCheck.getText().toString())) {
                    edit_passwordCheck.requestFocus();
                    Toast.makeText(Register.this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();


                } else if (edit_id.getText().toString() != null && edit_nick.getText().toString() != null && edit_password.getText().toString().equals(edit_passwordCheck.getText().toString())) {

                    SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
                    String user_id = edit_id.getText().toString();
                    String user_nick = edit_nick.getText().toString();
                    String user_password = edit_password.getText().toString();
                    String profile_image =null ;

                    if(profile_image == null){
                        profile_image =  "content://com.android.providers.media.documents/document/image%3A74";
                    }else{
                        profile_image = uri.toString();
                    }

                    System.out.println("프로필 이미지 스트링 값 : " + profile_image);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("id", user_id);
                    editor.putString("password", user_password);
                    editor.putString("user_nick", user_nick);
                    editor.putString("request", member.following);
                    editor.putString(user_id+"image", profile_image);
                    editor.putString("image", profile_image);

                    editor.putString(user_id + user_password + "data", user_nick);

                    editor.commit();

                    Toast.makeText(Register.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("id", user_id);
                    setResult(3, intent);

                    //프로필에 이미지를 추가해줌.

                    Item_member item = new Item_member(user_id, user_nick, user_password, profile_image);

                    array_member.add(0, item);

                    saveData();
                    finish();
                }

            }
        });

        Log.i("MY", "----register_onCreate----");
    }


    public void loadImagefromGallery(View view) {
        //Intent 생성
      //  Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //ACTION_PIC과 차이점?
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                uri = data.getData();
                System.out.println("uri의 절대경로 : " + uri.toString());

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                profile_image = (ImageView) findViewById(R.id.profile_image);
               // profile_image.setImageBitmap(scaled);

                profile_image.setImageURI(uri);


            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    private void saveData() {

        // SharedPreferences sp = getSharedPreferences("memberInfo", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("memberInfo2", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();
        //  Gson gson2 = new Gson();
        String json = gson.toJson(array_member);


        //지워야 할 놈
        String json2 = gson.toJson(array_member);


        System.out.println("리지스터 : " + json.toString());
        //  editor.putString("task list", json);
        //팔로잉 하는 친구들 부르기 위함.
        editor.putString(edit_id.getText().toString() + 100, json);
        editor.putString("task list100", json2);
         //editor.clear();


        editor.apply();

    }

    private void loadData() {

        SharedPreferences sp = getSharedPreferences("memberInfo2", MODE_PRIVATE);

        Gson gson = new Gson();

        String json = null;
        if (json == null) {
            json = sp.getString("task list100", null);
//            Log.i("jason이 null일 때", json.toString());
        } else {
            json = sp.getString(edit_id.getText().toString() + 100, null);
            Log.i("jason이 null이 아닐 때", json.toString());
        }
        Type type = new TypeToken<ArrayList<Item_member>>() {
        }.getType();
        array_member = gson.fromJson(json, type);
        //System.out.println(json.toString());
        if (array_member == null) {
            array_member = new ArrayList<>();
        }
       // array_member.remove(0);
        //array_member.remove(0);
       // array_member.remove(2);
        System.out.println("리스트 로드 됨");


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
        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----register_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").

       // saveData();
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
