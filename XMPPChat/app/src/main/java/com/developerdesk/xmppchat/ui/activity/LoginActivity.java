package com.developerdesk.xmppchat.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developerdesk.xmppchat.R;
import com.developerdesk.xmppchat.service.RoosterConnectionService;

public class LoginActivity extends AppCompatActivity {


    private Button signupButton;
    private Button signinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupButton = findViewById(R.id.email_signup_button);
        signinButton = findViewById(R.id.email_sign_in_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(LoginActivity.this,UserListActivity.class));
            }
        });

        startService(new Intent(LoginActivity.this,RoosterConnectionService.class));
    }
}
