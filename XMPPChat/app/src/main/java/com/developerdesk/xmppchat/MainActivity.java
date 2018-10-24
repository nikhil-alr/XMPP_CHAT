package com.developerdesk.xmppchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.developerdesk.xmppchat.adapter.ChatAdapter;
import com.developerdesk.xmppchat.datamodel.ChatDataModel;
import com.developerdesk.xmppchat.service.RoosterConnectionService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private RecyclerView recyclerView;
   private EditText editText;
   private Button sendButton;
   List<ChatDataModel> chatData;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recylerView);
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.sendButton);


        chatData = new ArrayList();


        chatAdapter = new ChatAdapter(chatData);
        recyclerView.setAdapter(chatAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("sendmessage-event");
                // You can also include some extra data.
                intent.putExtra("message", editText.getText().toString());
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                chatData.add(new ChatDataModel(editText.getText().toString(),true));
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatData.size()-1);
                editText.setText("");



            }
        });

        startService(new Intent(this,RoosterConnectionService.class));
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            chatData.add(new ChatDataModel(message,false));
            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatData.size()-1);

        }
    };
}
