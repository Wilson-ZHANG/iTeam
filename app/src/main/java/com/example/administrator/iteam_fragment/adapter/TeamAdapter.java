package com.example.administrator.iteam_fragment.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.MainActivity;
import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.TeamContent;
import com.example.administrator.iteam_fragment.TeamManageActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by SML on 2017/4/7 0007.
 */

@SuppressWarnings("ALL")
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private List<Map<String, Object>> mlistitems;
    private MainActivity mainActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View teamView;
        ImageView teamImage;
        TextView teamName;
        TextView teamRole;
        ImageView fileImage;
        ImageView openImage;

        public ViewHolder(View view) {
            super(view);
            teamView=view;
            teamImage=(ImageView)view.findViewById(R.id.team_image);
            teamName=(TextView)view.findViewById(R.id.team_name);
            teamRole=(TextView)view.findViewById(R.id.team_role);
            fileImage=(ImageView)view.findViewById(R.id.team_file);
            openImage=(ImageView)view.findViewById(R.id.team_open);
        }
    }

    public TeamAdapter(List<Map<String, Object>> listitems, MainActivity mActivity){
        mainActivity = mActivity;
        mlistitems=listitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.team_listitem,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.fileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                Toast.makeText(v.getContext(),"team "+position+"'s file clicked",Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 点击团队头像进入管理团队页面
         */
        holder.teamImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String teamName = item.get("teamName").toString();
                String teamId = item.get("teamId").toString();
                Intent intent=new Intent(mainActivity,TeamManageActivity.class);
                intent.putExtra("teamname",teamName);
                intent.putExtra("teamid",teamId);

                mainActivity.startActivity(intent);
            }
        });
        holder.openImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String teamName = item.get("teamName").toString();
                String teamId = item.get("teamId").toString();
                Intent intent=new Intent(mainActivity,TeamContent.class);
                intent.putExtra("teamname",teamName);
                intent.putExtra("teamid",teamId);
                mainActivity.startActivity(intent);
            }
        });
        holder.teamView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String teamName = item.get("teamName").toString();
                String teamId = item.get("teamId").toString();
                Intent intent=new Intent(mainActivity,TeamContent.class);
                intent.putExtra("teamname",teamName);
                intent.putExtra("teamid",teamId);
                mainActivity.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("teamImage").toString().trim();
        Log.d("imageimage", url);
        Glide
                .with(mainActivity)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.teamImage);
        //holder.teamImage.setImageResource((Integer)item.get("teamImage"));//这个你们之前好像注释了，不知道会不会有影响
        holder.teamName.setText((String)item.get("teamName"));
        holder.teamRole.setText((String)item.get("teamRole"));
        holder.fileImage.setImageResource((Integer)item.get("fileImage"));
        holder.openImage.setImageResource((Integer)item.get("openImage"));

    }



    @Override
    public int getItemCount() {
        return mlistitems.size();
    }
}

