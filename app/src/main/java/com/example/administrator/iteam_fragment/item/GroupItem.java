package com.example.administrator.iteam_fragment.item;

/**
 * Created by cp on 2017/9/19.
 */

@SuppressWarnings("ALL")
public class GroupItem {
    public int groupId;
    public String groupName;
    public String taskName;
    public String pic;
    public int leaderId;

    @Override
    public String toString() {
        return "GroupItem{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", pic='" + pic + '\'' +
                ", leaderId=" + leaderId +
                '}';
    }


    public GroupItem(int id, String name, String tName, String pic_one, int lid)
    {
        groupId = id;
        groupName = name;
        taskName = tName;
        pic = pic_one;
        leaderId = lid;
    }
}
