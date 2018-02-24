package com.hyphenate.easeuisimpledemo.ui;

/**
 * Created by jarvis on 01/11/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConvFrag extends EaseConversationListFragment {

    private List<Map<String,Object>> listItems;
    private ListView list;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private Map<String, EaseUser> getContacts() throws HyphenateException {
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        Log.d("friends",usernames.toString());
        contacts.put("Jarvis", new EaseUser("Jarvis"));
        contacts.put("13515664030", new EaseUser("13515664030"));
//        for(String name : usernames){
//            EaseUser user = new EaseUser(name);
//            contacts.put(name, user);
//            Log.d("friends",name);
//        }
        return contacts;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setConversationListItemClickListener(new EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
//        view=inflater.inflate(R.layout.ease_fragment_contact_list,container,false);
//        try {
//            this.setContactsMap(getContacts());
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
//        this.setContactListItemClickListener(new EaseContactListItemClickListener() {
//
//            @Override
//            public void onListItemClicked(EaseUser user) {
//                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//            }
//        });
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_message);
////        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshMain();
//            }
//        });

        return inflater.inflate(com.hyphenate.easeui.R.layout.ease_fragment_conversation_list, container, false);
    }


//    private void refreshMain()
//    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(1000);
//                } catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//                mActivity.runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                try {
//                                                    setContactsMap(getContacts());
//                                                } catch (HyphenateException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                Toast.makeText(mActivity,"这是消息",Toast.LENGTH_SHORT).show();
//                                                swipeRefreshLayout.setRefreshing(false);
//                                            }
//                                        }
//                );
//            }
//        }).start();
//    }
}
