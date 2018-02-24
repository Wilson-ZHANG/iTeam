package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.iteam_fragment.adapter.TaskInTeamAdapter;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
import com.example.administrator.iteam_fragment.item.TaskItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

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
public class TaskInTeamFragment extends Fragment {
    private boolean isFree;//判断是否被认领
    private TeamContent teamActivity;
    private View view;

    private int[] photoimageIds1 = new int[]{R.drawable.task, R.drawable.task, R.drawable.task};
    private int[] openimageIds1 = new int[]{R.drawable.open, R.drawable.open, R.drawable.open};

    private int[] photoimageIds2 = new int[]{R.drawable.task, R.drawable.task};
    private int[] openimageIds2 = new int[]{R.drawable.open, R.drawable.open};
    private List<Map<String, Object>> listItems;
    private TextView tv_received;
    private TextView tv_unReceived;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private TaskInTeamAdapter adapter;

    private ArrayList<TaskItem> taskInTeamItems = new ArrayList<>();
    private String teamid;
    private int init = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TaskInTeamFragment", "onCreate: ----");

        super.onCreate(savedInstanceState);
        isFree = true;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        teamActivity = (TeamContent) getActivity();
        Intent intent = teamActivity.getIntent();
        teamid = intent.getStringExtra("teamid");
        view = inflater.inflate(R.layout.taskinteam_fragment, container, false);
        tv_received = (TextView) view.findViewById(R.id.tv_received);
        tv_unReceived = (TextView) view.findViewById(R.id.tv_unReceived);
        recyclerView = (RecyclerView) view.findViewById(R.id.taskinteam_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });

        refreshMain();
        setListAdapter();
//        recyclerView.setClickListener(new View.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //   Toast.makeText(teamActivity,"item "+position+" clicked",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(teamActivity, TaskInTeamItem.class);
//                //3表示未接收，4表示未完成，5表示已完成
//                if (onReceived) {
//                    switch (position) {
//                        case 0:
//                            intent.putExtra("taskState", "4");
//                            break;
//                        default:
//                            intent.putExtra("taskState", "5");
//                            break;
//                    }
//                } else {
//                    intent.putExtra("taskState", "3");
//                }
//                startActivity(intent);
//
//            }
//        });
        tv_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFree = false;
                tv_received.setBackgroundResource(R.drawable.bg_borderroundselect);
                tv_unReceived.setBackgroundResource(R.drawable.bg_borderround);
                tv_received.setTextColor(Color.BLACK);
                tv_unReceived.setTextColor(Color.GRAY);
                refreshMain();

            }
        });

        tv_unReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFree = true;
                tv_received.setBackgroundResource(R.drawable.bg_borderround);
                tv_unReceived.setBackgroundResource(R.drawable.bg_borderroundselect);
                tv_received.setTextColor(Color.GRAY);
                tv_unReceived.setTextColor(Color.BLACK);
                // setlistItem2();
                refreshMain();
            }
        });

        return view;
    }

    private void setlistItem() {
        if (listItems == null)
            listItems = new ArrayList<Map<String, Object>>();
        listItems.clear();
        for (int i = 0; i < taskInTeamItems.size(); i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("taskName", taskInTeamItems.get(i).taskName);
            listItem.put("taskDes", taskInTeamItems.get(i).des);
            listItem.put("taskImage", taskInTeamItems.get(i).pic);
            listItem.put("open", openimageIds1[0]);
            listItem.put("taskId",taskInTeamItems.get(i).taskId);
            if(!isFree)
                listItem.put("taskLeader",taskInTeamItems.get(i).leaderName);
            listItems.add(listItem);
        }
    }


    private void setListAdapter() {
        setlistItem();
        LinearLayoutManager layoutManager=new LinearLayoutManager(teamActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskInTeamAdapter(listItems,teamActivity);
        recyclerView.setAdapter(adapter);

        if(init == 0)
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                DividerItemDecoration.VERTICAL_LIST));

        init = 1;
    }

    private void refreshMain() {

        if(isFree) {
            //Toast.makeText(teamActivity, "这是未被接受的任务", Toast.LENGTH_SHORT).show();

            String url = "http://127.0.0.1/iteam/getFreeTaskInTeam.php";
            String cookie = pref.getString("cookie", null);
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("teamId",teamid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String param=jsonObject.toString();
            HttpUtil.getUtilsInstance().doPost(url, cookie, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TaskInTeamRefresh", "onFailure: noresponse");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText = response.body().string();
                    Log.d("getFreeTIT_response", responseText);
                    // JSONArray jsonArray=new JSONArray(responseText);
                    Gson gson = new Gson();
                    taskInTeamItems = gson.fromJson(responseText,
                            new TypeToken<ArrayList<TaskItem>>() {
                            }.getType());
                    Log.d("etFreeTIT_taskItems", taskInTeamItems.toString());
                    teamActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }else {
            //Toast.makeText(teamActivity, "这是已经被接受的任务", Toast.LENGTH_SHORT).show();

            String url = "http://127.0.0.1/iteam/getOcpTaskInTeam.php";
            String cookie = pref.getString("cookie", null);
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("teamId",teamid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String param=jsonObject.toString();
            HttpUtil.getUtilsInstance().doPost(url, cookie, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TaskInTeamRefresh", "onFailure: noresponse");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText = response.body().string();
                    Log.d("getOcpTIT_response", responseText);
                    // JSONArray jsonArray=new JSONArray(responseText);
                    Gson gson = new Gson();
                    taskInTeamItems = gson.fromJson(responseText,
                            new TypeToken<ArrayList<TaskItem>>() {
                            }.getType());
                    Log.d("getOcpTIT_taskItems", taskInTeamItems.toString());
                    teamActivity.runOnUiThread(new Runnable() {
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



