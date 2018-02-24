package com.example.administrator.iteam_fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.adapter.FragmentAdapter;
import com.example.administrator.iteam_fragment.change_head.IdDBHelper;
import com.example.administrator.iteam_fragment.change_head.MPoPuWindow;
import com.example.administrator.iteam_fragment.nav.FriendActivity;
import com.example.administrator.iteam_fragment.nav.MyTaskActivity;
import com.example.administrator.iteam_fragment.ui.CircleImageView;
import com.example.administrator.iteam_fragment.ui.HeadControlPanel;
import com.example.administrator.iteam_fragment.util.PublicWay;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;

import static com.example.administrator.iteam_fragment.LoginActivity.editor;
import static com.example.administrator.iteam_fragment.LoginActivity.pref;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public enum Type {   //为切换头像加入的类型
        PHONE, CAMERA
    }

    private PopupWindow mPopupWindow;
    private ImageView mIvThumb;
    private File file;

    private Uri ImgUri;

    private Type type;

    private MPoPuWindow puWindow;

    CircleImageView photos;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;
    String photo_id;

    private ImageView bt_task;
    private ImageView bt_team;
    private ImageView bt_message;
    private ImageView bt_group;

    private ImageView taskImage;
    private ImageView teamImage;
    private ImageView messageImage;
    private ImageView groupImage;

    private TextView taskText;
    private TextView teamText;
    private TextView messageText;
    private TextView groupText;
    HeadControlPanel headPanel = null;
    CircleImageView headImage;
    //滑动屏
    private ViewPager vp_content;
    private FragmentAdapter mfragmentAdapter;
    private List<Fragment> fragmentList;

    private DrawerLayout mDrawerLayout;

    private NavigationView navigationView;
    private static Context context;

    public Context getContext() {
        return context;
    }

    private void initViews() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header);

        headImage = (CircleImageView) view.findViewById(R.id.icon_image);
        Glide
                .with(getContext())
                .load(pref.getString("userHeadPic", null))
                .asBitmap()
                .into(headImage);
        Log.d("abc", pref.getString("userHeadPic", null)); //设置侧滑栏的头像

        headImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                puWindow = new MPoPuWindow(MainActivity.this, MainActivity.this);
                puWindow.showPopupWindow(findViewById(R.id.LinerLayout_main));
                puWindow.setOnGetTypeClckListener(new MPoPuWindow.onGetTypeClckListener() {

                    @Override
                    public void getType(MainActivity.Type type) {
                        MainActivity.this.type = type;
                    }

                    @Override
                    public void getImgUri(Uri ImgUri, File file) {
                        MainActivity.this.ImgUri = ImgUri;
                        MainActivity.this.file = file;
                    }

                });

            }
        });




        TextView username = (TextView) view.findViewById(R.id.username);
        username.setText(pref.getString("userName",null));//设置侧滑栏用户名

        bt_task= (ImageView) findViewById(R.id.task_image);
        bt_group=(ImageView ) findViewById(R.id.group_image);
        bt_message=(ImageView) findViewById(R.id.message_image);
        bt_team=(ImageView) findViewById(R.id.team_image);

        bt_group.setOnClickListener(this);
        bt_task.setOnClickListener(this);
        bt_message.setOnClickListener(this);
        bt_team.setOnClickListener(this);

        taskImage = (ImageView) findViewById(R.id.task_image);
        teamImage = (ImageView) findViewById(R.id.team_image);
        messageImage = (ImageView) findViewById(R.id.message_image);
        groupImage = (ImageView) findViewById(R.id.group_image);

        taskText = (TextView) findViewById(R.id.task_text);
        teamText = (TextView) findViewById(R.id.team_text);
        messageText = (TextView) findViewById(R.id.message_text);
        groupText = (TextView) findViewById(R.id.group_text);


        headPanel = (HeadControlPanel)findViewById(R.id.head_layout);
        if(headPanel != null){
            headPanel.initHeadPanel();

        }

        vp_content=(ViewPager)findViewById(R.id.content_viewPager);
        fragmentList = new ArrayList<Fragment>();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        initViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        //navView.setItemIconTintList(null);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_call:
                        replaceFragment(new TaskFragment());
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_friends:
                        Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this,"nav_location",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, MyTaskActivity.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_mail:

                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_location:
                        Log.d("aaa", "onNavigationItemSelected: touch");

                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        mDrawerLayout.closeDrawers();
                        EMClient.getInstance().logout(false, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                finish();
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String error) {

                            }
                        });
                        Toast.makeText(MainActivity.this,"退出登录",Toast.LENGTH_LONG).show();
                        return true;


                }
                return true;
            }
        });



        fragmentList.add(new TaskFragment());
        fragmentList.add(new TeamFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new GroupFragment());
        mfragmentAdapter=new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp_content.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp_content.setAdapter(mfragmentAdapter);

        vp_content.setCurrentItem(0,true);//初始设置ViewPager选中第一帧
        clearSelection();
        changeColor(0);//初始化将task图标上色

        //ViewPager的监听事件
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
               /*此方法在页面被选中时调用*/
                clearSelection();
                changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
               arg0 ==1的时辰默示正在滑动，
               arg0==2的时辰默示滑动完毕了，
               arg0==0的时辰默示什么都没做。*/
            }
        });

        PublicWay.activityList.add(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return  true;
    }

    private final Handler handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case R.id.task_image:
                    // 当点击了消息tab时，选中第1个tab
                    vp_content.setCurrentItem(0,true);
                    break;
                case R.id.team_image:
                    // 当点击了联系人tab时，选中第2个tab
                    vp_content.setCurrentItem(1,true);
                    break;
                case R.id.message_image:
                    // 当点击了动态tab时，选中第3个tab
                    vp_content.setCurrentItem(2,true);
