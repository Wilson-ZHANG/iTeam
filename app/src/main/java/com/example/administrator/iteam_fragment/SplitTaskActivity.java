package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class SplitTaskActivity extends AppCompatActivity {

    private EditText split_headline;
    private EditText split_task_des;
    private EditText split_deadline;
    private LinearLayout search_person_button;
    private EditText search_text;
    private ListView person_list;
    private Button split_submit;

    private List<Map<String,Object>> listItems;

    private String[] personName=new String[]{"狗子","翠花"};
    private int[] personImage=new int[]{R.drawable.team_image3, R.drawable.group_image1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_task);

        split_headline = (EditText)findViewById(R.id.split_headline);
        split_task_des = (EditText)findViewById(R.id.split_task_des);
        split_deadline = (EditText)findViewById(R.id.split_deadline);
        search_person_button = (LinearLayout)findViewById(R.id.search_person_button);
        search_text = (EditText)findViewById(R.id.search_text);
        person_list = (ListView)findViewById(R.id.person_list);
        split_submit = (Button)findViewById(R.id.split_submit);

        setlistItem();
        setListAdapter();

        //点击拆分按钮进行拆分,把数据提交到数据库
        split_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"拆分成功啦，你很棒棒哦",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SplitTaskActivity.this, TaskReceiveContent.class);
                startActivity(intent);
            }
        });
    }



    private void setlistItem(){
        if(listItems==null)
            listItems=new ArrayList<Map<String, Object>>();
        listItems.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<personName.length;i++) {
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    listItem.put("personImage", personImage[i]);
                    listItem.put("personName", personName[i]);
                    listItems.add(listItem);
                }
            }
        }).start();

    }

    private void setListAdapter(){
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems, R.layout.person_layout,
                new String[]{"personImage","personName"},
                new int[]{R.id.person_image, R.id.person_name});

        person_list.setAdapter(simpleAdapter);
    }
}
