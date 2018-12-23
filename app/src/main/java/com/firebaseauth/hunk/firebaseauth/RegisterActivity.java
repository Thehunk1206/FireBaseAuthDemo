package com.firebaseauth.hunk.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText mNameET,mEmailET,mPasswordET,mConfirmPasswordET;
    private Button mCreateAccBtn;
    private ProgressDialog mRegister_progress;
    //custom_error_toast_text
    private TextView error_text;
    private Toast error_toast;

    private LinearLayout mRegister_layout;
    private Animation ShakeAnimation;

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
        mRegister_layout = findViewById(R.id.register_layout);


       // mCreateAccBtn.setEnabled(false);

        mRegister_progress = new ProgressDialog(this);

        //inflating custom toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.custom_error_toast));
        error_text = layout.findViewById(R.id.error_text);
        error_toast = new Toast(getApplicationContext());
        error_toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        error_toast.setDuration(Toast.LENGTH_SHORT);
        error_toast.setView(layout);





        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mNameET.getText().toString().trim();
                email = mEmailET.getText().toString().trim();
                password = mPasswordET.getText().toString().trim();
                confirm_password = mConfirmPasswordET.getText().toString().trim();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
                    error_text.setText("All fields are required!!!");
                    error_toast.show();
                    animateLayout();



                }else if(name.length() < 3){
                    error_text.setText("Name must contain 3 or more character");
                    error_toast.show();
                    animateLayout();

                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    error_text.setText("Enter a valid Email address");
                    error_toast.show();
                    animateLayout();



                }else if(password.length() < 6){
                    error_text.setText("Password must contain 6 or more character");
                    error_toast.show();
                    animateLayout();

                }else if(!password.equals(confirm_password) || !confirm_password.equals(password)){
                    error_text.setText("Please enter correct password in both fields");
                    error_toast.show();
                    mConfirmPasswordET.setFocusable(true);
                    animateLayout();

                }else{
                    mRegister_progress.setTitle("Creating account on"+ R.string.app_name);
                    mRegister_progress.setMessage("Please Wait while we create your account");
                    mRegister_progress.setCanceledOnTouchOutside(false);
                    mRegister_progress.show();

                    register_user();

                }


            }
        });




    }

    private void animateLayout() {

        ShakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);

        ShakeAnimation.setInterpolator(getApplicationContext(), android.R.anim.cycle_interpolator);
        ShakeAnimation.setRepeatCount(3);
        mRegister_layout.setAnimation(ShakeAnimation);
    }


    private void register_user() {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

                            }else{
                                mRegister_progress.hide();
                                //TODO remove toast message
                                error_text.setText("Something went wrong, try again");

                            }
                        }
                    });


                }



            }
        });

    }



}
