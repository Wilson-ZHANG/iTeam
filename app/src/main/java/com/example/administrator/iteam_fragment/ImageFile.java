package com.example.administrator.iteam_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.administrator.iteam_fragment.adapter.FolderAdapter;
import com.example.administrator.iteam_fragment.util.Bimp;
import com.example.administrator.iteam_fragment.util.PublicWay;


@SuppressWarnings("ALL")
public class ImageFile extends Activity {
    private FolderAdapter folderAdapter;
    private Button bt_cancel;
    private Context mContext;
    public static ImageFile instance = null;//为了在适配器中能够关闭该活动

    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_camera_image_file);
        PublicWay.activityList.add(this);
        mContext = this;
        bt_cancel = (Button) findViewById(R.id.cancel);
        bt_cancel.setOnClickListener(new CancelListener());
        GridView gridView = (GridView) findViewById(R.id.fileGridView);
        TextView textView = (TextView) findViewById(R.id.headerTitle);
        textView.setText(R.string.photo);
        folderAdapter = new FolderAdapter(this);
        gridView.setAdapter(folderAdapter);
    }

    private class CancelListener implements View.OnClickListener {// 取消按钮的监听
        public void onClick(View v) {
            //清空选择的图片
            Bimp.tempSelectBitmap.clear();
//            Intent intent = new Intent();
//            intent.setClass(mContext, TaskReceiveContent.class);
//            startActivity(intent);
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Intent intent = new Intent();
        intent.setClass(mContext, TaskReceiveContent.class);
        startActivity(intent);
        this.finish();

        return true;
    }

}
