package com.example.administrator.iteam_fragment.item;

/**
 * Created by cp on 2017/11/3.
 */

@SuppressWarnings("ALL")
public class FriendItem {
    public int friendId;
    public String phone;
    public String friendName;

    public String pic;

    @Override
    public String toString() {
        return "FriendItem{" +
                "friendId=" + friendId +
                ", friendName='" + friendName + '\'' +
                ", pic='" + pic + '\'' +
                ", phone=" + phone +
                '}';
    }


    public FriendItem(int fid, String fname, String fphone, String pic_one)
    {
        friendId = fid;
        friendName = fname;
        pic = pic_one;
        phone =fphone;
    }
}
