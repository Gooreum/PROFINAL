package com.example.goo.profinal.Item;

/**
 * Created by Goo on 2018-02-23.
 */

public class Item_message {

    public String nick;
    public String image;
    public String message_content;
    public int messeage_count2;

    public Item_message() {
    }

    public Item_message(int messeage_count2) {
        this.messeage_count2 = messeage_count2;
    }

    public Item_message(String nick, String message_content) {
        this.nick = nick;
        this.message_content = message_content;
    }

    public Item_message(String nick, String message_content, String image) {
        this.nick = nick;
        this.message_content = message_content;
        this.image = image;

    }
}
