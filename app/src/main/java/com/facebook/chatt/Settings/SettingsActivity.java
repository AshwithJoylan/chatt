package com.facebook.chatt.Settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.chatt.R;
import com.facebook.chatt.utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private Context context = SettingsActivity.this;
    private final String[] user = new String[5];
    TextView status;
    TextView name;


    //FireBase
    FirebaseAuth mAuth;
    FirebaseFirestore mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        setupUI();
    }

    private void setupUI() {

        CircleImageView profileImage = findViewById(R.id.settingsProfileImage);
        status = findViewById(R.id.settingProfileStatus);
        name = findViewById(R.id.settingsDisplayName);

        getDatabaseElements();
    }

    private void getDatabaseElements() {
        final FirebaseUser mUser = mAuth.getCurrentUser();
        Log.d(TAG, "onCreate: gotUser" + mUser);

        final Users users = new Users();

        mDb.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "onComplete: checkingIsTasakSuccefull");
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: tassk: successfull");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "onComplete: doc" + document.getData());
                                Log.d(TAG, "onComplete: " + document.getId() + " " + mUser.getUid());
                                if (document.getId().equals(mUser.getUid())) {
                                    user[0] = document.get("display_name").toString();
                                    user[1] = document.get("profile_image").toString();
                                    user[2] = document.get("thumb_image").toString();
                                    user[3] = document.get("status").toString();
                                    user[4] = document.get("id").toString();
                                    Log.d(TAG, "onComplete: user" + user[0]);

                                    Users users = new Users();
                                    users.setDisplay_name(user[0]);
                                    users.setProfile_image(user[1]);
                                    users.setThumb_image(user[2]);
                                    users.setStatus(user[3]);
                                    users.setId(user[4]);
                                    Log.d(TAG, "getDatabaseElements: returningUser" + users);

                                    status.setText(users.getStatus());
                                    name.setText(users.getDisplay_name());
                                }

                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