//                maddPopWindow.showAtLocation(MainActivity.this.findViewById(R.id.content),
//                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                case R.id.group_image:
                    // 当点击了设置tab时，选中第4个tab
                    vp_content.setCurrentItem(3,true);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.task_image:
                // 当点击了消息tab时，选中第1个tab
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = R.id.task_image;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.team_image:
                // 当点击了联系人tab时，选中第2个tab
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = R.id.team_image;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.message_image:
                // 当点击了动态tab时，选中第3个tab
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = R.id.message_image;
                        handler.sendMessage(message);
                    }
                }).start();
//                maddPopWindow.showAtLocation(MainActivity.this.findViewById(R.id.content),
//                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.group_image:
                // 当点击了设置tab时，选中第4个tab
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = R.id.group_image;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            default:
                break;
        }
    }



    private void clearSelection() {
        taskImage.setImageResource(R.drawable.task_unselected);
        taskText.setTextColor(Color.parseColor("#707070"));

        teamImage.setImageResource(R.drawable.team_unselected);
        teamText.setTextColor(Color.parseColor("#707070"));

        messageImage.setImageResource(R.drawable.message_unselected);
        messageText.setTextColor(Color.parseColor("#707070"));

        groupImage.setImageResource(R.drawable.group_unselected);
        groupText.setTextColor(Color.parseColor("#707070"));
    }
    private void changeColor(int position){
        switch(position){
            case 0:
                headPanel.setAddDisable();
                headPanel.setMiddleTitle("任务");
                taskImage.setImageResource(R.drawable.task_selected);
                taskText.setTextColor(getResources().getColor(R.color.colorChosen));
                break;
            case 1:
                headPanel.setAddAble();
                headPanel.setMiddleTitle("团队");
                teamImage.setImageResource(R.drawable.team_selected);
                teamText.setTextColor(getResources().getColor(R.color.colorChosen));
                break;
            case 2:
                headPanel.setAddDisable();
                headPanel.setMiddleTitle("消息");
                messageImage.setImageResource(R.drawable.message_selected);
                messageText.setTextColor(getResources().getColor(R.color.colorChosen));
                break;
            case 3:
                headPanel.setAddDisable();
                headPanel.setMiddleTitle("工作组");
                groupImage.setImageResource(R.drawable.group_selected);
                groupText.setTextColor(getResources().getColor(R.color.colorChosen));
                break;
            default:
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", type + "");
        if (requestCode == 1) {
            if (ImgUri != null) {
                Glide
                        .with(getContext())
                        .load(ImgUri)
                        .asBitmap()
                        .placeholder(R.drawable.ic_launcher)
                        .into(headImage);
            }
        } else if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d("aaa",uri.getPath());
                Glide
                        .with(getContext())
                        .load(uri)
                        .asBitmap()
                        .placeholder(R.drawable.ic_launcher)
                        .into(headImage);
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor actualimagecursor = this.managedQuery(uri,proj,null,null,null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();

                String img_path = actualimagecursor.getString(actual_image_column_index);
                Log.d("iaska",img_path);
                File file = new File(img_path);
                String path="http://127.0.0.1/iteam/upload_headpic.php";
                String cookie=pref.getString("cookie",null);
                ArrayList<File> files= new ArrayList<>();
                files.add(file);
                try {
                    HttpUtil.getUtilsInstance().sendMultipart(path, cookie, "", files, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("upload fail",e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String text=response.body().string();
                            Log.d("upload suc",text);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 3) {
            if (type == MainActivity.Type.PHONE) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    if (bitmap != null) {
                        headImage.setImageBitmap(bitmap); // java.lang.NullPointerException

                        byte[] bit = img(bitmap);
                        dbHelper = new IdDBHelper(this);
                        db = dbHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("img",bit);
                        db.update("ID_PASSWORD",contentValues,"id = ?",new String[]{photo_id});
                        db.close();
                    }
                }
            } else if (type == MainActivity.Type.CAMERA) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                headImage.setImageBitmap(bitmap);

                byte[] bit = img(bitmap);
                dbHelper = new IdDBHelper(this);
                db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("img",bit);
                db.update("ID_PASSWORD",contentValues,"id = ?",new String[]{photo_id});
                db.close();
            }
        }
    }

    public byte[] img(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
