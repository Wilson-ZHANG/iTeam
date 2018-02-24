package com.example.administrator.iteam_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import util.HttpUtil;

@SuppressWarnings("ALL")
public class LoginActivity extends Activity{
    public static  SharedPreferences.Editor editor;
    public static SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);
//        EMClient.getInstance().chatManager().getChatOptions().setUseRoster(true);
//        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        setContentView(R.layout.activity_login);
        final Intent it=new Intent(LoginActivity.this,MainActivity.class);

        pref=getSharedPreferences("Pref", Context.MODE_PRIVATE);
        editor = pref.edit();
        if(pref.getString("loginpasswd",null)!=null)
            Log.d("prefprefpref",pref.getString("loginpasswd",null));
        final EditText phone= (EditText) findViewById(R.id.phone);
        final EditText password= (EditText) findViewById(R.id.password);
        final EditText code= (EditText) findViewById(R.id.code);
        final EditText name= (EditText) findViewById(R.id.name);

        Button login= (Button) findViewById(R.id.login);
        TextView signup= (TextView) findViewById(R.id.sign_up);
        if(pref.getString("phone",null)!=null && pref.getString("loginpasswd",null)!=null&&pref.getString("userName",null)!=null) {
            Log.d("pref2","hhhhhhhhhhh");
            loginit(pref.getString("phone",null),pref.getString("loginpasswd",null),pref.getString("userName",null));
        }



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击注册",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone=phone.getText().toString();
                String userpassWord=password.getText().toString();
                if(TextUtils.isEmpty(userPhone)){
                    userPhone="12345678901";
                    userpassWord="qwertyuiop";
                }
                String path="http://127.0.0.1/iteam/login.php";
                final JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("phone",userPhone);
                    jsonObj.put("password",userpassWord);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString("loginpasswd",userpassWord);
                editor.apply();
                String param=jsonObj.toString();
                HttpUtil.getUtilsInstance().doPost(path, null, param, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Login","Wrong");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Headers headers = response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        if (!cookies.isEmpty()) {
                            String session = cookies.get(0);
                            String s = session.substring(0, session.indexOf(";"));
                            Log.i("cookie", "session is  :" + s);
                            editor.putString("cookie", s);
                            editor.apply();
                        }
                        final String responseText=response.body().string();
                        Log.d("Test",responseText);
                        try {
                            JSONObject jsonObject=new JSONObject(responseText);
                            String flag=jsonObject.getString("flag");
                            if(flag.equals("success")){
                                editor.putString("userName",jsonObject.getString("name"));
                                editor.putString("phone",jsonObject.getString("phone"));
                                editor.putString("userHeadPic",jsonObject.getString("pic").trim());
                                editor.apply();
                                Log.d("head",jsonObject.getString("pic").trim());
                                EMClient.getInstance().login(jsonObject.getString("name"), password.getText().toString(), new EMCallBack() {

                                    @Override
                                    public void onSuccess() {
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        Log.d("main", "登录聊天服务器成功！");
                                        startActivity(it);
                                        finish();
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "login failed",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                finish();
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                                        //startActivity(it);
                                        finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    }
                });


            }
        });
    }
    private void loginit(String phone,String pwd,String name){
        String path="http://127.0.0.1/iteam/login.php";
        final JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("phone",phone);
            jsonObj.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString("loginpasswd",pwd);
        editor.apply();
        String param=jsonObj.toString();
        HttpUtil.getUtilsInstance().doPost(path, null, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Login","Wrong");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                if (!cookies.isEmpty()) {
                    String session = cookies.get(0);
                    String s = session.substring(0, session.indexOf(";"));
                    Log.i("cookie", "session is  :" + s);
                    editor.putString("cookie", s);
                    editor.apply();
                }
                final String responseText=response.body().string();
                Log.d("Test",responseText);
                try {
                    JSONObject jsonObject=new JSONObject(responseText);
                    String flag=jsonObject.getString("flag");
                    if(flag.equals("success")){
                        editor.putString("userName",jsonObject.getString("name"));
                        editor.putString("phone",jsonObject.getString("phone"));
                        editor.putString("userHeadPic",jsonObject.getString("pic").trim());
                        editor.apply();
//                        Log.d("head",jsonObject.getString("pic").trim());
                        EMClient.getInstance().login(jsonObject.getString("name"), pref.getString("loginpasswd",null), new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("main", "登录聊天服务器成功！");
                                Intent it=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(it);
                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String error) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "login failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        finish();
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                                //startActivity(it);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                response.body().close();
            }
        });

    }
}