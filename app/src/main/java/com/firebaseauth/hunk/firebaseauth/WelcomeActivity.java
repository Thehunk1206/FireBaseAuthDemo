package com.firebaseauth.hunk.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button mCreateAccBtn = findViewById(R.id.create_account_btn);
        Button mSignInBtn = findViewById(R.id.sign_in_btn);

        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterPage = new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(RegisterPage);


            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginPage = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(LoginPage);

            }
        });


    }
}
