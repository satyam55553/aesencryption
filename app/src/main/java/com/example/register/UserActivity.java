package com.example.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserActivity extends AppCompatActivity {

    EditText inputText,inputPassword;
    TextView outputText;
    ImageView tala;
    Button encBtn, decBtn;
    String outputString;
    String AES = "AES";

    UserData userData;
    Button logOut;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logOut=findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserActivity.this, MainActivity.class));
            }
        });

        inputText = (EditText) findViewById(R.id.inputText);
        inputPassword = (EditText) findViewById(R.id.password);
        tala=(ImageView) findViewById(R.id.lockImage);
        encBtn = (Button) findViewById(R.id.encBtn);
        decBtn = (Button) findViewById(R.id.decBtn);

        encBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    outputString = encrypt(inputText.getText().toString(), inputPassword.getText().toString());
                    inputText.setText(outputString);
                    tala.setVisibility(View.VISIBLE);
                    inputPassword.getText().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = decrypt(outputString, inputPassword.getText().toString());
                    inputPassword.getText().clear();

                } catch (Exception e) {
                    Toast.makeText(UserActivity.this, "WrongPassword", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                tala.setVisibility(View.GONE);
                inputText.setText(outputString);
            }
        });
    }

    private String decrypt(String outputString, String Password) throws Exception{
        SecretKeySpec key =  generateKey(Password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private String encrypt(String Data, String Password) throws Exception{
        SecretKeySpec key =  generateKey(Password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        DialogInterface.OnClickListener discardButtonClickListener=
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                };
        showExitConfirmationDialog(discardButtonClickListener);
    }
    private void showExitConfirmationDialog(
            DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit?");
        builder.setPositiveButton("Exit",discardButtonClickListener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //User clicked "Keep Editing" ,so dismiss the dialog and continue editing the pet
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
//        create and show the AlertDialog
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
