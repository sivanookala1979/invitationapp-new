package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.AllChatDetailsAdapter;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.ChatInfo;
import com.example.dataobjects.ChatRoom;

import java.util.ArrayList;
import java.util.List;


public class AllChatsActivity extends BaseActivity implements OnItemClickListener,View.OnClickListener {
    ListView allChatsListView;
    AllChatDetailsAdapter chatsAdapter;
    List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
    FontTypes fontTypes;
    LinearLayout customer_suport;
    TextView customer_suport_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        addToolbarView();

        fontTypes = new FontTypes(getApplicationContext());
        allChatsListView = (ListView) findViewById(R.id.all_chat_rooms);
        allChatsListView.setOnItemClickListener(this);
        chatsAdapter = new AllChatDetailsAdapter(getApplicationContext(), R.layout.all_chat_details_row, chatRooms);
        allChatsListView.setAdapter(chatsAdapter);

         customer_suport= (LinearLayout) findViewById(R.id.customer_suport);
         customer_suport_text= (TextView) findViewById(R.id.customer_suport_text);
        customer_suport.setOnClickListener(this);
        customer_suport_text.setOnClickListener(this);

        chatsAdapter.clear();
        loadAllChats();

        setFontType(R.id.customer_suport_text);
    }

    private void loadAllChats() {
        new InvtAppAsyncTask(AllChatsActivity.this) {
            List<String> chatRoomNames = new ArrayList<String>();

            @Override
            public void process() {
//                IntraChatSyncher intraChatSyncher = new IntraChatSyncher();
//                chatRooms = new ArrayList<ChatRoom>();
//                chatRooms = intraChatSyncher.getChats();
//                for (ChatRoom chatRoom : chatRooms) {
//                    chatRoomNames.add(chatRoom.getOtherUserName() + " - " + chatRoom.getOtherUserId());
//                }
            }

            @Override
            public void afterPostExecute() {
                chatsAdapter.clear();
                chatsAdapter.addAll(chatRooms);
                chatsAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
        if (chatRooms != null && position < chatRooms.size()) {
            Intent intent = new Intent(getApplicationContext(), IntraChatActivity.class);
            intent.putExtra("ChatRoomId", chatRooms.get(position).getChatRoomId());
            intent.putExtra("UserName", chatRooms.get(position).getOtherUserName());
            intent.putExtra("UserImage", chatRooms.get(position).getImage());
            intent.putExtra("UserId", chatRooms.get(position).getOtherUserId());
            ChatInfo chatInfo=new ChatInfo();
            chatInfo.setImageUrl(chatRooms.get(position).getImage());
            chatInfo.setName(chatRooms.get(position).getOtherUserName());
            InvtAppPreferences.setChatInfo(chatInfo);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        loadAllChats();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customer_suport:
            case R.id.customer_suport_text:
                Intent intent = new Intent(getApplicationContext(), SupportChatActivity.class);
                 intent.putExtra("UserImage", "");
                 intent.putExtra("UserName", "");
                ChatInfo chatInfo=new ChatInfo();
                chatInfo.setImageUrl("");
                InvtAppPreferences.setChatInfo(chatInfo);
                startActivity(intent);
            break;

        }
    }
}
