package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.iteam_fragment.adapter.ImageListAdapter;
import com.example.administrator.iteam_fragment.item.TaskItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
 * Created by cp on 2017/11/7.
 */

@SuppressWarnings("ALL")
public class TaskOver extends AppCompatActivity{
    private TaskSendContent aActivity;
    private STATE state;
    private ImageView iv_headback;
    private TextView tv_taskName;
    private TextView tv_belongTeam;
    private TextView tv_taskDes;
    private TextView tv_expectEndTime;
    private ImageView iv_state;
    //任务被接受后才会显示的控件
    private LinearLayout ly_receive;
    private TextView tv_master;
    private TextView tv_startTime;
    //任务结束后才会显示的控件
    private LinearLayout ly_finish;
    private TextView tv_endTime;
    private TextView tv_feedBack;
    private GridView gridView;
    //    private ArrayList<HashMap<String, Object>> listImageItem;
    private ArrayList<String> pics=new ArrayList<>();

    private List<TaskItem> taskItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String taskid;
    private String leaderId;
    private String status; //数据库中项目的完成情况
    //private String state; //可视化界面中的项目状态
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_over);
        aActivity=new TaskSendContent();
        init();
        Intent intent =getIntent();
        taskid = intent.getStringExtra("id");
        getData();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });
        iv_headback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        iv_headback=(ImageView)findViewById(R.id.head_back);
        tv_taskName=(TextView)findViewById(R.id.head_task_name);
        tv_belongTeam=(TextView)findViewById(R.id.tv_belong_team);
        tv_taskDes=(TextView)findViewById(R.id.tv_task_des);
        tv_expectEndTime=(TextView)findViewById(R.id.tv_tast_expectEndTime);
        iv_state=(ImageView)findViewById(R.id.iv_state);

        ly_receive=(LinearLayout)findViewById(R.id.ly_receive);
        tv_master=(TextView)findViewById(R.id.tv_master);
        tv_startTime=(TextView)findViewById(R.id.tv_startTime);

        ly_finish=(LinearLayout)findViewById(R.id.ly_finish);
        tv_endTime=(TextView)findViewById(R.id.tv_endTime);
        tv_feedBack=(TextView)findViewById(R.id.tv_feedback);
        gridView=(GridView)findViewById(R.id.pic_gridView);

        // initState();
    }

    private void initState() {

        ly_receive.setVisibility(View.VISIBLE);
        ly_finish.setVisibility(View.VISIBLE);
        iv_state.setImageResource(R.drawable.state_finish);



        String url2 = "http://127.0.0.1/iteam/getTaskPics.php";
        String cookie = pref.getString("cookie", null);
        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("taskId",taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String param=jsonObject.toString();
        HttpUtil.getUtilsInstance().doPost(url2, cookie, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("llll",param);
                String text=response.body().string();
                Log.d("hhhhh",text);
                try {
                    JSONArray jsonArray=new JSONArray(text);
                    pics.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1= (JSONObject) jsonArray.get(i);
                        Log.d("iiii",jsonObject1.toString());
                        pics.add(i,jsonObject1.getString("pic"));
                    }
                    aActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        tv_master.setText(taskItems.get(0).leaderName);
        tv_feedBack.setText(taskItems.get(0).feedBack);



    }

    private void setAdapter(){

        //添加并且显示
        ImageListAdapter imageListAdapter=new ImageListAdapter(this,pics);
        gridView.setAdapter(imageListAdapter);
    }


    private  void setcontent()
    {
        tv_taskName.setText(taskItems.get(0).taskName);
        tv_belongTeam.setText(taskItems.get(0).teamName);
        tv_taskDes.setText(taskItems.get(0).des);
        tv_endTime.setText(time.format(taskItems.get(0).finish_time));
        tv_startTime.setText(time.format(taskItems.get(0).begin_time));
        tv_expectEndTime.setText(time.format(taskItems.get(0).last_time));
        initState();
    }

    private void getData(){
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
                Log.d("send_Task_response", responseText);
                // JSONArray jsonArray=new JSONArray(responseText);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                taskItems=gson.fromJson(responseText,
                        new TypeToken<ArrayList<TaskItem>>(){}.getType());
                Log.d("send_Task_taskItem", taskItems.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setcontent();
                    }
                });

            }
        });
    }
    private void refreshMain() {
        getData();

        swipeRefreshLayout.setRefreshing(false);

    }

}
