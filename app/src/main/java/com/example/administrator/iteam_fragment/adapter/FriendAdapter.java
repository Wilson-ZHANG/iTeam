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
import com.example.administrator.iteam_fragment.ChatActivity;
import com.example.administrator.iteam_fragment.nav.FriendActivity;
import com.example.administrator.iteam_fragment.R;
import com.hyphenate.easeui.EaseConstant;

import java.util.List;
import java.util.Map;

/**
 * Created by cp on 2017/11/3.
 */

@SuppressWarnings("ALL")
public class FriendAdapter  extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<Map<String, Object>> mlistitems;
    private FriendActivity friendActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View friendView;
        ImageView friendImage;
        TextView friendName;
        TextView friendNumber;
        ImageView openImage;

        public ViewHolder(View view) {
            super(view);
            friendView=view;
            friendImage=(ImageView)view.findViewById(R.id.friend_image);
            friendName=(TextView)view.findViewById(R.id.friend_name);
            friendNumber=(TextView)view.findViewById(R.id.friend_number);
            openImage=(ImageView)view.findViewById(R.id.team_open);
        }
    }


    public FriendAdapter(List<Map<String, Object>> listitems, FriendActivity mActivity){
        friendActivity = mActivity;
        mlistitems=listitems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);


        holder.friendImage.setOnClickListener(new View.OnClickListener(){
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
        holder.openImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String name = item.get("friendName").toString();
                Intent intent=new Intent(friendActivity, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,name);
                friendActivity.startActivity(intent);
            }
        });
        holder.friendView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Map<String, Object> item=mlistitems.get(position);
                String name = item.get("friendName").toString();
                Intent intent=new Intent(friendActivity, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,name);
                friendActivity.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> item=mlistitems.get(position);
        String url=item.get("friendImage").toString().trim();

        Log.d("imageimage", url);
        Glide
                .with(friendActivity)
                .load(url)
                .placeholder(R.drawable.ic_launcher)

                .into(holder.friendImage);
        //holder.teamImage.setImageResource((Integer)item.get("teamImage"));//这个你们之前好像注释了，不知道会不会有影响
        holder.friendName.setText((String)item.get("friendName"));
        holder.friendNumber.setText((String)item.get("friendPhone"));
//        holder.friendImage.setImageResource((String)item.get("friendImage"));
        holder.openImage.setImageResource((Integer)item.get("openImage"));


    }



    @Override
    public int getItemCount() {
        return mlistitems.size();
    }
}

