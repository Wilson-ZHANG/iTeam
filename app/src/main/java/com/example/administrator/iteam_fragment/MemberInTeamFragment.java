package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.iteam_fragment.adapter.MemberAdapter;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
import com.example.administrator.iteam_fragment.item.MemberItem;
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
public class MemberInTeamFragment extends Fragment {
    private TeamContent teamActivity;
    private View view;

    private int myPower;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Map<String,Object>> listItems;
    private RecyclerView recyclerView;
    private ArrayList<MemberItem> memberItems=new ArrayList<>();
    private MemberAdapter adapter;
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
        view=inflater.inflate(R.layout.memberinteam_fragment,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.member_list);
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
        for(int i=0;i<memberItems.size();i++){
            Map<String,Object> listItem=new HashMap<String, Object>();
            listItem.put("memberName",memberItems.get(i).memberName);
            //listItem.put("memberPos", memberItems.get(i).friendId);
            listItem.put("memberImage",memberItems.get(i).pic);
            listItem.put("memberId",memberItems.get(i).memberId);
            listItem.put("creatorId",memberItems.get(i).creatorId);
            listItem.put("memberPhone",memberItems.get(i).memberPhone);
            listItem.put("teamId",teamId);
            //listItem.put("memberPosImage",memberImagePos[0]);
            listItems.add(listItem);
        }
    }


    private void setListAdapter(){

        setlistItem();
        LinearLayoutManager layoutManager=new LinearLayoutManager(teamActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MemberAdapter(listItems,teamActivity);
        recyclerView.setAdapter(adapter);

        //添加分界线
        if(init==0)
            recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                    DividerItemDecoration.VERTICAL_LIST));
        init = 1;
    }

    private void refreshMain(){
        String url="http://127.0.0.1/iteam/getMemberInTeam.php";
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

                String responseText=response.body().string();
                Log.d("teamMemberResponse",responseText);

                Gson gson=new Gson();
                memberItems=gson.fromJson(responseText,
                        new TypeToken<ArrayList<MemberItem>>(){}.getType());
                Log.d("memberItem",memberItems.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListAdapter();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }
}
