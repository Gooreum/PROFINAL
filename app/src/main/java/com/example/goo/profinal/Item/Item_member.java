package com.example.goo.profinal.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-12.
 * 회원가입 할 경우 회원 목록 관리해주는 아이템 클래스
 */

public class Item_member {

    private String id;
    private String nick;
    private String password;
    private String email;
    public String profileImage;


    public ArrayList<Item_project> array_project;
    public ArrayList<Item_member> array_member;
    public ArrayList<Item_message> array_message;
    public String me = "본인";
    public String following = "팔로우";
    public String like = "좋아요";

    public int content_count = 0;
//    private boolean isClicked;


    public Item_member() {
        array_project = new ArrayList<Item_project>();
        array_member = new ArrayList<Item_member>();
        array_message = new ArrayList<>();
    }

    public Item_member(String nick) {
        this.nick = nick;

        array_project = new ArrayList<Item_project>();

        array_member = new ArrayList<Item_member>();

    }

    public Item_member(String id, String nick, String password) {
        this.id = id;
        this.nick = nick;
        this.password = password;

        array_project = new ArrayList<Item_project>();
        array_member = new ArrayList<Item_member>();
    }

    public Item_member(String id, String nick, String password, String profileImage) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.profileImage = profileImage;
        array_project = new ArrayList<Item_project>();
        array_member = new ArrayList<Item_member>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
