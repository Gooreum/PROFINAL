package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Activity.Like;
import com.example.goo.profinal.Activity.Message;
import com.example.goo.profinal.Activity.Modify_Project_Content;
import com.example.goo.profinal.Activity.Request;
import com.example.goo.profinal.Item.Item_like_member;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.Item.Item_request_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Goo on 2018-02-05.
 */

public class Adapter_friend_home extends ArrayAdapter<Item_project> {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Activity context;

    private int id;
    ArrayList<Item_project> array;
    ArrayList<Item_like_member> array_like;
    ArrayList<Item_request_member> array_request;
    String nick;
    Uri uri;
    int second;
    int min;
    int hour;
    Thread t;
    Item_like_member like_member;
    Item_request_member request_member;
    private int lastPosition = -1;
    Item_member member;
    public Adapter_friend_home(Activity context, int resource, ArrayList<Item_project> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array = objects;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        // array_like = new ArrayList<>();
//        Item_like_member like = array_like.get(position);
        final Item_project item = array.get(position);


        final TextView txt_nick = convertView.findViewById(R.id.txt_nick);
        txt_nick.setText(item.nick);

        //프로젝트 관련 내용 가져오기
        final TextView txt_project_name = (TextView) convertView.findViewById(R.id.txt_project_name);
        txt_project_name.setText(item.getProject_name());

        final TextView txt_project_intro = (TextView) convertView.findViewById(R.id.txt_project_intro);
        txt_project_intro.setText(item.getProject_intro());

        final TextView txt_project_ability = (TextView) convertView.findViewById(R.id.txt_project_ability);
        txt_project_ability.setText(item.getProject_ability());

        final TextView txt_development_duration = (TextView) convertView.findViewById(R.id.txt_development_duration);
        txt_development_duration.setText(item.getProject_develop_duration());

        final TextView txt_apply_duration = (TextView) convertView.findViewById(R.id.txt_apply_duration);
        txt_apply_duration.setText(item.getProject_apply_duration());

        final TextView txt_people_num = convertView.findViewById(R.id.txt_people_num);
        txt_people_num.setText(item.people_num);
        final ImageView image = convertView.findViewById(R.id.image);

        final LinearLayout line_request = convertView.findViewById(R.id.line_request);
        final LinearLayout line_like = convertView.findViewById(R.id.line_like);

        final Button btn_motify = convertView.findViewById(R.id.btn_motify);
        final Button btn_delete = convertView.findViewById(R.id.btn_delete);


        /**
         * 신청하기 숫자 눌렀을 때 Request클래스로 화면 전환하고 아이템의 포지션값도 보낸다.
         */

        //프로젝트 갯수를 보내주는 놈 쉐어드.
        final SharedPreferences item_preferences = context.getSharedPreferences("array_project", Context.MODE_PRIVATE);
        final SharedPreferences.Editor item_editor = item_preferences.edit();
        line_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Request.class);
                //Request 클래스에 포지션값을 보내준다.
                intent.putExtra("position", array.size() - position - 1);
                intent.putExtra("nick", item.nick);
                intent.putExtra("people_num", item.people_num);

                origin.startActivity(intent);


