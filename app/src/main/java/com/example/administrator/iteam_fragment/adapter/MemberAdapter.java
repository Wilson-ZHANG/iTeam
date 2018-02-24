package com.example.administrator.iteam_fragment.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.MemberDes;
import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.TeamContent;

import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/11/4.
 */

@SuppressWarnings("ALL")
public class MemberAdapter  extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private List<Map<String, Object>> mlistitems;
    private TeamContent teamContent;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View memberView;
        ImageView memberImage;
        TextView memberName;
        TextView memberNumber;
        ImageView memberPos;
        TextView memberPosname;
        int pos;

        public ViewHolder(View view) {
            super(view);
            memberView=view;
            memberImage=(ImageView)view.findViewById(R.id.iv_memberImage);
            memberName=(TextView)view.findViewById(R.id.tv_memberName);
            memberNumber=(TextView)view.findViewById(R.id.tv_memberPos);
            memberPos=(ImageView)view.findViewById(R.id.iv_memberPos);
            memberPosname=(TextView)view.findViewById(R.id.tv_memberPos);
        }
    }


    public MemberAdapter(List<Map<String, Object>> listitems, TeamContent mActivity){
        teamContent = mActivity;
        mlistitems=listitems;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item,parent,false);
        final MemberAdapter.ViewHolder holder=new ViewHolder(view);


        holder.memberImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                Map<String, Object> item=mlistitems.get(position);
//                String teamName = item.get("teamName").toString();
//                String teamId = item.get("teamId").toString();
//                Intent intent=new Intent(this,TeamManageActivity.class);
//                intent.putExtra("teamname",teamName);
//                intent.putExtra("teamid",teamId);

                // mainActivity.startActivity(intent);
            }
        });
        holder.memberPos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                Map<String, Object> item=mlistitems.get(position);
//                String teamName = item.get("teamName").toString();
//                String teamId = item.get("teamId").toString();
//                Intent intent=new Intent(mainActivity,TeamContent.class);
//                intent.putExtra("teamname",teamName);
//                intent.putExtra("teamid",teamId);
//                mainActivity.startActivity(intent);
            }
        });
        holder.memberView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String memberId = item.get("memberId").toString();
                String bossId = item.get("creatorId").toString();
                String memberName = item.get("memberName").toString();
                String pic = item.get("memberImage").toString();
                String phone = item.get("memberPhone").toString();
                Intent intent=new Intent(teamContent,MemberDes.class);
                String teamId = item.get("teamId").toString();
                intent.putExtra("memberId",memberId);
                intent.putExtra("bossId",bossId);
                intent.putExtra("memberName",memberName);
                intent.putExtra("pic",pic);
                intent.putExtra("memberPhone",phone);
                intent.putExtra("teamId",teamId);
                teamContent.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("memberImage").toString().trim();

        Log.d("imageimage", url);
        Glide
                .with(teamContent)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.memberImage);
        //holder.teamImage.setImageResource((Integer)item.get("teamImage"));//这个你们之前好像注释了，不知道会不会有影响
        holder.memberName.setText((String)item.get("memberName"));
        // holder.memberNumber.setText((String)item.get("friendRole"));
        // holder.memberPos.setImageResource((Integer)item.get("memberPos"));
        if(item.get("memberId").toString().equals(item.get("creatorId").toString())) {
            holder.memberPos.setImageResource(R.drawable.leader);
            holder.memberPosname.setText("组长");
        }
        else {
            holder.memberPos.setImageResource(R.drawable.mate);
            holder.memberPosname.setText("组员");
        }



    }



    @Override
    public int getItemCount() {
        return mlistitems.size();
    }
}
