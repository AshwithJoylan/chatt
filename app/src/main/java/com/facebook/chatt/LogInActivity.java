package com.facebook.chatt;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.chatt.Home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private Context context = LogInActivity.this;

    private String email, password;
    EditText mPassword;
    private ImageView logo;


    //Firebase
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logo = findViewById(R.id.logo);

        mPassword = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        setUpAuthStateListener();
        settupUI();
    }

    private void setUpAuthStateListener() {

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mUser = mAuth.getCurrentUser();
                if (mUser != null) {
                    Log.d(TAG, "onAuthStateChanged:starting Home activity ");
                    if(Build.VERSION.SDK_INT >= 21) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogInActivity.this, logo, "imageTransition");
                        startActivity(new Intent(context, HomeActivity.class), options.toBundle());
                        finish();
                    }
                }
            }
        };
    }

    //Log.d(TAG, "");
    //SettingUp ui
    private void settupUI() {

        TextView toPasswordReset = findViewById(R.id.forgot_password_tv);
        TextView toRegister = findViewById(R.id.to_register_tv);
        Button logInBtn = findViewById(R.id.login_btn);

        toPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PasswordResetActivity.class));
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                Log.d(TAG,"InfoGot"+ email+ password);


            }
        });

    }

    private void getInfo() {
        EditText mEmail = findViewById(R.id.login_email);

        email = mEmail.getText().toString();
        Log.d(TAG, "Checking if login email emty");
        if (email.isEmpty()) {
            Log.d(TAG, "loginEmail:empty");
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show();
        }Log.d(TAG, "loginEmail:notEmpty");

        password = mPassword.getText().toString();
        Log.d(TAG, "Checking if LoginPassword empty");
        if (password.isEmpty()) {
            Log.d(TAG, "loginPAssword:Empty");
            Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "loginPassword:notEmpty");
            Log.d(TAG, "onClick: emailverified: yes");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "Checking if Login Task is Succesfull");
                            if (task.isSuccessful()) {
                                Log.d(TAG, "LogIn:succesfull");
                            } else {
                                Log.d(TAG, "Login: Failed");
                                if (password.length() < 6) {
                                    Log.d(TAG, "passwordLegth: less");
                                    mPassword.setError("Must Contain 6 Charecters");
                                } else {
                                    Log.d(TAG, "Athentication failed!");
                                    Toast.makeText(context, "Athentication Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mListener);

        FirebaseUser mUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mListener);
    }
}
