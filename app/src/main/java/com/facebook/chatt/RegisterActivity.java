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

import com.facebook.chatt.utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Context context = RegisterActivity.this;

    private String email, password, name;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getting Firebase instence
        Log.d(TAG, "Getting Firebase Instaence");
        mAuth = FirebaseAuth.getInstance();

        setupAuthStateListener();
        setupUI();

    }

    //Setting up Auth state listener
    private void setupAuthStateListener() {

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "Getting CurrentUseer");
                FirebaseUser mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    Log.d(TAG, "USer:" + mUser);
                    sendVerificationEmail();
                    setupDatabase();
                }
            }
        };
    }


    //database uploading
    private void setupDatabase() {
        Log.d(TAG, "setupDatabase: settingDatabasetoUpload");
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();
        Log.d(TAG, "setupDatabase: uid:"+id);

        Users users = new Users(id, name, "Hi there, I'm using Text. app!", "default", "default");
        Log.d(TAG, "setupDatabase: user is" + users);
        Log.d(TAG, "setupDatabase: addindTocollection");
        mDb.collection("users")
                .document(mUser.getUid())
                .set(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onSuccess: DocAddedToDatabase: success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onSuccess: DocAddedToDatabase: Failuire");
                    }
                });


    }


    //verification email sending
    private void sendVerificationEmail() {
        Log.d(TAG, "Sending Verification Email");
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Checking if verification email sent");
                            if (task.isSuccessful()) {
                                Log.d(TAG, "verificationEmailSent:success");

                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(context, "Registration succefull! Verification email sent!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(context, LogInActivity.class));
                                finish();

                            } else {
                                Log.d(TAG, "verificatioonEmailSent:Failed");

                                //Restart activity
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                            }
                        }
                    });
        }
    }


    //Setting up ui elements
    private void setupUI() {


        TextView tvToLogin = findViewById(R.id.to_register_tv);
        Button btnRegister = findViewById(R.id.register_btn);

        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Going back to login screen");
                startActivity(new Intent(context, LogInActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                Log.d(TAG, "Info got:" + email + name + password);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Checking if Registration task is complete");

                                if (task.isSuccessful()) {
                                    Log.d(TAG, "creatUserWithEmail:success");
                                }
                                else {
                                    Log.d(TAG, "creatUserWithEmail:failed");
                                    Toast.makeText(context, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    //getting user info from edit text and dchecking wheater they are empty
    private void getInfo() {
        EditText mEmail = findViewById(R.id.register_email);
        EditText mPassword = findViewById(R.id.register_password);
        EditText mName = findViewById(R.id.register_name);

        email = mEmail.getText().toString();

        Log.d(TAG, "Checking of Email, password, name");

        if (email.isEmpty()) {
            Log.d(TAG, "Email:empty");
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show();
        }

        password = mPassword.getText().toString();

        if (password.isEmpty()) {
            Log.d(TAG, "Password:empty");
            Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if(password.length() < 6) {
            Log.d(TAG, "Passwor Too short");
            Toast.makeText(context, "Password must contain atleast 6 charecters ", Toast.LENGTH_SHORT).show();
        }

        name = mName.getText().toString();

        if (name.isEmpty()) {
            Log.d(TAG, "Name:empty");
            Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mListener);
    }
}
