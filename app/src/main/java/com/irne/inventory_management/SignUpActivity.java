package com.irne.inventory_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout container;
    AnimationDrawable anim;
    private EditText signUpName,signUpEmail,signUpPassword,signUpId;
    private Button signUp;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private LottieAnimationView animationView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        container = (ConstraintLayout) findViewById(R.id.constraint_signup);

        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);

        signUpName = findViewById(R.id.signUpName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpId = findViewById(R.id.signUpId);

        signUp = (Button) findViewById(R.id.signUp);
        animationView = (LottieAnimationView) findViewById(R.id.animationview);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        user = new User();

        signUp.setOnClickListener(SignUpActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }

    private void registerUser(){
        final String email = signUpEmail.getText().toString().trim();
        final String password = signUpPassword.getText().toString().trim();

        final String name = signUpName.getText().toString().trim();
        final String Id = signUpId.getText().toString().trim();

        if (TextUtils.isEmpty(signUpEmail.getText().toString())){

            signUpEmail.setError("Email is required");
            signUpEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(signUpName.getText().toString())){

            signUpName.setError("Name is required");
            signUpName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(signUpPassword.getText().toString())){

            signUpPassword.setError("Password is required");
            signUpPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(signUpId.getText().toString())){

            signUpId.setError("Id is required");
            signUpId.requestFocus();
            return;
        }

        if (signUpPassword.length() < 6){

            signUpPassword.setError("Passsword must be greater than or equal to 6 characters");
            signUpPassword.requestFocus();
            return;
        }

        signUp.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    user.setSignup_id(Id);
                    user.setName(name);
                    user.setEmail(email);


                    firebaseDatabase.getReference("Users").child(task.getResult().getUser().getUid()).push().setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            animationView.setVisibility(View.GONE);
                            signUp.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()){

                                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                                finish();

                            }
                            else{
                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    animationView.setVisibility(View.GONE);
                    signUp.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.signUp:
                registerUser();
                break;
        }
    }

}
