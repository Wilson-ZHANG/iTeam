package com.example.administrator.iteam_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.iteam_fragment.item.TaskItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.Constant.time;
import static com.example.administrator.iteam_fragment.LoginActivity.pref;

/**
 * Created by SML on 2017/4/16 0016.
 */

@SuppressWarnings("ALL")
public class TaskInTeamItem extends Activity{
    private boolean onReceive;//判断是已认领的任务还是未认领的任务
    private ImageView iv_headback;
    private TextView tv_taskName;
    private TextView tv_belongTeam;
    private TextView tv_initiator;
    private TextView tv_taskDes;
    private TextView tv_expectEndTime;
    private Button bt_take;

    //已认领的任务才会显示的控件
    private LinearLayout ly_receive;
    private TextView tv_master;
    private TextView tv_startTime;

    //已结束的任务才会显示的控件
    private LinearLayout ly_finish;
    private TextView tv_endTime;

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskItem> taskItems;
    private String taskid;
    String n;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskinteam_item);
        init();
        iv_headback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent =getIntent();

        taskid = intent.getStringExtra("id");
        Log.d("TaskInTeam", taskid);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });

        refreshMain();


    }

    private void init(){
        iv_headback=(ImageView)findViewById(R.id.head_back);
        tv_taskName=(TextView)findViewById(R.id.head_task_name);
        tv_belongTeam=(TextView)findViewById(R.id.tv_belong_team);
        tv_taskDes=(TextView)findViewById(R.id.tv_task_des);
        tv_expectEndTime=(TextView)findViewById(R.id.tv_tast_expectEndTime);
        tv_initiator=(TextView) findViewById(R.id.tv_initiator);
        bt_take=(Button)findViewById(R.id.bt_take);

        ly_receive=(LinearLayout)findViewById(R.id.ly_receive);
        tv_master=(TextView)findViewById(R.id.tv_master);
        tv_startTime=(TextView)findViewById(R.id.tv_startTime);

        ly_finish=(LinearLayout)findViewById(R.id.ly_finish);
        tv_endTime=(TextView)findViewById(R.id.tv_endTime);
    }

    private  void setcontent()
    {
        tv_taskName.setText(taskItems.get(0).taskName);
        tv_belongTeam.setText(taskItems.get(0).teamName);
        tv_taskDes.setText(taskItems.get(0).des);
        tv_initiator.setText(taskItems.get(0).pubName);
        tv_startTime.setText(time.format(taskItems.get(0).begin_time));

        if(taskItems.get(0).status==1) {
            tv_endTime.setText(time.format(taskItems.get(0).finish_time));

        }

        tv_expectEndTime.setText(time.format(taskItems.get(0).last_time));
        tv_master.setText(taskItems.get(0).leaderName);



        if(taskItems.get(0).leaderId==0)
            n = "0";
        else if(taskItems.get(0).status == 0)
            n = "1";
        else
            n = "2";

        initState();

    }

    private void refreshMain() {

        //Toast.makeText(this, "任务详情", Toast.LENGTH_SHORT).show();

        String url = "http://127.0.0.1/iteam/getTask.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("taskid",taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String param=jsonObject.toString();
        HttpUtil.getUtilsInstance().doPost(url, cookie,param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TaskRefresh", "onFailure: noresponse");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                Log.d("IN_getTask_response", responseText);
                // JSONArray jsonArray=new JSONArray(responseText);
//                Gson gson = new Gson();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                taskItems=gson.fromJson(responseText,
                        new TypeToken<ArrayList<TaskItem>>(){}.getType());
                Log.d("IN_getTask_taskItem", taskItems.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setcontent();
                    }
                });

            }
        });


        swipeRefreshLayout.setRefreshing(false);

    }

    private void initState()
    {
        switch (n){
            case "0":
                ly_receive.setVisibility(View.GONE);
                ly_finish.setVisibility(View.GONE);
                bt_take.setVisibility(View.VISIBLE);
                bt_take.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TaskInTeamItem.this,"认领任务",Toast.LENGTH_SHORT).show();

                        getTaskNow();
                    }
                });
                break;
            case "1":
                ly_receive.setVisibility(View.VISIBLE);
                ly_finish.setVisibility(View.GONE);
                bt_take.setVisibility(View.GONE);
                break;
            case "2":
                ly_receive.setVisibility(View.VISIBLE);
                ly_finish.setVisibility(View.VISIBLE);
                bt_take.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void getTaskNow() {
        Log.d( "getTaskNow:---", "begin");

        String url = "http://127.0.0.1/iteam/getDoTask.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("taskId",taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String param=jsonObject.toString();
        HttpUtil.getUtilsInstance().doPost(url, cookie,param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TaskRefresh", "onFailure: noresponse");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                Log.d("IN_getTask_response", responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "领取成功", Toast.LENGTH_SHORT).show();
                        //startActivity(it);
                        finish();
                    }
                });
                response.body().close();
            }

        });

    }

}
