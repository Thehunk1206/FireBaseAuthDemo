package com.firebaseauth.hunk.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText email_TV, password_TV;
    private Button login_Btn;
    private ProgressDialog loginProgress;
    private LinearLayout LoginLayout;

    private FirebaseUser mCurrent_User;
    private FirebaseAuth mAuth;


    //animation
    private Animation ShakeAnimations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Android Fields
        email_TV = findViewById(R.id.login_email_ET);
        password_TV = findViewById(R.id.login_password_ET);
        login_Btn = findViewById(R.id.login_btn);
        loginProgress = new ProgressDialog(this);
        LoginLayout = findViewById(R.id.login_layout);

        //Loading animation
        ShakeAnimations = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);



        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();



        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email_TV.getText().toString().trim();
                String Password = password_TV.getText().toString().trim();

                if(Email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email_TV.setError("Enter a valid Email Address");
                    LoginLayout.setAnimation(ShakeAnimations);

                }

                loginProgress.setTitle("Logging in to App");
                loginProgress.setMessage("PLease wait a moment");
                loginProgress.setCanceledOnTouchOutside(false);
                loginProgress.show();

                LoginUser(Email,Password);



            }
        });



    }


    private void LoginUser(String Email, String Password) {

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgress.dismiss();
                    Intent Homepage = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(Homepage);
                    finish();


                }else{
                    loginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


}
