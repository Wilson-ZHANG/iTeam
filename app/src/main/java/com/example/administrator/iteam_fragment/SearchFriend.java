package com.example.administrator.iteam_fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.pref;

@SuppressWarnings("ALL")
public class SearchFriend extends AppCompatActivity {


    private ImageView friendImage;
    private EditText friendPhone;
    private TextView friendName;
    private Button buttonSearch;
    private Button buttonAdd;

    private String id;
    private String name;
    private String pic;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friend);

        initView();

        buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchFriend();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }

    private void initView() {
        friendImage  = (ImageView)findViewById(R.id.friend_result_image);
        friendPhone = (EditText)findViewById(R.id.search_friend_phone);
        friendName = (TextView)findViewById(R.id.friend_result_name);
        buttonSearch = (Button) findViewById(R.id.search_friend_phone_button);
        buttonAdd = (Button)findViewById(R.id.add_friend_result_button);


    }

    private void setContent() {
        friendName.setText(name);
        Glide
                .with(this)
                .load(pic)
                .placeholder(R.drawable.ic_launcher)

                .into(friendImage);

    }
    private void searchFriend() {
        Log.d( "attempAddFriends:---", "begin");

        String url = "http://127.0.0.1/iteam/getUserInfo.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        String phonenum = friendPhone.getText().toString();
        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("phone",phonenum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String param=jsonObject.toString();
        HttpUtil.getUtilsInstance().doPost(url, cookie,param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TaskRefresh", "onFailure: noresponse");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "无此用户", Toast.LENGTH_SHORT).show();
                        //startActivity(it);
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                Log.d("iii", responseText);
                try {
                    JSONObject jsonObject1=new JSONObject(responseText);
                    id = jsonObject1.getString("id");
                    pic = jsonObject1.getString("pic");
                    phone = jsonObject1.getString("phone");
                    name = jsonObject1.getString("name");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContent();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "无此用户", Toast.LENGTH_SHORT).show();
                            //startActivity(it);
                            finish();
                        }
                    });
                }

                response.body().close();
            }

        });

    }

    private void addFriend() {
        Log.d( "attempAddFriends:---", "begin");

        String url = "http://127.0.0.1/iteam/addFriends.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("memberId",id);
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
                        Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                        //startActivity(it);
                        finish();
                    }
                });
                response.body().close();
            }

        });


    }
}
