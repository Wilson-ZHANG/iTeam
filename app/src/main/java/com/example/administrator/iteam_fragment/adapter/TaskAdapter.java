package com.example.administrator.iteam_fragment.adapter;

/**
 * Created by cp on 2017/9/18.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.TaskOver;
import com.example.administrator.iteam_fragment.TaskReceiveContent;
import com.example.administrator.iteam_fragment.TaskSendContent;

import java.util.List;
import java.util.Map;

/**
 * Created by SML on 2017/4/7 0007.
 */

@SuppressWarnings("ALL")
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Map<String, Object>> mlistitems;
    private AppCompatActivity mainActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View taskView;
        ImageView taskImage;
        TextView taskName;
        TextView taskDes;
        ImageView openImage;

        public ViewHolder(View view) {
            super(view);
            taskView=view;
            taskImage=(ImageView)view.findViewById(R.id.task_image);
            taskName=(TextView)view.findViewById(R.id.task_name);
            taskDes=(TextView)view.findViewById(R.id.task_des);
            openImage=(ImageView)view.findViewById(R.id.task_open);
        }
    }

    public TaskAdapter(List<Map<String, Object>> listitems, AppCompatActivity mActivity){
        mainActivity = mActivity;
        mlistitems=listitems;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_listitem,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.openImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);

                String state = item.get("state").toString();
                if(state.equals("receive")) {
                    Intent intent = new Intent(mainActivity, TaskReceiveContent.class);
                    String taskId = item.get("taskId").toString();
                    intent.putExtra("id", taskId);
                    Log.d("enter", taskId);
                    mainActivity.startActivity(intent);
                } else{
                    Intent intent = new Intent(mainActivity, TaskSendContent.class);
                    String taskId = item.get("taskId").toString();
                    intent.putExtra("id", taskId);
                    Log.d("enter", taskId);
                    mainActivity.startActivity(intent);
                }
            }
        });
        holder.taskView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);

                String state = item.get("state").toString();
                if(state.equals("receive")) {
                    Intent intent = new Intent(mainActivity, TaskReceiveContent.class);
                    String taskId = item.get("taskId").toString();
                    intent.putExtra("id", taskId);
                    Log.d("enter", taskId);
                    mainActivity.startActivity(intent);
                } else if(state.equals("send")){
                    Intent intent = new Intent(mainActivity, TaskSendContent.class);
                    String taskId = item.get("taskId").toString();
                    intent.putExtra("id", taskId);
                    Log.d("enter", taskId);
                    mainActivity.startActivity(intent);
                } else{
                    Intent intent = new Intent(mainActivity, TaskOver.class);
                    String taskId = item.get("taskId").toString();
                    intent.putExtra("id", taskId);
                    Log.d("enter", taskId);
                    mainActivity.startActivity(intent);
                }
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("taskImage").toString().trim();
        Log.d("imageimage", url);
        Glide
                .with(mainActivity)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.taskImage);
        //holder.taskImage.setImageResource((Integer)item.get("taskImage"));
        holder.taskName.setText((String)item.get("taskName"));
        holder.taskDes.setText((String)item.get("taskDes"));
        holder.openImage.setImageResource((Integer)item.get("openImage"));

    }

    @Override
    public int getItemCount() {
        return mlistitems.size();

    }

}