                item_editor.putInt("array_project", array.size());
                item_editor.putInt("position", getPosition(item));
                Log.i("----------------------position은 ", getPosition(item) + "");
                item_editor.commit();

            }
        });



        //이미지 가져오기
        uri = Uri.parse(item.profile_image);
        image.setImageURI(uri);

        final TextView txt_history = convertView.findViewById(R.id.txt_history);

        //게시글 작성일자
        long a = System.currentTimeMillis() - Long.parseLong(item.history.toString());
        //초
        long b = a / 1000;

        //분
        long c = a / (1000 * 60);

        //시간
        long d = a / (1000 * 60 * 60);
        //item.history = String.valueOf(b);

        if (b < 60) {
            txt_history.setText("방금전");
        } else if (b < 3600) {
            txt_history.setText(c + "분 전");
        } else if (b < 3600 * 24) {
            txt_history.setText(d + "시간 전");
        } else {
            Date mDate = new Date(Long.parseLong(item.history.toString()));
            SimpleDateFormat mFormat2 = new SimpleDateFormat("yyyy년MM월dd일");
            txt_history.setText(mFormat2.format(mDate));
        }

        //댓글달기 버튼
        final Button btn_message = convertView.findViewById(R.id.btn_message);
        //댓글 갯수
        final TextView txt_message_count = convertView.findViewById(R.id.txt_message_count);
        //좋아요 갯수
        final TextView txt_like_count = convertView.findViewById(R.id.txt_like_count);
        //좋아요 이미지
        final ImageView img_heart = convertView.findViewById(R.id.img_heart);
        //좋아요 버튼
        final Button btn_like = convertView.findViewById(R.id.btn_like);
        //신청하기 버튼
        final Button btn_request = convertView.findViewById(R.id.btn_request);
        //모집인원
        final TextView txt_num = convertView.findViewById(R.id.txt_num);

        //모집인원수 표시하기
        if (item.people_num == null) {
            txt_num.setText(0 + "");
        } else {
            txt_num.setText(item.people_num);
        }


        final TextView txt_request_count = convertView.findViewById(R.id.txt_request_count);


        // 로그인한 아이디별로 좋아요 혹은 좋아요 취소 버튼의 상태를 저장한다.
        //final SharedPreferences like_count = context.getSharedPreferences("like_count2", Context.MODE_PRIVATE);
        final SharedPreferences like_count = context.getSharedPreferences(array.size() - position - 1 + "like_count2", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = like_count.edit();
        final SharedPreferences sp5 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp5.getString("home", "");
        final String profile_image = sp5.getString(id + "image", null);
        //아이디별로 좋아요 버튼 누른 상태 불러오기
        item.like = like_count.getString(id + (array.size() - position - 1) + "like", "좋아요");
        btn_like.setText(item.like);

        //게시글마다 좋아요 갯수 불러오기
        // item.like_count = like_count.getInt((array.size() - position - 1) + "like_count2", 0);
        item.like_count = like_count.getInt("like_count2", 0);
        txt_like_count.setText(item.like_count + "");


        //좋아요 버튼 리스너
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //========================================좋아요 누른 사람 명단 불러오기=========================================
                SharedPreferences sp0 = context.getSharedPreferences(array.size() - position - 1 + "like6", Context.MODE_PRIVATE);

                Gson gson = new Gson();
                String json2 = sp0.getString("like", null);

                Type type = new TypeToken<ArrayList<Item_like_member>>() {
                }.getType();
                array_like = gson.fromJson(json2, type);
                // System.out.println(json2.toString());
                if (array_like == null) {
                    array_like = new ArrayList<>();
                }
                notifyDataSetChanged();


                if (btn_like.getText().equals("좋아요")) {
                    item.like = "좋아요 취소";

                    item.like_count = item.like_count + 1;

                    btn_like.setText(item.like);

                    //게시글 마다 좋아요 갯수저장
                    // editor.putInt((array.size() - position - 1) + "like_count2", item.like_count);
                    editor.putInt("like_count2", item.like_count);
                    //아이디와 게시글 마다 좋아요,좋아요 취소 버튼 상태 저장
                    editor.putString(id + (array.size() - position - 1) + "like", item.like);
                    //  editor.clear();
                    editor.commit();
                    notifyDataSetChanged();


                    //좋아요 누른 사람들 명단 저장하기
                    SharedPreferences like = context.getSharedPreferences(array.size() - position - 1 + "like6", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor_like = like.edit();
                    Gson gson2 = new Gson();

                    //좋아요 명단 중복 막기
                    like_member = new Item_like_member(profile_image, id);

                    if (array_like.size() == 0) {
                        array_like.add(0, like_member);
                    } else if (array_like.size() > 0) {
                        for (int i = 0; i < array_like.size(); i++) {
                            if (!array_like.contains(like_member)) {

                                array_like.add(like_member);

                            }
                        }
                    }
                    String json = gson2.toJson(array_like);

                    editor_like.putString("like", json);

                    editor_like.apply();
                    notifyDataSetChanged();

                    //============================================================================================================
                } else if (btn_like.getText().equals("좋아요 취소")) {

                    item.like = "좋아요";
                    btn_like.setText(item.like);
                    item.like_count = item.like_count - 1;

                    //게시글 마다 좋아요 갯수저장
                    // editor.putInt((array.size() - position - 1) + "like_count2", item.like_count);
                    editor.putInt("like_count2", item.like_count);
                    //아이디와 게시글 마다 좋아요,좋아요 취소 버튼 상태 저장
                    editor.putString(id + (array.size() - position - 1) + "like", item.like);
                    //editor.clear();
                    editor.commit();

                    SharedPreferences like = context.getSharedPreferences(array.size() - position - 1 + "like6", Context.MODE_PRIVATE);

                    final SharedPreferences.Editor editor_like = like.edit();
                    Gson gson2 = new Gson();

                    //좋아요 취소 누르면 해당 아이디 명단 빼기
                    for (int i = 0; i < array_like.size(); i++) {
                        if (id.equals(array_like.get(i).nick)) {

                            array_like.remove(i);

                        }
                    }

                    String json = gson2.toJson(array_like);

                    editor_like.putString("like", json);

                    editor_like.apply();


                    notifyDataSetChanged();
                }


            }
        });
        btn_like.setText(item.like);

        //좋아요 갯수가 0개 이상일 때만 하트 이미지가 보인다.
        txt_like_count.setText(item.like_count + "");
        if (item.like_count == 0) {
            img_heart.setVisibility(View.INVISIBLE);

        } else
            img_heart.setVisibility(View.VISIBLE);


        //누가 좋아요를 눌렀는지 보기
        line_like.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Like.class);

                //Like 클래스에 포지션값을 보내준다.
                intent.putExtra("position", array.size() - position - 1);
                origin.startActivity(intent);
            }
        });

        //모든 정보는 "request_count2" 파일명으로 저장한다.
        //final SharedPreferences request_count = context.getSharedPreferences("request_count2", Context.MODE_PRIVATE);
        final SharedPreferences request_count = context.getSharedPreferences(array.size() - position - 1 + "request_count2", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = request_count.edit();


      //  item.reuqest = request_count.getString(id + (array.size() - position - 1) + "request2", "신청하기");
        item.reuqest = request_count.getString(id + "request2", "신청하기");
        btn_request.setText(item.reuqest);
        //item.request_count = request_count.getInt((array.size() - position - 1) + "request_count2", 0);
        item.request_count = request_count.getInt("request_count2", 0);
        txt_request_count.setText(item.request_count + "");
//



//        line_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Activity origin = (Activity) context;
//                Intent intent = new Intent(view.getContext(), Request.class);
//                //Request 클래스에 포지션값을 보내준다.
//                intent.putExtra("position", array.size() - position - 1);
//                intent.putExtra("nick", item.nick);
//                intent.putExtra("people_num", item.people_num);
//
//                origin.startActivity(intent);
//
//
//                item_editor.putInt("array_project", array.size());
//                item_editor.putInt("position", getPosition(item));
//                Log.i("----------------------position은 ", getPosition(item) + "");
//                item_editor.commit();
//
//            }
//        });

        //프로젝트 신청하기 리스너


        SharedPreferences sp2 = context.getSharedPreferences(array.size() - position - 1 + "모집완료", Context.MODE_PRIVATE);
        final String btn_chat = sp2.getString("모집완료", "프로젝트 시작");
        if (btn_chat.equals("모집완료")) {
            array.get(position).reuqest = "모집완료";
            btn_request.setText(array.get(position).reuqest);
        }
        btn_request.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                //클릭하면 바로 신청하기 누른 사람들의 명단을 불러온다.

                SharedPreferences sp0 = context.getSharedPreferences(array.size() - position - 1 + "request11", Context.MODE_PRIVATE);

                Gson gson = new Gson();
                String json2 = sp0.getString("request", null);

                Type type = new TypeToken<ArrayList<Item_request_member>>() {
                }.getType();
                array_request = gson.fromJson(json2, type);
                // System.out.println(json2.toString());
                if (array_request == null) {
                    array_request = new ArrayList<>();
                }
                notifyDataSetChanged();

                if (btn_request.getText().equals("신청하기")) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("확인 메세지");
                    builder.setMessage("이 프로젝트를 신청하시겠습니까?");
                    builder.setPositiveButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    item.reuqest = "신청취소";
                                    btn_request.setText(item.reuqest);
                                    item.request_count = item.request_count + 1;

                                    //게시글 마다 신청하기 갯수저장
                                    // editor2.putInt((array.size() - position - 1) + "request_count2", item.request_count);
                                    editor2.putInt("request_count2", item.request_count);
                                    //아이디와 게시글 마다 신청하기,신청취소 버튼 상태 저장
                                    // editor2.putString(id + (array.size() - position - 1) + "request2", item.reuqest);
                                    editor2.putString(id + "request2", item.reuqest);
                                    //  editor.clear();
                                    editor2.commit();
                                    notifyDataSetChanged();

                                    //신청하기 누른 사람들 명단 저장하기


                                    SharedPreferences request = context.getSharedPreferences(array.size() - position - 1 + "request11", Context.MODE_PRIVATE);
                                    final SharedPreferences.Editor editor_request = request.edit();
                                    Gson gson2 = new Gson();

                                    //좋아요 명단 중복 막기
                                    request_member = new Item_request_member(profile_image, id);
                                    System.out.println("========================array_request의 사이즈는 : ========================" + array_request.size());
                                    if (array_request.size() == 0) {
                                        request_member = new Item_request_member(profile_image, id);
                                        array_request.add(0, request_member);
                                    } else if (array_request.size() > 0) {
                                        for (int i = 0; i < array_request.size(); i++) {

                                            if (!array_request.contains(request_member)) {

                                                array_request.add(request_member);

                                            }
                                        }
                                    }

                                    String json = gson2.toJson(array_request);

                                    editor_request.putString("request", json);

                                    editor_request.apply();
                                    notifyDataSetChanged();

                                    Toast.makeText(context, "프로젝트를 신청하였습니다.", Toast.LENGTH_SHORT).show();

                                }
                            });
                    builder.show();


                } else if (btn_request.getText().equals("신청취소")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("확인 메세지");
                    builder.setMessage("프로젝트 신청을 취소하시겠습니까?");
                    builder.setPositiveButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    item.reuqest = "신청하기";
                                    btn_request.setText(item.reuqest);
                                    item.request_count = item.request_count - 1;
                                    notifyDataSetChanged();

                                    //게시글 마다 신청하기 갯수저장
                                    // editor2.putInt((array.size() - position - 1) + "request_count2", item.request_count);
                                    editor2.putInt("request_count2", item.request_count);
                                    //아이디와 게시글 마다 좋아요,좋아요 취소 버튼 상태 저장
                                    //editor2.putString(id + (array.size() - position - 1) + "request2", item.reuqest);
                                    editor2.putString(id + "request2", item.reuqest);
                                    //editor.clear();
                                    editor2.commit();

                                    SharedPreferences request = context.getSharedPreferences(array.size() - position - 1 + "request11", Context.MODE_PRIVATE);

                                    final SharedPreferences.Editor editor_request = request.edit();
                                    Gson gson2 = new Gson();

                                    //좋아요 취소 누르면 해당 아이디 명단 빼기
                                    for (int i = 0; i < array_request.size(); i++) {
                                        if (id.equals(array_request.get(i).nick)) {

                                            array_request.remove(i);

                                        }
                                    }

                                    String json = gson2.toJson(array_request);

                                    editor_request.putString("request", json);

                                    editor_request.apply();

                                    System.out.println("========================array_request의 사이즈는 : ========================" + array_request.size());
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "프로젝트 신청을 취소하였습니다.", Toast.LENGTH_SHORT).show();

                                }
                            });
                    builder.show();


                } else if (btn_request.getText().toString().equals("모집완료")) {
                    Toast.makeText(context, "프로젝트 모집이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btn_request.setText(item.reuqest);


        //댓글 달기 버튼 리스너
        btn_message.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                int sum = array.size();
                Activity origin = (Activity) context;
                Intent intent = new Intent(view.getContext(), Message.class);
                intent.putExtra("sum", sum);
                intent.putExtra("position", position);

                origin.startActivityForResult(intent, 500);
            }
        });
        btn_message.setText(item.message);


        //메세지 클래스에서 댓글이 추가될 때 마다 댓글 수를 매개변수로 가지고 있는 객체를 만들어 주었음.
        // 그 객체가 변화시키는 값들을 sharedpreferences로 저장해서 여기서 받아옴.
        SharedPreferences sp = context.getSharedPreferences("message_count_plz", context.MODE_PRIVATE);
        final int sum = array.size();
        final int pos = position;
        final int hi = sum - pos - 1;
        final String hello = Integer.toString(hi);
        if (array.size() > 0)

        {
            for (int i = 0; i < array.size(); i++) {


                array.get(array.size() - i - 1).messeage_count = sp.getInt(hello.toString() + hello.toString() + "sss", 0);
                txt_message_count.setText(item.messeage_count + "");

            }
        } else
            txt_message_count.setText(0 + "");

