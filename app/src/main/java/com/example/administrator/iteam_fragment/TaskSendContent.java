package com.example.administrator.iteam_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by SML on 2017/4/16 0016.
 */
@SuppressWarnings({"ALL", "DefaultFileTemplate"})
enum STATE{SEND,RECEIVE,FINISH}

@SuppressWarnings("ALL")
public class TaskSendContent extends Activity {
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
    private Button bt_finish;
    private List<TaskItem> taskItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String taskid;
    private String leaderId;
    private String status; //数据库中项目的完成情况
    //private String state; //可视化界面中的项目状态
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_send_content);
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
        //String n=intent.getStringExtra("State");
        String n = "0";
        Log.d("State",n);

        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempConfirm();
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
        tv_startTime=(TextView)findViewById(R.id.tx_start_time);

        ly_finish=(LinearLayout)findViewById(R.id.ly_finish);
        tv_endTime=(TextView)findViewById(R.id.tv_endTime);
        tv_feedBack=(TextView)findViewById(R.id.tv_feedback);
        gridView=(GridView)findViewById(R.id.pic_gridView);
        bt_finish=(Button)findViewById(R.id.bt_finish);
    }

    private void initState(String n) {
        switch (n) {
            case "0":
                state = STATE.SEND;
                break;
            case "1":
                state = STATE.RECEIVE;
                break;
            case "2":
                state = STATE.FINISH;
                break;
        }
        switch (state) {
            case SEND:
                ly_receive.setVisibility(View.GONE);
                ly_finish.setVisibility(View.GONE);
                iv_state.setImageResource(R.drawable.state_send);
                bt_finish.setVisibility(View.GONE);
                break;
            case RECEIVE:
                ly_receive.setVisibility(View.VISIBLE);
                ly_finish.setVisibility(View.GONE);
                iv_state.setImageResource(R.drawable.state_receive);
                bt_finish.setVisibility(View.GONE);
                tv_master.setText(taskItems.get(0).leaderName);
                break;
            case FINISH:
                ly_receive.setVisibility(View.VISIBLE);
                ly_finish.setVisibility(View.VISIBLE);
                iv_state.setImageResource(R.drawable.state_finish);
                bt_finish.setVisibility(View.VISIBLE);


                //装填gridView图片
//                listImageItem = new ArrayList<HashMap<String, Object>>();
//                for(int i=0;i<10;i++) {
//                    HashMap<String, Object> map = new HashMap<String, Object>();
//                    map.put("ItemImage", R.drawable.task);//添加图像资源的ID
//                    listImageItem.add(map);
//                }
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

                break;
        }

    }

    private void setAdapter(){
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
//        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
//                listImageItem,//数据来源
//                R.layout.pic_item,//night_item的XML实现
//
//                //动态数组与ImageItem对应的子项
//                new String[] {"ItemImage"},
//
//                //ImageItem的XML文件里面的一个ImageView
//                new int[] {R.id.pic_Item});
//
//        //显示bitmap资源
//        saImageItems.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object bitmapData, String s) {
//                if(view instanceof ImageView && bitmapData instanceof Bitmap){
//                    ImageView i = (ImageView)view;
//                    i.setImageBitmap((Bitmap) bitmapData);
//                    return true;
//                }
//                return false;
//            }
//        });

        //添加并且显示
        ImageListAdapter imageListAdapter=new ImageListAdapter(this,pics);
        gridView.setAdapter(imageListAdapter);
    }


    private  void setcontent()
    {
        tv_taskName.setText(taskItems.get(0).taskName);
        tv_belongTeam.setText(taskItems.get(0).teamName);
        tv_taskDes.setText(taskItems.get(0).des);

        if (taskItems.get(0).status >=1)
        tv_endTime.setText(time.format(taskItems.get(0).finish_time));

        tv_expectEndTime.setText(time.format(taskItems.get(0).last_time));
        tv_startTime.setText(time.format(taskItems.get(0).begin_time));

        String n;
        if(taskItems.get(0).leaderId==0)
            n = "0";
        else if(taskItems.get(0).status == 0)
            n = "1";
        else
            n = "2";

        initState(n);

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

    private void attempConfirm(){
        String url = "http://127.0.0.1/iteam/confirmTask.php";
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"确认成功，之后请在策划栏资料库中查看",Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });
    }

}


