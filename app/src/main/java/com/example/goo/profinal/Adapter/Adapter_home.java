package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;

import java.util.ArrayList;


/**
 * Created by Goo on 2018-02-05.
 */

public class Adapter_home extends ArrayAdapter<Item_project> {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Activity context;

    private int id;
    ArrayList<Item_project> array;


    String nick;
    int profileImage;

    public Adapter_home(Activity context, int resource, ArrayList<Item_project> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array = objects;
        //this.nick = nick;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final Item_project item = array.get(position);
        final TextView txt_nick = convertView.findViewById(R.id.txt_nick);
        //txt_nick.setText(nick);
        //  txt_nick.setText(item.nick);
        txt_nick.setText(item.nick);
        System.out.println("아이템의 닉 네임은 : " + item.nick);
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


        //글번호
        //TextView number = (TextView) convertView.findViewById(R.id.number);
        //setText 안에 들어가야 하는 내용은 반드시 String이어야 한다.... 그래서 숫자가 들어갈 경우 반드시 "" 를 덧붙여 줘야 함.
        // number.setText(array.size() - position + "");


        //수정버튼
        Button btn_modify = (Button) convertView.findViewById(R.id.btn_modify);

        //체크박스
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cbx_work);


        //체크박스 리스너
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIsChecked(isChecked);

            }
        });

        checkBox.setChecked(item.isChecked());


//        //수정버튼 리스너
//        btn_modify.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                LayoutInflater inflater = LayoutInflater.from(view.getContext());
//                final View v = inflater.inflate(R.layout.custom_dialog, null);
//                builder.setTitle("수정");
//                builder.setMessage("수정값을 입력하세요.");
//                builder.setView(v);
//
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText dial_work = (EditText) v.findViewById(R.id.dial_work);
//                        EditText dial_hour = (EditText) v.findViewById(R.id.dial_hour);
//                        EditText dial_min = (EditText) v.findViewById(R.id.dial_min);
//
//                        String work = dial_work.getText().toString();
//                        String time = dial_hour.getText().toString() + " 시" + dial_min.getText().toString() +" 분";
//
//                        item.setWork(work);
//                        item.setTime(time);
//                        notifyDataSetChanged();
//                    }
//                });
//                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//
//                builder.create().show();
//            }
//        });
        return convertView;


    }


}

