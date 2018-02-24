package com.example.administrator.iteam_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class MessageFragment extends EaseConversationListFragment {
    private MainActivity mActivity;
    private View view;
    private String[] mesNames=new String[]{"系统","SML",};
    private String[] meskDes=new String[]{"任务一即将到期","可以了吗？"};
    private int[] mesimageIds=new int[]{R.drawable.mail,R.drawable.chat};
    private int[] openimageIds=new int[]{R.drawable.open,R.drawable.open};

    private List<Map<String,Object>> listItems;
    private ListView list;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        this.setConversationListItemClickListener(new EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                if(conversation.isGroup()){
                    if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                        // it's group chat
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                    }else{
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                    }
                }
                Log.d("conversationId",conversation.conversationId());
                startActivity(intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        return inflater.inflate(R.layout.conversation, container, false);
    }


}
