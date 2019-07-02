package com.example.goo.profinal.Item;

/**
 * Created by Goo on 2018-03-02.
 */

public class Item_follower {
    public String nick;
    public String image;
    public String following;
    public String me = "본인";

    public Item_follower() {
    }
    public Item_follower(String nick  ) {
        this.nick = nick;

    }
    public Item_follower(String nick, String image) {
        this.nick = nick;
        this.image = image;
    }
}
