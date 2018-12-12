package com.firebaseauth.hunk.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;

    private Button VerifyEmailBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        TextView EmailIdTV = findViewById(R.id.email_id_TV);
        EmailIdTV.setText(mCurrentUser.getEmail());

        VerifyEmailBtn = findViewById(R.id.email_verify_btn);

        VerifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyEmailBtn.setEnabled(false);
                SendEmailVerificationMail();

            }
        });

        if(mCurrentUser.isEmailVerified()){

            Intent HomePage = new Intent(EmailVerificationActivity.this,HomeActivity.class);
            startActivity(HomePage);
            finish();
        }


    }

    private void SendEmailVerificationMail(){
        if (mCurrentUser != null) {

            mCurrentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(EmailVerificationActivity.this, "Verification Email sent to" + mCurrentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    }else{

                        VerifyEmailBtn.setEnabled(true);
                        Toast.makeText(EmailVerificationActivity.this, "Failed to sent Verification Email, try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

    }
}
