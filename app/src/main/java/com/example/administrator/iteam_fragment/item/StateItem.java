package com.example.administrator.iteam_fragment.item;

/**
 * Created by SuperTayson on 2017/12/16.
 */

@SuppressWarnings("ALL")
public class StateItem {
    public int memberId;
    public String memberName;
    public int creatorId;
    public String pic;
//    public String date;
    public String time;
    public String state;
    public String task;
    @Override
    public String toString() {
        return "MemberItem{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", pic='" + pic + '\'' +
                ", creatorId="+creatorId+
//                ", date="+date+
                ", time="+time+
                ", state="+state+
                '}';
    }


    public StateItem(int fid, String fname, String pic_one,int cid,String ftime,String fstate,String taskName)
    {
        memberId = fid;
        memberName = fname;
        pic = pic_one;
        creatorId = cid;
//        date=fdate;
        time=ftime;
        state=fstate;
        task=taskName;
    }
}

