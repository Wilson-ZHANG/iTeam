package com.example.administrator.iteam_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.iteam_fragment.item.TaskItem;
import com.example.administrator.iteam_fragment.util.Bimp;
import com.example.administrator.iteam_fragment.util.FileUtils;
import com.example.administrator.iteam_fragment.util.ImageItem;
import com.example.administrator.iteam_fragment.util.PublicWay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.Constant.subContent;
import static com.example.administrator.iteam_fragment.Constant.taskid;
import static com.example.administrator.iteam_fragment.Constant.time;
import static com.example.administrator.iteam_fragment.LoginActivity.pref;

/**
 * Created by SML on 2017/4/15 0015.
 */

@SuppressWarnings("ALL")
public class TaskReceiveContent extends Activity implements AdapterView.OnItemClickListener {
    private TextView tv_taskName;
    private TextView tv_belongTeam;
    private TextView tv_taskDes;
    private TextView tv_taskExpectEndTime;
    private Button bt_submit;
    private ImageView iv_headBack;
    private ImageView iv_splitTask;
    private GridView gridView;
    private PopupMenu popupMenu;
    private EditText editSub;
    Menu menu;
    public static final int TAKE_PHOTO=6;
    public static final int CHOOSE_PHOTO=7;
    private ImageView picture;
    private Uri imageUri;
    public static Bitmap bimap ;
    private GridAdapter adapter;
    private static final int TAKE_PHOTO_REQUEST_CODE=1;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private View parentView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TaskItem> taskItems;

    //    private  String taskid;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if(!TextUtils.isEmpty(intent.getStringExtra("id")))
            taskid=intent.getStringExtra("id");
        Toast.makeText(this,taskid,Toast.LENGTH_LONG).show();



        Log.i("activity","taskReseiveContent create  "+taskid);





        parentView = getLayoutInflater().inflate(R.layout.task_receive_content, null);
        setContentView(parentView);



        pop = new PopupWindow(TaskReceiveContent.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);

        pop.setContentView(view);


        initview();
        if(subContent!=null){
            editSub.setText(subContent);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMain();
            }
        });
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(TaskReceiveContent.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(TaskReceiveContent.this,new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },1);
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent2 = new Intent(TaskReceiveContent.this, AlbumActivity.class);
                            subContent=editSub.getText().toString();
                            startActivity(intent2);
                        }
                    }).start();
                }
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        iv_headBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent=new Intent(TaskReceiveContent.this,MainActivity.class);
                        subContent=null;
                        startActivity(intent);
                    }
                }).start();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(),"Submit this task",Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = new ProgressDialog(TaskReceiveContent.this);
                ArrayList<File> files= new ArrayList<>();
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) getIntent().getSerializableExtra("files");
                if(images!=null)
                    for(ImageItem pic :images){
                        files.add(new File(pic.getImagePath()));
                    }
                String url="http://127.0.0.1/iteam/upload_files.php";
                String cookie=pref.getString("cookie",null);
                String sub = editSub.getText().toString();
                subContent=null;
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("taskId",taskid);
                    jsonObj.put("taskSub",sub);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.setTitle("正在提交");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                Log.d("oo",jsonObj.toString());
                try {
                    HttpUtil.getUtilsInstance().sendMultipart(url, cookie,jsonObj.toString(),  files, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("oooo","fail");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String respenseText=response.body().string();
                            Log.d("oooo",respenseText);
                            progressDialog.cancel();
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        iv_splitTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //考虑之后改成邀请伙伴加入自己接的任务，
                Toast.makeText(getBaseContext(),"拆分任务",Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(TaskReceiveContent.this, SplitTaskActivity.class);
                startActivity(intent);
            }
        });



        adapter = new GridAdapter(this);
        adapter.update();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        //添加消息处理
        //   gridview.setOnItemClickListener(new ItemClickListener());
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.add_pic);
        PublicWay.activityList.add(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==Bimp.tempSelectBitmap.size()){
            ll_popup.startAnimation(AnimationUtils.loadAnimation(TaskReceiveContent.this,R.anim.activity_translate_in));
            pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        }
        else{
            Intent intent = new Intent(TaskReceiveContent.this,
                    GalleryActivity.class);
            intent.putExtra("position", "1");
            intent.putExtra("ID", position);
            startActivity(intent);
        }
    }

    private void photo(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_REQUEST_CODE);
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Log.d("qqq",fileName);
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                    adapter.update();
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == 9){
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bimp.tempSelectBitmap.clear();
            Bimp.max = 0;
            Intent intent=new Intent(TaskReceiveContent.this,MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
        Bimp.max = 0;
    }



    private void initview() {

        tv_taskName=(TextView)findViewById(R.id.head_task_name_trc);
        tv_belongTeam=(TextView)findViewById(R.id.tv_belong_team_trc);
        tv_taskDes=(TextView)findViewById(R.id.tv_task_des_trc);
        tv_taskExpectEndTime=(TextView)parentView.findViewById(R.id.tv_tast_expectEndTime);
        bt_submit=(Button)findViewById(R.id.bt_submit);
        iv_headBack=(ImageView)findViewById(R.id.head_back);
        gridView = (GridView) findViewById(R.id.pic_gridView);
        iv_splitTask=(ImageView)findViewById(R.id.iv_splitTask);
        editSub = (EditText)findViewById(R.id.task_return_content);



    }

    private  void setcontent()
    {
        tv_taskName.setText(taskItems.get(0).taskName);
        tv_belongTeam.setText(taskItems.get(0).teamName);
        tv_taskDes.setText(taskItems.get(0).des);
        tv_taskExpectEndTime.setText(time.format(taskItems.get(0).last_time));


    }
    private void refreshMain() {

        Toast.makeText(this, "任务详情", Toast.LENGTH_SHORT).show();

        String url = "http://127.0.0.1/iteam/getTask.php";
        String cookie = pref.getString("cookie", null);
        Log.d( "refreshMain: ", cookie+"bc");
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("taskid",taskid);
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
//                Gson gson = new Gson();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                taskItems=gson.fromJson(responseText,
                        new TypeToken<ArrayList<TaskItem>>(){}.getType());
                Log.d("IN_getTask_taskItem", taskItems.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setcontent();
                    }
                });

            }
        });


        swipeRefreshLayout.setRefreshing(false);

    }
}

