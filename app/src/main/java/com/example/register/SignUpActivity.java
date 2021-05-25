package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    RadioGroup radioGroup;
    EditText name, emailId, newPassword, RePassword;
    Button signUp;
    String userName, password, retypePassword, email, role;
    public static final String EXTRA_FIRST_ARRAY = "ExtraFirstArray";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

//        final ArrayList<UserData> user = new ArrayList<UserData>();
//
//        radioGroup = (RadioGroup) findViewById(R.id.roleSignUp);
//        int id = radioGroup.getCheckedRadioButtonId();
//        if (id == R.id.studentSignUp) {
//            role = "Student";
//        } else if (id == R.id.teacherSignUp) {
//            role = "Teacher";
//        }
//
//        name = (EditText) findViewById(R.id.name);
//        userName = name.getText().toString();
//        Log.v(getLocalClassName(), userName);

        emailId = (EditText) findViewById(R.id.email);

        newPassword = (EditText) findViewById(R.id.newPassword);

        RePassword = (EditText) findViewById(R.id.retypePassword);

        signUp = (Button) findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailId.getText().toString();
                password = newPassword.getText().toString();
                retypePassword = RePassword.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                if (password.isEmpty()) {
                    newPassword.setError("Please enter password");
                    newPassword.requestFocus();
                }
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }
                if (retypePassword.isEmpty()) {
                    RePassword.setError("Please retype your password");
                }
                if (!retypePassword.equals(password)) {
                    RePassword.setError("Password didn't matched");
                }
                if ((!email.isEmpty() && !password.isEmpty()) && retypePassword.equals(password)) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).
                            addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(SignUpActivity.this, UserActivity.class));
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
