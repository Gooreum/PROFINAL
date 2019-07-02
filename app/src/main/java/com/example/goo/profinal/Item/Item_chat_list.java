package com.example.goo.profinal.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-05.
 */

public class Item_chat_list {

    public String nick;
    public String profile_image;
    public String chat_content;
    public String history;
    ArrayList<Item_chat> array_chat = new ArrayList<>();
    ;

    public Item_chat_list() {

    }

    public Item_chat_list(String chat_content) {
        this.chat_content = chat_content;
    }


    public Item_chat_list(String nick, String history, String chat_content) {
        this.nick = nick;
        this.history = history;
        this.chat_content = chat_content;
    }

//
//    }public Item_chat_list(String nick, String profile_image) {
//        this.nick = nick;
//        this.profile_image = profile_image;
//
//
//    }
//    public Item_chat_list(String nick, String profile_image, String chat_content) {
//        this.nick = nick;
//        this.profile_image = profile_image;
//        this.chat_content = chat_content;
//
//    }
}
