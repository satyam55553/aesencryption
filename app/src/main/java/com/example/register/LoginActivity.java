package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    String role, userEmail, userPass;
    UserData userData;
    ArrayList<UserData> list;
    EditText userName, password;
    Button signIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            mFirebaseAuth = FirebaseAuth.getInstance();
//            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.roleLogIn);
//            int id = radioGroup.getCheckedRadioButtonId();
//            if (id == R.id.studentsLogIn) {
//                role = "Student";
//            } else if (id == R.id.teachersLogIn) {
//                role = "Teacher";
//            }
            userName = (EditText) findViewById(R.id.userEmail);
            password = (EditText) findViewById(R.id.password);
            signIn = (Button) findViewById(R.id.login);

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LoginActivity.this, "PLEASE LOGIN", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userEmail = userName.getText().toString();
                    Log.v(String.valueOf(LoginActivity.this),"User Email = "+userEmail);
                    userPass = password.getText().toString();
                    
                    if (TextUtils.isEmpty(userEmail)) {
                        userName.setError("Please enter email id");
                        userName.requestFocus();
                    } else if (userPass.isEmpty()) {
                        password.setError("Please enter password");
                        password.requestFocus();
                    } else if (userEmail.isEmpty() && userPass.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                    } else if (!(userEmail.isEmpty() && userPass.isEmpty())) {
                        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPass).
                                addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Log in Failed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "You are not registered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//        mFirebaseAuth.getCurrentUser();
    }
}
