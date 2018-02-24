package com.example.administrator.iteam_fragment;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.iteam_fragment.adapter.GroupAdapter;
import com.example.administrator.iteam_fragment.item.TeamItem;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
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
public class GroupFragment extends Fragment {
    private MainActivity mActivity;
    private View view;

    private ArrayList<TeamItem> teamItems=new ArrayList<>();


    private List<Map<String,Object>> listItems;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private int init = 0;
    GroupAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        view=inflater.inflate(R.layout.team_fragment,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.team_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });

        setListAdapter();

        refreshMain();  //第一次进入填充好
        return view;
    }

    private void setListItems(){
        if(listItems==null)
            listItems=new ArrayList<Map<String, Object>>();
        listItems.clear();
        for(int i=0;i<teamItems.size();i++){
            Map<String,Object> listItem=new HashMap<String, Object>();
            listItem.put("teamName", teamItems.get(i).teamName);
            listItem.put("teamRole",teamItems.get(i).des);
            listItem.put("teamImage",teamItems.get(i).pic);
            listItem.put("teamId",teamItems.get(i).teamId);
            listItem.put("fileImage",R.drawable.libraries);
            listItem.put("openImage",R.drawable.open);
            listItems.add(listItem);
        }
    }
    @TargetApi(23)
    private void setListAdapter(){
        setListItems();

        Log.d("setList","touch");
        Log.d("listItems",listItems.toString());
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupAdapter(listItems,mActivity);
        recyclerView.setAdapter(adapter);

        //添加分界线
        if(init == 0 )
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                DividerItemDecoration.VERTICAL_LIST));

        init = 1;
    }

    private void refreshMain()
    {
        // Toast.makeText(mActivity,"这是团队",Toast.LENGTH_SHORT).show();

        String url="http://127.0.0.1/iteam/getAllTeam.php";
        String cookie=pref.getString("cookie",null);
        HttpUtil.getUtilsInstance().doPost(url, cookie, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText=response.body().string();
                Log.d("teamResponse",responseText);
                // JSONArray jsonArray=new JSONArray(responseText);
                Gson gson=new Gson();
                teamItems=gson.fromJson(responseText,
                        new TypeToken<ArrayList<TeamItem>>(){}.getType());
                Log.d("teamItem",teamItems.toString());
                mActivity.runOnUiThread(new Runnable() {
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
