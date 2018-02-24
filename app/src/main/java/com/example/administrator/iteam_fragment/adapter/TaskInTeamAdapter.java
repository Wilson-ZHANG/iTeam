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
import com.example.administrator.iteam_fragment.TaskInTeamItem;
import com.example.administrator.iteam_fragment.TeamContent;
import com.example.administrator.iteam_fragment.R;


import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/10/25.
 */

@SuppressWarnings("ALL")
public class TaskInTeamAdapter extends RecyclerView.Adapter<TaskInTeamAdapter.ViewHolder>{
    private List<Map<String, Object>> mlistitems;
    private TeamContent teamContent;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View taskView;
        ImageView taskImage;
        TextView taskName;
        TextView taskLeader;
        ImageView openImage;

        public ViewHolder(View view) {
            super(view);
            taskView=view;
            taskImage=(ImageView)view.findViewById(R.id.task_image);
            taskName=(TextView)view.findViewById(R.id.task_name);
            taskLeader=(TextView)view.findViewById(R.id.task_des);
            openImage=(ImageView)view.findViewById(R.id.task_open);
        }
    }

    public TaskInTeamAdapter(List<Map<String, Object>> listitems, TeamContent teamActivity){
        teamContent = teamActivity;
        mlistitems=listitems;
    }



    @Override
    public TaskInTeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_listitem,parent,false);
        final TaskInTeamAdapter.ViewHolder holder=new TaskInTeamAdapter.ViewHolder(view);

        holder.openImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String taskId = item.get("taskId").toString();
                Intent intent=new Intent(teamContent,TaskInTeamItem.class);
                intent.putExtra("id",taskId);
                Log.d("enterInTeam", taskId);
                teamContent.startActivity(intent);
            }
        });
        holder.taskView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String taskId = item.get("taskId").toString();
                Intent intent=new Intent(teamContent,TaskInTeamItem.class);
                intent.putExtra("id",taskId);
                Log.d("enterInTeam", taskId);
                teamContent.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskInTeamAdapter.ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("taskImage").toString().trim();
        Log.d("imageimage", url);
        Glide
                .with(teamContent)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.taskImage);
        //     holder.taskImage.setImageResource((Integer)item.get("taskImage"));
        holder.taskName.setText((String)item.get("taskName"));
        holder.taskLeader.setText((String)item.get("taskLeader"));
//        holder.openImage.setImageResource((Integer)item.get("openImage"));

    }

    @Override
    public int getItemCount() {
        return mlistitems.size();

    }
}
