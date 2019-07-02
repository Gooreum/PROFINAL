package com.example.goo.profinal.Item;

/**
 * Created by Goo on 2018-03-02.
 */

public class Item_request_member {

    public String profile_image;
    public String nick;
    public String accept = "승낙";

    public int accept_count;
    public Item_request_member() {
    }

    public Item_request_member(String profile_image, String nick) {
        this.profile_image = profile_image;
        this.nick = nick;
    }
}
