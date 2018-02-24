package com.example.administrator.iteam_fragment.nav;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.adapter.TaskAdapter;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
import com.example.administrator.iteam_fragment.item.TaskItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.pref;

@SuppressWarnings("ALL")
public class MyTaskActivity extends AppCompatActivity {


    private View view;

    private int[] photoimageIdsRec=new int[]{R.drawable.task,R.drawable.task,R.drawable.task};
    private int[] openimageIdsRec=new int[]{R.drawable.open,R.drawable.open,R.drawable.open};
    private int[] photoimageIdsSend=new int[]{R.drawable.task,R.drawable.task,R.drawable.task};
    private int[] openimageIdsSend=new int[]{R.drawable.open,R.drawable.open,R.drawable.open};


    private List<Map<String,Object>> listItems;
    private TextView tvReceive;
    private TextView tvSend;
    boolean onReceive;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新

    private ArrayList<TaskItem> taskItems=new ArrayList<>();
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView back;

    private int init=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_my_task);
        tvReceive=(TextView) findViewById(R.id.tv_receive);
        tvSend=(TextView) findViewById(R.id.tv_send);
        recyclerView=(RecyclerView) findViewById(R.id.task_list);
        back = (ImageView)findViewById(R.id.my_task_head_back);
        // list=(ListView) view.findViewById(R.id.task_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });
        refreshMain(); //测试用，先注释
        setListAdapter();
        onReceive=true;


        tvReceive.setOnClickListener(new View.OnClickListener() {
            private Handler handler = new Handler() {

                public void handleMessage(Message msg){
                    onReceive=true;
                    tvReceive.setBackgroundResource(R.drawable.bg_borderselect);
                    tvSend.setBackgroundResource(R.drawable.bg_border);
                    tvReceive.setTextColor(Color.BLACK);
                    tvSend.setTextColor(Color.GRAY);
                    refreshMain();
                }
            };

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            private Handler handler = new Handler() {

                public void handleMessage(Message msg){
                    onReceive=false;
                    tvReceive.setBackgroundResource(R.drawable.bg_border);
                    tvSend.setBackgroundResource(R.drawable.bg_borderselect);
                    tvReceive.setTextColor(Color.GRAY);
                    tvSend.setTextColor(Color.BLACK);
                    listItems.clear();
                    refreshMain();
                }
            };

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setlistItem(){
        if(listItems==null)
            listItems=new ArrayList<Map<String, Object>>();
        listItems.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<taskItems.size();i++) {
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    listItem.put("taskName", taskItems.get(i).taskName);
                    listItem.put("taskDes", taskItems.get(i).teamName);
                    listItem.put("taskImage",taskItems.get(i).pic);
                    listItem.put("openImage", openimageIdsRec[0]);
                    listItem.put("taskId",taskItems.get(i).taskId);
                    listItem.put("leaderId",taskItems.get(i).leaderId);
                    listItem.put("state","over");
                    listItems.add(listItem);
                }
            }
        }).start();

    }


    private void setListAdapter(){


        setlistItem();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new TaskAdapter(listItems,this);
        recyclerView.setAdapter(adapter);

        if(init == 0)
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        init = 1;
    }



    private void refreshMain()
    {
        if(onReceive) {

            Toast.makeText(this, "这是接受的任务", Toast.LENGTH_SHORT).show();

            String url = "http://127.0.0.1/iteam/getMyTaskRec.php";
            String cookie = pref.getString("cookie", null);
            HttpUtil.getUtilsInstance().doPost(url, cookie, "", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TaskRefresh", "onFailure: noresponse");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText = response.body().string();
                    Log.d("getTaskRec_response", responseText);
                    // JSONArray jsonArray=new JSONArray(responseText);
                    Gson gson = new Gson();
                    taskItems = gson.fromJson(responseText,
                            new TypeToken<ArrayList<TaskItem>>() {
                            }.getType());
                    Log.d("getTaskRec_taskItems", taskItems.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
        else{
            Toast.makeText(this, "这是发出的任务", Toast.LENGTH_SHORT).show();

            String url = "http://127.0.0.1/iteam/getMyTaskSend.php";
            String cookie = pref.getString("cookie", null);
            HttpUtil.getUtilsInstance().doPost(url, cookie, "", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TaskRefresh", "onFailure: noresponse");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText = response.body().string();
                    Log.d("getTaskSend_response", responseText);
                    // JSONArray jsonArray=new JSONArray(responseText);
                    Gson gson = new Gson();
                    taskItems = gson.fromJson(responseText,
                            new TypeToken<ArrayList<TaskItem>>() {
                            }.getType());
                    Log.d("getTaskSend_taskItems", taskItems.toString());
                   runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }

        swipeRefreshLayout.setRefreshing(false);
    }


}
