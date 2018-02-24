package com.example.administrator.iteam_fragment.item;


import java.util.Date;

/**
 * Created by cp on 2017/9/18.
 */

@SuppressWarnings("ALL")
public class TaskItem {
    public int taskId;
    public String taskName;
    public String des;

    public String  teamId;
    public String teamName;
    public String pic;
    public int leaderId;
    public int status;
    public String leaderName;
    public String pubName;
    public String feedBack;
    public Date begin_time;
    public Date finish_time;
    public Date last_time;


    @Override
    public String toString() {
        return "TaskItem{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", des='" + des + '\'' +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", pic='" + pic + '\'' +
                ", leaderId=" + leaderId +
                ", status=" + status +
                ", leaderName='" + leaderName + '\'' +
                ", pubName='" + pubName + '\'' +
                ", feedBack='" + feedBack + '\'' +
                ", begin_time=" + begin_time +
                ", finish_time=" + finish_time +
                ", last_time=" + last_time +
                '}';
    }
}
