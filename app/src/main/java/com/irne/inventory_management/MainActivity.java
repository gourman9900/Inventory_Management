package com.irne.inventory_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout container;
    AnimationDrawable anim;
    private Button button,signIn;
    private LottieAnimationView lottieAnimationView;
    private FirebaseAuth mAuth;
    LottieAnimationView animationView;
    EditText signInEmail,signInPassword;
    String email,password;
    String TAG = "Sign In Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (ConstraintLayout) findViewById(R.id.constraint_container);

        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);

        animationView = (LottieAnimationView) findViewById(R.id.animation3);

        button = (Button) findViewById(R.id.button2);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation2);
        mAuth = FirebaseAuth.getInstance();
        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        signIn = (Button) findViewById(R.id.signIn);
        button.setOnClickListener(MainActivity.this);
        signIn.setOnClickListener(MainActivity.this);

        File root = new File(Environment.getExternalStorageState(),"Barcode");





        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        //View view =getSupportActionBar().getCustomView();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            finish();
        }
    }

    public void signInUser(){

        animationView.setVisibility(View.VISIBLE);


        email = signInEmail.getText().toString();
        password = signInPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed due to" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

        animationView.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signIn :
                signInUser();
                break;
            case R.id.button2:
                button.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                lottieAnimationView.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                break;

        }
    }
}
