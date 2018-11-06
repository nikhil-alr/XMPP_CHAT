package com.developerdesk.xmppchat.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.developerdesk.xmppchat.Interface.UserCallback;
import com.developerdesk.xmppchat.R;
import com.developerdesk.xmppchat.adapter.UserAdapter;
import com.developerdesk.xmppchat.datamodel.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserCallback {

    private RecyclerView recyclerView;
    List<UserModel> userDataModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recylerView);

        prepareData();

        UserAdapter userAdapter = new UserAdapter(userDataModel,this);
        recyclerView.setAdapter(userAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    private void prepareData()
    {
        userDataModel.add(new UserModel("ABC","1"));
        userDataModel.add(new UserModel("ABCd","1"));
        userDataModel.add(new UserModel("ABCe","1"));
        userDataModel.add(new UserModel("ABCf","1"));
    }

    @Override
    public void onUserSelect(int position) {

        startActivity(new Intent(this,ChatActivity.class));
    }
}