//
//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        convertView.startAnimation(animation);
//        lastPosition = position;


        if (id.equals(array.get(position).nick)) {
            btn_delete.setVisibility(View.VISIBLE);
            btn_motify.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.INVISIBLE);
            btn_motify.setVisibility(View.INVISIBLE);
        }
        btn_motify.setText(item.motify);
        btn_delete.setText(item.delete);


        //게시글 수정 버튼
        btn_motify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("확인 메세지");
                builder.setMessage("이 프로젝트를 수정 하시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Activity origin = (Activity) context;
                                Intent intent = new Intent(view.getContext(), Modify_Project_Content.class);
                                //Request 클래스에 포지션값을 보내준다.
                                intent.putExtra("index", position);
                                intent.putExtra("name", array.get(position).getProject_name());
                                intent.putExtra("intro", array.get(position).getProject_intro());
                                intent.putExtra("ability", array.get(position).getProject_ability());
                                intent.putExtra("dev", array.get(position).getProject_develop_duration());
                                intent.putExtra("apply", array.get(position).getProject_apply_duration());
                                intent.putExtra("num", array.get(position).people_num);
                                intent.putExtra("history", array.get(position).history);
                                intent.putExtra("nick", array.get(position).nick);
                                intent.putExtra("image", array.get(position).profile_image);

                                origin.startActivityForResult(intent,0);


                            }
                        });
                builder.show();
            }
        });

        loadData();
        //게시글 삭제 버튼
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("확인 메세지");
                builder.setMessage("이 프로젝트를 삭제 하시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //홈 화면에서 삭제한 게시글 개인 페이지에서도 글이 삭제되도록 하기!
                                for(int i = 0; i<member.array_project.size();i++){

                                  //  if(member.array_project.get(i).getProject_name().equals(array.get(position).getProject_name())){
                                    if(member.array_project.get(i).history.equals(array.get(position).history)){
                                        System.out.println("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ"+member.array_project.get(i).history.equals(array.get(position).history));

                                        member.array_project.remove(i);
                                    }
                                }
                                saveData();
                                //좋아요 누른 사람들 명단 지우기
                                SharedPreferences like = context.getSharedPreferences(array.size() - position  -1+ "like6", Context.MODE_PRIVATE);
                                //SharedPreferences like = context.getSharedPreferences(position+ "like6", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor_like = like.edit();
                                editor_like.clear();
                                editor_like.commit();

                                //좋아요 갯수 제거
                                SharedPreferences like_count = context.getSharedPreferences(array.size() - position  -1+ "like_count2", Context.MODE_PRIVATE);
                                // SharedPreferences like_count = context.getSharedPreferences(position+ "like_count2", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor_like_count = like_count.edit();
                                editor_like_count.clear();
                                //editor_like_count.remove(array.size() - position - 1 + "like_count2");
                                editor_like_count.commit();

                                //신청하기 누른 사람들 명단 지우기
                                SharedPreferences request = context.getSharedPreferences(array.size() - position  -1+ "request11", Context.MODE_PRIVATE);
                                // SharedPreferences request = context.getSharedPreferences(position+ "request11", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor_request = request.edit();
                                editor_request.clear();
                                editor_request.commit();

                                //신청한 사람 수 제거
                                SharedPreferences request_count = context.getSharedPreferences(array.size() - position  -1+ "request_count2", Context.MODE_PRIVATE);
                                // SharedPreferences request_count = context.getSharedPreferences(position+"request_count2", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor_request_count = request_count.edit();
                                editor_request_count.clear();
                                editor_request_count.commit();

                                //댓글 수 제거
                                SharedPreferences sp = context.getSharedPreferences("message_count_plz", context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = sp.edit();
                                editor.remove(hello.toString() + hello.toString() + "sss");
                                editor.commit();

                                //댓글 목록 제거
                                SharedPreferences sp2 = context.getSharedPreferences(hello.toString() + hello.toString() +"message", context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor2 = sp2.edit();
                               // editor2.remove(hello.toString() + hello.toString() + "message");
                                editor2.clear();
                                editor2.commit();

                                //모집완료된 버튼 원상복구 시키기
                                SharedPreferences sp3 = context.getSharedPreferences(array.size() - position - 1 + "모집완료", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = sp3.edit();
                                editor3.clear();
                                btn_request.setText("신청하기");
                                editor3.commit();


                                //신청한 사람들 지우기
                                SharedPreferences sp4 = context.getSharedPreferences(array.size() - position - 1 + "accept_for_chat11", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor4 = sp4.edit();
                                editor4.clear();
                                editor4.commit();


                                //해당글 삭제
                                array.remove(position);






                                notifyDataSetChanged();
                               // saveData();


                                Toast.makeText(context, "작성하신 모집글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });



        if(item.nick.equals(id)){
            btn_request.setText(item.request_check);
        }

        if(btn_request.getText().toString().equals("모집확인")){
            btn_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity origin = (Activity) context;
                    Intent intent = new Intent(view.getContext(), Request.class);
                    //Request 클래스에 포지션값을 보내준다.
                    intent.putExtra("position", array.size() - position - 1);
                    intent.putExtra("nick", item.nick);
                    intent.putExtra("people_num", item.people_num);

                    origin.startActivity(intent);


                    item_editor.putInt("array_project", array.size());
                    item_editor.putInt("position", getPosition(item));
                    Log.i("----------------------position은 ", getPosition(item) + "");
                    item_editor.commit();

                }
            });
        }


        return convertView;


    }

    private void loadData() {
        final SharedPreferences sp5 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        final String id = sp5.getString("home", "");

        member = new Item_member(id);

        SharedPreferences sp = context.getSharedPreferences(id, context.MODE_PRIVATE);
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

        //arraylist 저장
        private void saveData() {
            final SharedPreferences sp6 = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
            final String id2 = sp6.getString("home", "");
            SharedPreferences sp2 = context.getSharedPreferences(id2, context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp2.edit();
            //리스트의 객체를 문자열 형식으로 바꿈
            Gson gson2 = new Gson();

            String json2 = gson2.toJson(member.array_project);

            editor.putString("task list", json2);

            editor.putString("project", String.valueOf(member.array_project));
            editor.apply();


            System.out.println("리스트 저장 됨");
        }
    }


