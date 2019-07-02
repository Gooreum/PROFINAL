package com.example.goo.profinal.Item;

/**
 * Created by Goo on 2018-03-06.
 */

public class Item_following {
    public String nick;
    public String image;
    public String following;
    public String follow = "팔로우 취소";
    public String me = "본인";

    public Item_following() {
    }
    public Item_following(String nick  ) {
        this.nick = nick;

    }
    public Item_following(String nick, String image) {
        this.nick = nick;
        this.image = image;
    }
}
