package com.developerdesk.xmppchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.developerdesk.xmppchat.adapter.ChatAdapter;
import com.developerdesk.xmppchat.datamodel.ChatDataModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private RecyclerView recyclerView;
   private EditText editText;
   private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recylerView);
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.sendButton);


        final List<ChatDataModel> chatData = new ArrayList();


        final ChatAdapter chatAdapter = new ChatAdapter(chatData);
        recyclerView.setAdapter(chatAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatData.add(new ChatDataModel(editText.getText().toString(),true));
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatData.size()-1);
                editText.setText("");

            }
        });
    }
}
