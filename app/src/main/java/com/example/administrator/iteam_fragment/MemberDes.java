package com.example.administrator.iteam_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.pref;

@SuppressWarnings("ALL")
public class MemberDes extends AppCompatActivity {

    private ImageView img;
    private TextView name;
    private TextView phone;
    private Button buttonAdd;
    private Button buttonGiveRight;
    private Button buttonRemove;

    private String memberId;
    private String bossId;
    private String memberName;
    private String pic;
    private String memberPhone;
    private String teamId;
    private String userId;
    private boolean ifBoss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_des);

        initView();
        Intent intent = getIntent();
        memberId = intent.getStringExtra("memberId");
        bossId = intent.getStringExtra("bossId");
        memberName = intent.getStringExtra("memberName");
        pic = intent.getStringExtra("pic");
        memberPhone = intent.getStringExtra("memberPhone");
        teamId = intent.getStringExtra("teamId");
        userId = pref.getString("userId",null);
        ifBoss = bossId.equals(userId);
        Toast.makeText(this,memberId+" "+bossId,Toast.LENGTH_SHORT).show();

        setContent();

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attempAddFriends();
            }
        });

        buttonGiveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempGiveRight();
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempRemove();
            }
        });

        pref=getSharedPreferences("Pref", Context.MODE_PRIVATE);

    }



    private void initView() {
        img = (ImageView)findViewById(R.id.member_image);
        name = (TextView) findViewById(R.id.member_name);
        phone = (TextView)findViewById(R.id.member_phone);
        buttonAdd = (Button)findViewById(R.id.add_friend);
        buttonGiveRight = (Button)findViewById(R.id.give_right);
        buttonRemove = (Button) findViewById(R.id.remove_member);
    }

    private void setContent() {
        Glide
                .with(this)
                .load(pic)
                .placeholder(R.drawable.ic_launcher)

                .into(img);

        name.setText(memberName);
        phone.setText(memberPhone);

        if(!ifBoss) {
            buttonRemove.setVisibility(View.GONE);
            buttonGiveRight.setVisibility(View.GONE);
        }

    }

    private void attempAddFriends(){
        Log.d( "attempAddFriends:---", "begin");

        String url = "http://127.0.0.1/iteam/addFriends.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("memberId",memberId);
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
                        Toast.makeText(getApplicationContext(), "添加好友成功", Toast.LENGTH_SHORT).show();
                        //startActivity(it);
                        finish();
                    }
                });
                response.body().close();
            }

        });

    }

    private void attempGiveRight(){
        Log.d( "attempAddFriends:---", "begin");

        String url = "http://127.0.0.1/iteam/giveRightToMember.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("memberId",memberId);
            jsonObject.put("teamId",teamId);
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
                        finish();
                    }
                });
                response.body().close();
            }

        });
    }

    private void attempRemove(){
        Log.d( "attempAddFriends:---", "begin");

        String url = "http://127.0.0.1/iteam/removeMemberFromTeam.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("memberId",memberId);
            jsonObject.put("teamId",teamId);
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
                        finish();
                    }
                });
                response.body().close();
            }

        });
    }
}
