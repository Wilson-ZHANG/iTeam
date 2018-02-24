package com.example.administrator.iteam_fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.pref;


@SuppressWarnings("ALL")
public class CreateTask extends AppCompatActivity {

    private String taskName;
    private String taskDes;
    private String endTime;
    EditText editTaskName;
    EditText editTextDes;
    EditText editTextYear;
    EditText editTextMonth;
    EditText editTextDay;

    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Button mbutton=(Button)findViewById(R.id.submit);

        initView();

        teamId = getIntent().getStringExtra("teamid");
        mbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendMessage();
            }
        });
    }

    private void initView() {
        editTaskName = (EditText)findViewById(R.id.create_task_name);
        editTextDes = (EditText)findViewById(R.id.create_task_des);
        editTextYear= (EditText) findViewById(R.id.date);
        editTextMonth= (EditText) findViewById(R.id.month);
        editTextDay=(EditText)findViewById(R.id.day);

    }

    private void getContent() {
        taskName = editTaskName.getText().toString();
        taskDes = editTextDes.getText().toString();
        endTime=editTextYear.getText().toString()+editTextMonth.getText().toString()+editTextDay.getText().toString();
    }

    private void sendMessage()
    {

        getContent();
        Toast.makeText(this, "创建任务成功", Toast.LENGTH_SHORT).show();

        String url = "http://127.0.0.1/iteam/createTask.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("taskName",taskName);
            jsonObject.put("taskDes",taskDes);
            jsonObject.put("teamId",teamId);
            jsonObject.put("endTime",endTime);
            Log.d("json",taskName+" "+taskDes+" "+teamId);
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
                // JSONArray jsonArray=new JSONArray(responseText);
                finish();
            }

        });
    }
}
