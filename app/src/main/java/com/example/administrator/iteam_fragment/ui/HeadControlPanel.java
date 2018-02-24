package com.example.administrator.iteam_fragment.ui;

/**
 * Created by cp on 17-4-13.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.iteam_fragment.CreateTeam;
import com.example.administrator.iteam_fragment.R;

import static com.example.administrator.iteam_fragment.LoginActivity.pref;


@SuppressWarnings("ALL")
public class HeadControlPanel extends RelativeLayout {

    private ImageView headImage;

    private Context mContext;
    private TextView mMiddleTitle;
   private ImageView addImage;
    private static final float middle_title_size = 20f;

    private static final int default_background_color = Color.rgb(0,0,0);

    public HeadControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mMiddleTitle = (TextView) findViewById(R.id.middle_title);
        //mRightTitle = (TextView)findViewById(R.id.right_title);
        headImage = (ImageView) findViewById(R.id.head_image);
        addImage = (ImageView) findViewById(R.id.add_team);
        //headMessage = (ImageView) findViewById(R.id.head_message);
        setBackgroundColor(Color.parseColor("#01B8BB"));

        addImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), CreateTeam.class);
                getContext().startActivity(it);
            }
        });
    }

    public void initHeadPanel(){

        String postUrl = "http://127.0.0.1/iteam/getUserPic.php";
        Log.d("cookie2",pref.getString("cookie", null));

        Glide
                .with(getContext())
                .load(pref.getString("userHeadPic",null))
                .asBitmap()
                .into(headImage);


//        setHeadMessage(R.drawable.right_message);
//        setmRightTitle("消息");
        if(mMiddleTitle != null){
            setMiddleTitle("任务");
        }
    }
    public void setMiddleTitle(String s){
        mMiddleTitle.setText(s);
        mMiddleTitle.setTextSize(middle_title_size);
    }

    public String getMiddleTitle()
    {
        return mMiddleTitle.getText().toString();
    }

//    public void setmRightTitle(String s){
//        mRightTitle.setText(s);
//        mRightTitle.setTextSize(right_title_size);
//    }

    public void setHeadImage(int a){
        headImage.setImageResource(a);
    }
   // public void setHeadMessage(int a){ headMessage.setImageResource(a);}

    public void setAddDisable() {
        addImage.setVisibility(GONE);
    }
    public void setAddAble(){
        addImage.setVisibility(VISIBLE);
    }
}
