package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.iteam_fragment.adapter.StateAdapter;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
import com.example.administrator.iteam_fragment.item.StateItem;
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
    public class StateInTeamFragment extends Fragment {
        private TeamContent teamActivity;
        private View view;

        private int myPower;
        private SwipeRefreshLayout swipeRefreshLayout;

        private List<Map<String,Object>> listItems;
        private RecyclerView recyclerView;
        private ArrayList<StateItem> stateItems=new ArrayList<>();
        private StateAdapter adapter;
        private String teamId;
        private int init = 0;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            teamActivity = (TeamContent) getActivity();
            Intent intent = teamActivity.getIntent();
            teamId = intent.getStringExtra("teamid");

            //更改权限图标
            setlistItem();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            teamActivity = (TeamContent) getContext();
            view=inflater.inflate(R.layout.stateinteam_fragment,container,false);
            recyclerView=(RecyclerView) view.findViewById(R.id.state_list);
            setListAdapter();
            //获取“我”的权限
            myPower = 0;



            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshMain();
                }
            });
            refreshMain();
            return view;
        }

        private void setlistItem(){
            if(listItems==null)
                listItems=new ArrayList<Map<String, Object>>();
            listItems.clear();
            for(int i=0;i<stateItems.size();i++){
                Map<String,Object> listItem=new HashMap<String, Object>();
                listItem.put("memberName",stateItems.get(i).memberName);
                //listItem.put("memberPos", memberItems.get(i).friendId);
                listItem.put("memberImage",stateItems.get(i).pic);
                listItem.put("memberId",stateItems.get(i).memberId);
                listItem.put("creatorId",stateItems.get(i).creatorId);
                listItem.put("taskName",stateItems.get(i).task);
                listItem.put("time",stateItems.get(i).time);
                listItem.put("state",stateItems.get(i).state);
                listItem.put("teamId",teamId);
                //listItem.put("memberPosImage",memberImagePos[0]);
                listItems.add(listItem);
            }
        }


        private void setListAdapter(){

            setlistItem();
            LinearLayoutManager layoutManager=new LinearLayoutManager(teamActivity);
            recyclerView.setLayoutManager(layoutManager);
            adapter=new StateAdapter(listItems,teamActivity);
            recyclerView.setAdapter(adapter);

            //添加分界线
            if(init==0)
                recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                        DividerItemDecoration.VERTICAL_LIST));
            init = 1;
        }

        private void refreshMain(){
            Log.d("stateRes","hhhhh");
            String url="http://127.0.0.1/iteam/getHistory.php";
            String cookie=pref.getString("cookie",null);

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("teamId",teamId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String param=jsonObject.toString();
            HttpUtil.getUtilsInstance().doPost(url, cookie, param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText=response.body().string().trim();

                    Log.d("stateResponse",responseText);
                    if(!responseText.equals("[]") && !TextUtils.isEmpty(responseText)) {
                        Gson gson = new Gson();
                        stateItems = gson.fromJson(responseText,
                                new TypeToken<ArrayList<StateItem>>() {
                                }.getType());
                        Log.d("stateItem", stateItems.toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setListAdapter();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });

            swipeRefreshLayout.setRefreshing(false);
        }
    }
