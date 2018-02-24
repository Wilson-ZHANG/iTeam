package com.example.administrator.iteam_fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.TeamContent;

import java.util.List;
import java.util.Map;



@SuppressWarnings("ALL")
public class StateAdapter  extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
    private List<Map<String, Object>> mlistitems;
    private TeamContent teamContent;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View stateView;
        ImageView memberImage;
        TextView memberName;
        TextView memberNumber;
        TextView opTime;
        TextView opTx;
        TextView taskName;
        ImageView memberPos;
        TextView memberPosname;
        int pos;

        public ViewHolder(View view) {
            super(view);
            stateView=view;
            memberImage=(ImageView)view.findViewById(R.id.iv_memberImage);
            memberName=(TextView)view.findViewById(R.id.tv_memberName);
            memberNumber=(TextView)view.findViewById(R.id.tv_memberPos);
            memberPos=(ImageView)view.findViewById(R.id.iv_memberPos);
            opTime= (TextView) view.findViewById(R.id.tv_stateTime);
            opTx= (TextView) view.findViewById(R.id.tv_op);
            taskName= (TextView) view.findViewById(R.id.tv_taskName);
            memberPosname=(TextView)view.findViewById(R.id.tv_memberPos);

        }
    }


    public StateAdapter(List<Map<String, Object>> listitems, TeamContent mActivity){
        teamContent = mActivity;
        mlistitems=listitems;
    }

    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item,parent,false);
        final StateAdapter.ViewHolder holder=new ViewHolder(view);


//        holder.memberImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                Map<String, Object> item=mlistitems.get(position);
//                String teamName = item.get("teamName").toString();
//                String teamId = item.get("teamId").toString();
//                Intent intent=new Intent(this,TeamManageActivity.class);
//                intent.putExtra("teamname",teamName);
//                intent.putExtra("teamid",teamId);

        // mainActivity.startActivity(intent);
//            }
//        });
//        holder.memberPos.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                Map<String, Object> item=mlistitems.get(position);
//                String teamName = item.get("teamName").toString();
//                String teamId = item.get("teamId").toString();
//                Intent intent=new Intent(mainActivity,TeamContent.class);
//                intent.putExtra("teamname",teamName);
//                intent.putExtra("teamid",teamId);
//                mainActivity.startActivity(intent);
//            }
//        });
//        holder.memberView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                Map<String, Object> item=mlistitems.get(position);
//                String memberId = item.get("memberId").toString();
//                String bossId = item.get("creatorId").toString();
//                String memberName = item.get("memberName").toString();
//                String pic = item.get("memberImage").toString();
//                String phone = item.get("memberPhone").toString();
//                Intent intent=new Intent(teamContent,MemberDes.class);
//                String teamId = item.get("teamId").toString();
//                intent.putExtra("memberId",memberId);
//                intent.putExtra("bossId",bossId);
//                intent.putExtra("memberName",memberName);
//                intent.putExtra("pic",pic);
//                intent.putExtra("memberPhone",phone);
//                intent.putExtra("teamId",teamId);
//                teamContent.startActivity(intent);

//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("memberImage").toString().trim();

        Log.d("imageimage", url);
        Glide
                .with(teamContent)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.memberImage);
        holder.taskName.setText((String)item.get("taskName"));
        holder.opTime.setText((String)item.get("time"));
        holder.memberName.setText((String)item.get("memberName"));
        if(item.get("state").toString().equals("0")){
            holder.opTx.setText("发出");
        }else if(item.get("state").toString().equals("1")){
            holder.opTx.setText("领取");
        }else {
            holder.opTx.setText("完成并提交");
        }
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
