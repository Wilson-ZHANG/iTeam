package com.example.administrator.iteam_fragment.item;

/**
 * Created by cp on 2017/9/14.
 */

@SuppressWarnings("ALL")
public class TeamItem {
    public String teamName;
    public String des;
    public String  teamId;
    public String pic;

    @Override
    public String toString() {
        return "TeamItem{" +
                "teamName='" + teamName + '\'' +
                ", des='" + des + '\'' +
                ", teamId=" + teamId +
                ", pic='" + pic + '\'' +
                '}';
    }
}
