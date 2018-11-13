package com.developerdesk.xmppchat.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.developerdesk.xmppchat.Interface.ConnectionCallback;
import com.developerdesk.xmppchat.R;
import com.developerdesk.xmppchat.service.RoosterConnectionService;



public class LoginActivity extends AppCompatActivity implements ConnectionCallback {


    @Override
    public void connectedSuccessfully() {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }

    @Override
    public void connectionError() {
        Log.e("ss","ss");
    }



    private Button signupButton;
    private Button signinButton;
    private RoosterConnectionService roosterConnectionService;
    private boolean isBound = true;
    private  Intent serviceIntent;
    private EditText emailEditText;
    private EditText passwordEditText;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            RoosterConnectionService.LocalBinder binder = (RoosterConnectionService.LocalBinder) service;
            roosterConnectionService = binder.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };



    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signupButton = findViewById(R.id.email_signup_button);
        signinButton = findViewById(R.id.email_sign_in_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                roosterConnectionService.connect(emailEditText.getText().toString(),passwordEditText.getText().toString(),LoginActivity.this);

                //startActivity(new Intent(LoginActivity.this,UserListActivity.class));
            }
        });
        serviceIntent = new Intent(LoginActivity.this,RoosterConnectionService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }


}
