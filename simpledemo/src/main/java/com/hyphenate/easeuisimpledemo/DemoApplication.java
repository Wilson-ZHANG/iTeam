package com.hyphenate.easeuisimpledemo;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;


public class DemoApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().chatManager().loadAllConversations();
    }
    
}
