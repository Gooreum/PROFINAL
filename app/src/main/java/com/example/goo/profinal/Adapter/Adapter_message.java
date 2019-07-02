package com.example.goo.profinal.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Item.Item_message;
import com.example.goo.profinal.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-02-23.
 */

public class Adapter_message extends ArrayAdapter<Item_message> {

    private Activity context;

    private int id;
    ArrayList<Item_message> array_message;

    Uri uri;
    public Adapter_message(Activity context, int resource, ArrayList<Item_message> objects) {
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.array_message = objects;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);


        }

        final Item_message message = array_message.get(position);

      //  Button btn_motify_content = convertView.findViewById(R.id.btn_motify_message);
        Button btn_del_content = convertView.findViewById(R.id.btn_del_message);

        final TextView txt_id = convertView.findViewById(R.id.txt_id);
        txt_id.setText(message.nick);
        final TextView txt_message = convertView.findViewById(R.id.txt_message);
        txt_message.setText(message.message_content);
         ImageView profile_image = convertView.findViewById(R.id.profile_image);

        final SharedPreferences sp = context.getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");

        //댓글 아이디와 지금 로그인한 아이디가 같을 경우에만 댓글 수정 및 삭제가 가능하다.
        if (!id.equals(txt_id.getText().toString())) {
            btn_del_content.setVisibility(View.INVISIBLE);
           // btn_motify_content.setVisibility(View.INVISIBLE);
        }


        uri =Uri.parse(message.image);
        profile_image.setImageURI(uri);

        btn_del_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("확인 메세지");
                builder.setMessage("댓글을 삭제 하시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (id.equals(array_message.get(position).nick)) {

                                    array_message.remove(position);

                                    notifyDataSetChanged();
                                }

                                Toast.makeText(context, "댓글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        });
                builder.show();

            }
        });

        return convertView;
    }
}
