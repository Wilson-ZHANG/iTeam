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
public class CreateTeam extends AppCompatActivity {

    private EditText teamName;
    private EditText teamDes;
    private Button createTeamButton;

    private String name;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        initView();
        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = teamName.getText().toString();
                des = teamDes.getText().toString();
                createTeam();
            }
        });
    }

    private void initView() {
        teamName = (EditText)findViewById(R.id.create_team_name);
        teamDes = (EditText)findViewById(R.id.create_team_des);
        createTeamButton = (Button)findViewById(R.id.create_team_button);
    }

    private void createTeam() {

        Toast.makeText(this, "创建团队成功", Toast.LENGTH_SHORT).show();

        String url = "http://127.0.0.1/iteam/createTeam.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("teamName",name);
            jsonObject.put("teamDes",des);
            Log.d("json",name+" "+des);
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
                Log.d("Create_team_response", responseText);
                // JSONArray jsonArray=new JSONArray(responseText);
                finish();
            }

        });
    }
}
