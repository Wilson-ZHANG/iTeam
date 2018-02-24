package com.example.administrator.iteam_fragment.nav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.iteam_fragment.R;
import com.example.administrator.iteam_fragment.SearchFriend;
import com.example.administrator.iteam_fragment.adapter.FriendAdapter;
import com.example.administrator.iteam_fragment.divider_item.DividerItemDecoration;
import com.hyphenate.easeui.domain.EaseUser;

import org.json.JSONArray;
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
public class FriendActivity extends AppCompatActivity {

    private List<Map<String,Object>> listItems = new ArrayList<>();

    private ImageView searchFrinedImage;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initView();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.friend_recycle_id);
        searchFrinedImage = (ImageView) findViewById(R.id.search_friend_image);
        back = (ImageView)findViewById(R.id.friend_head_back);
        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);
        FriendAdapter adapter = new FriendAdapter(listItems,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        searchFrinedImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchFriend();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView()
    {
        if(listItems==null)
            listItems=new ArrayList<Map<String, Object>>();
        listItems.clear();
        String path="http://127.0.0.1/iteam/getFriends.php";
        String cookie = pref.getString("cookie", null);
        final Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        HttpUtil.getUtilsInstance().doPost(path, cookie, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getFriends", "Fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d("aaa",responseText);
                try {
                    JSONArray jsonArray = new JSONArray(responseText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Map<String,Object> listItem=new HashMap<String, Object>();
                        listItem.put("friendName",jsonObject.getString("name"));
                        listItem.put("friendId",jsonObject.getString("id"));
                        listItem.put("friendPhone",jsonObject.getString("phone"));
                        listItem.put("friendImage",jsonObject.getString("pic"));
                        listItem.put("openImage",R.drawable.open);
                        listItems.add(listItem);
//                        notifyAll();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                response.body().close();
            }
        });
    }

    private void searchFriend() {
        Intent intent = new Intent(this,SearchFriend.class);
        startActivity(intent);
    }
}