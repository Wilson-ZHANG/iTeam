package com.example.administrator.iteam_fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.iteam_fragment.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SML on 2017/4/16 0016.
 */

@SuppressWarnings("ALL")
public class TeamContent extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_teamHeadBack;
    private ImageView iv_addTask;
    private TextView tv_teamName;
    private TaskInTeamFragment taskfragment;
    private MemberInTeamFragment memberfragment;
    private ImageView iv_task;
    private ImageView iv_member;
    private ImageView iv_state;
    private TextView tv_task;
    private TextView tv_member;
    private TextView tv_state;
    private FragmentManager fragmentManager;

    //滑动屏
    private ViewPager vp_content;
    private FragmentAdapter mfragmentAdapter;
    private List<android.support.v4.app.Fragment> fragmentList;
    private boolean taskChosen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Team", "onCreate:-------- ");
        setContentView(R.layout.team_content);
        Log.e("Team", "finish:---------- ");
        init();

        taskChosen = true;

        fragmentList.add(new TaskInTeamFragment());
        fragmentList.add(new MemberInTeamFragment());
        fragmentList.add(new StateInTeamFragment());

        mfragmentAdapter=new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp_content.setOffscreenPageLimit(2);//ViewPager的缓存为2帧
        vp_content.setAdapter(mfragmentAdapter);

        vp_content.setCurrentItem(0);//初始设置ViewPager选中第一帧

        //ViewPager的监听事件
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               /*此方法在页面被选中时调用*/
                clearSelection();
                changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
               arg0 ==1的时辰默示正在滑动，
               arg0==2的时辰默示滑动完毕了，
               arg0==0的时辰默示什么都没做。*/
            }
        });

        iv_teamHeadBack.setOnClickListener(this);
        iv_addTask.setOnClickListener(this);
        iv_task.setOnClickListener(this);
        iv_member.setOnClickListener(this);
        iv_state.setOnClickListener(this);

    }

    private void init(){
        iv_teamHeadBack=(ImageView)findViewById(R.id.team_head_back);
        iv_addTask=(ImageView)findViewById(R.id.iv_addTask);
        tv_teamName=(TextView)findViewById(R.id.head_team_name);
        iv_task=(ImageView)findViewById(R.id.iv_task);
        iv_member=(ImageView)findViewById(R.id.iv_member);
        iv_state=(ImageView)findViewById(R.id.iv_state);
        tv_task=(TextView)findViewById(R.id.tv_task);
        tv_member=(TextView)findViewById(R.id.tv_member);
        tv_state=(TextView)findViewById(R.id.tv_state);
        fragmentManager = getFragmentManager();

        vp_content=(ViewPager)findViewById(R.id.vp_team_content);
        fragmentList = new ArrayList<android.support.v4.app.Fragment>();

        String teamName  = getIntent().getStringExtra("teamname");
        tv_teamName.setText(teamName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_task:
                // 当点击了消息tab时，选中第1个tab
                vp_content.setCurrentItem(0,true);
                taskChosen = true;
                break;
            case R.id.iv_member:
                // 当点击了联系人tab时，选中第2个tab
                vp_content.setCurrentItem(1,true);
                taskChosen = false;
                break;
            case R.id.iv_state:
                // 当点击了联系人tab时，选中第3个tab
                vp_content.setCurrentItem(2,true);
                taskChosen = false;
                break;
            case R.id.team_head_back:
                finish();
                break;
            case R.id.iv_addTask:
                if(taskChosen) {
                    Toast.makeText(this, "新建任务", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeamContent.this, CreateTask.class);
                    String teamId = getIntent().getStringExtra("teamid");
                    intent.putExtra("teamid", teamId);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "加入新成员", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeamContent.this, AddMemberToTeam.class);
                    String teamId = getIntent().getStringExtra("teamid");
                    intent.putExtra("teamid", teamId);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }


    private void clearSelection(){
        iv_task.setImageResource(R.drawable.task_unselected);
        tv_task.setTextColor(Color.GRAY);

        iv_member.setImageResource(R.drawable.friend_unselected);
        tv_member.setTextColor(Color.GRAY);

        iv_state.setImageResource(R.drawable.team_unselected);
        tv_state.setTextColor(Color.GRAY);
    }
    private void changeColor(int position){
        switch (position){
            case 0:
                iv_task.setImageResource(R.drawable.task_selected);
                tv_task.setTextColor(Color.BLACK);
                break;
            case 1:
                iv_member.setImageResource(R.drawable.friend_selected);
                tv_member.setTextColor(Color.BLACK);
                break;
            case 2:
                iv_state.setImageResource(R.drawable.team_selected);
                tv_state.setTextColor(Color.BLACK);
                break;
            default:
                break;
        }
    }

}
