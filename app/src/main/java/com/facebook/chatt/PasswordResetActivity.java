package com.facebook.chatt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordResetActivity";
    private Context context = PasswordResetActivity.this;
    private String email;
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("verifying..");

        setUpUI();
    }

    //setting up ui
    private void setUpUI() {

        TextView toLogin = findViewById(R.id.to_login);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LogInActivity.class));
                finish();
            }
        });

        Button btnReset = findViewById(R.id.reset_btn);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getInfo();
                Log.d(TAG, "onClick: info got" + email);

                Log.d(TAG, "onClick: sending email for reset");

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onComplete: check if task is complete");

                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: resetEmailSent: succesfull");
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Password Reset Email sent!", Toast.LENGTH_SHORT).show();
                                } else  {
                                    Log.d(TAG, "onComplete: resetEmailSent: unssuccessfull");
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Email does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    //Gettinig info from Edit texts
    private void getInfo() {

        EditText mEmail = findViewById(R.id.reset_email);

        email = mEmail.getText().toString();
        if (email.isEmpty()) {
            Log.d(TAG, "getInfo: email Empty");
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show();
        }
    }
}
