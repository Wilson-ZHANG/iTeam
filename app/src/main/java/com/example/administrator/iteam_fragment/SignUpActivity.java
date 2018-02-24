package com.example.administrator.iteam_fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.iteam_fragment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.editor;
import static com.example.administrator.iteam_fragment.LoginActivity.pref;

@SuppressWarnings("ALL")
public class SignUpActivity extends AppCompatActivity {


    EditText phone;
    EditText code;
    EditText name;
    EditText password;
    Button getcode;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击注册", Toast.LENGTH_SHORT).show();
                String path = "http://127.0.0.1/iteam/signup.php";
                String userPhone = phone.getText().toString();
                String passWord = password.getText().toString();
                String userCode = code.getText().toString();
                String userName = name.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("code", userCode);
                    jsonObject.put("phone", userPhone);
                    jsonObject.put("password", passWord);
                    jsonObject.put("name", userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String param = jsonObject.toString();
                final String cookie = pref.getString("cookie", null);
                HttpUtil.getUtilsInstance().doPost(path, cookie, param, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("SignUp", "Wrong");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "sign up failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("SignUp", response.body().string().trim());
                        response.body().close();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "sign up success",Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                });


                    }
                });
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qqqqqqqqqqq","hhhhhh");
                Toast.makeText(getApplicationContext(),"dianji",Toast.LENGTH_LONG).show();
                String path = "http://127.0.0.1/iteam/sendcode.php";
                String phoneNum = phone.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone", phoneNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String param = jsonObject.toString();
                HttpUtil.getUtilsInstance().doPost(path, null, param, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("SendCode", "Wrong");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Headers headers = response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        if (!cookies.isEmpty()) {
                            String session = cookies.get(0);
                            Log.d("info_cookies", "onResponse-size: " + cookies);

                            String s = session.substring(0, session.indexOf(";"));
                            Log.i("info_s", "session is  :" + s);
                            editor.putString("cookie", s);
                            editor.apply();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "验证码发送成功",Toast.LENGTH_SHORT).show();
                                }
                            }); }
                        Log.d("Code", response.body().string().trim());
                        response.body().close();
                    }
                });

            }

        });}

    private void initView() {
        phone = (EditText) findViewById(R.id.phone_sign_up);
        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);
        getcode = (Button) findViewById(R.id.getcode);
        confirm = (Button) findViewById(R.id.confirm_sign_up);
        password=(EditText)findViewById(R.id.sign_up_password);

    }


}