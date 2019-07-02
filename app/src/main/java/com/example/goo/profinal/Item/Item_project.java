package com.example.goo.profinal.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-05.
 */

public class Item_project {
    Item_member member = new Item_member();
    Item_message messeage = new Item_message();
    public String connection = member.following;
    public String nick;
    public String profile_image;
    private String project_name;
    private String project_intro;
    private String project_ability;
    private String project_develop_duration;
    private String project_apply_duration;
    public String people_num;
    private boolean isChecked;
    public String message = "댓글";
    public String like = "좋아요";
    public int like_count = 0;
    public int messeage_count = 0;
    public int content_count = 0;
    public String request_check = "모집확인";

    public String history;
    public int second;
    public int min;
    public int hour;

    public String reuqest = "신청하기";
    public int request_count = 0;

    public String delete = "삭제";
    public String motify = "수정";

    public ArrayList<Item_project> array_project;
    public ArrayList<Item_member> array_member;

    public Item_project() {
    }

    public Item_project(String project_name, String project_intro, String project_ability, String project_develop_duration, String project_apply_duration, String people_num) {
        this.project_name = project_name;
        this.project_intro = project_intro;
        this.project_ability = project_ability;
        this.project_develop_duration = project_develop_duration;
        this.project_apply_duration = project_apply_duration;
        this.people_num = people_num;

    }

    //생성자
    public Item_project(String project_name, String project_intro, String project_ability, String project_develop_duration, String project_apply_duration, String people_num, String nick, String history, String profile_image) {
        this.project_name = project_name;
        this.project_intro = project_intro;
        this.project_ability = project_ability;
        this.project_develop_duration = project_develop_duration;
        this.project_apply_duration = project_apply_duration;
        this.people_num = people_num;
        this.nick = nick;
        this.history = history;
        this.profile_image = profile_image;
    }




    //프로젝트 이름
    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }


    //프로젝트 소개
    public String getProject_intro() {
        return project_intro;
    }

    public void setProject_intro(String project_intro) {
        this.project_intro = project_intro;
    }


    //프로젝트 사용 기술
    public String getProject_ability() {
        return project_ability;
    }

    public void setProject_ability(String project_ability) {
        this.project_ability = project_ability;
    }


    //프로젝트 개발 기간
    public String getProject_develop_duration() {
        return project_develop_duration;
    }

    public void setProject_develop_duration(String project_develop_duration) {
        this.project_develop_duration = project_develop_duration;
    }

    public String getProject_apply_duration() {
        return project_apply_duration;
    }


    //프로젝트 모집 기간
    public void setProject_apply_duration(String project_apply_duration) {
        this.project_apply_duration = project_apply_duration;
    }


    //체크박스
    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


}
