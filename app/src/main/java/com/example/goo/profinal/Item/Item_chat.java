package com.example.goo.profinal.Item;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-03.
 */

public class Item_chat {

    public String nick;
    public String profile_image;
    public String chat_content;
    public String history;
    ArrayList<Item_chat> array_chat = new ArrayList<>();
    public int color;

    public Item_chat() {

    }

    public Item_chat(String nick, String profile_image) {
        this.nick = nick;
        this.profile_image = profile_image;


    }

    public Item_chat(String nick, String profile_image, String chat_content, String history) {
        this.nick = nick;
        this.profile_image = profile_image;
        this.chat_content = chat_content;
        this.history = history;

    }


}
