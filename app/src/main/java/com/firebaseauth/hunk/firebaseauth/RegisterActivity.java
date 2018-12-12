package com.firebaseauth.hunk.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.StringTokenizer;

public class RegisterActivity extends AppCompatActivity {
    private EditText mNameET,mEmailET,mPasswordET,mConfirmPasswordET;
    private Button mCreateAccBtn;
    private ProgressDialog mRegister_progress;

    private String name,email,password,confirm_password;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();



        mNameET = findViewById(R.id.name_ET);
        mEmailET = findViewById(R.id.mail_ET);
        mPasswordET = findViewById(R.id.password_ET);
        mConfirmPasswordET = findViewById(R.id.confirm_password_ET);
        mCreateAccBtn = findViewById(R.id.register_acc_btn);
       // mCreateAccBtn.setEnabled(false);

        mRegister_progress = new ProgressDialog(this);





        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mNameET.getText().toString().trim();
                email = mEmailET.getText().toString().trim();
                password = mPasswordET.getText().toString().trim();
                confirm_password = mConfirmPasswordET.getText().toString().trim();

                if(name.isEmpty()){
                    mNameET.setError("PLease Enter your Name");
                    mNameET.setFocusable(true);
                }
                if(email.isEmpty()){
                    mEmailET.setError("Please Enter Your Email ID");
                    mEmailET.setFocusable(true);
                }
                if(password.isEmpty()){
                    mPasswordET.setError("Please Enter Your Password");
                    mPasswordET.setFocusable(true);

                }

                if(confirm_password.isEmpty()){
                    mConfirmPasswordET.setError("Please Confirm Your Password");
                    mConfirmPasswordET.setFocusable(true);

                }

                if(name.length() < 3 ){
                    mNameET.setError("Name Should has more the 3 characters");
                    mNameET.setFocusable(true);
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailET.setError("Please Enter Valid Email address");
                    mEmailET.setFocusable(true);
                }
                if(password.length() < 6){
                    mPasswordET.setError("Password must contain 6 or more than 6 character");
                    mPasswordET.setFocusable(true);

                }

                if(!password.equals(confirm_password)){
                    mConfirmPasswordET.setError("Password did not matched");
                    mConfirmPasswordET.setFocusable(true);
                }
                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(confirm_password)){
                    mRegister_progress.setTitle("Registering Account");
                    mRegister_progress.setMessage("Creating Account ....");
                    mRegister_progress.setCanceledOnTouchOutside(false);
                    mRegister_progress.show();

                    register_user();
                }



            }
        });




    }

    private void register_user() {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (mCurrentUser != null) {
                        String CurrentUserId = mCurrentUser.getUid();

                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(CurrentUserId);
                    }




                    HashMap<String,String> UserMap = new HashMap<>();
                    UserMap.put("name", name);
                    UserMap.put("email", email);
                    UserMap.put("profile_image","default");
                    UserMap.put("profile_thumb_image","default");

                    mUserDatabase.setValue(UserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRegister_progress.dismiss();
                                Intent EmailVerificationPage = new Intent(RegisterActivity.this,EmailVerificationActivity.class);
                                EmailVerificationPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(EmailVerificationPage);

                            }
                        }
                    });


                }



            }
        });

    }
}
