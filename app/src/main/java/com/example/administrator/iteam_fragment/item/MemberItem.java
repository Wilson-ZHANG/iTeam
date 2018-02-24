package com.example.administrator.iteam_fragment.item;

/**
 * Created by cp on 2017/11/4.
 */

@SuppressWarnings("ALL")
public class MemberItem {
    public int memberId;
    public String memberPhone;
    public String memberName;
    public int creatorId;
    public String pic;

    @Override
    public String toString() {
        return "MemberItem{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", pic='" + pic + '\'' +
                ", memberPhone=" + memberPhone +
                ", creatorId="+creatorId+
                '}';
    }


    public MemberItem(int fid, String fname, String fphone, String pic_one,int cid)
    {
        memberId = fid;
        memberName = fname;
        pic = pic_one;
        memberPhone =fphone;
        creatorId = cid;
    }
}
