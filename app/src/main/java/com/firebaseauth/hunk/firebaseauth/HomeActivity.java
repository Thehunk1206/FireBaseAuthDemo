package com.firebaseauth.hunk.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mCurrent_user = mAuth.getCurrentUser();


        if(mCurrent_user == null){
            Intent WelcomePage = new Intent(HomeActivity.this,WelcomeActivity.class);
            startActivity(WelcomePage);
            finish();
        }

        else if(!mCurrent_user.isEmailVerified()){

            Intent EmailVerificationPage = new Intent(HomeActivity.this,EmailVerificationActivity.class);
            startActivity(EmailVerificationPage);
            finish();
        }
    }


}
