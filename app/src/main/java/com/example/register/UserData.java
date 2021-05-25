package com.example.register;

import android.provider.ContactsContract;

public class UserData {
    String mUserName,mPassword,mEmail,mRole;
    UserData(String userName, String password, String email,String role){
        mUserName=userName;
        mPassword=password;
        mEmail=email;
        mRole=role;
    }
    public String getUserName(){return  mUserName;}
    public String getPassword(){return mPassword;}
    public String getEmail(){return mEmail;}
    public String getRole(){return  mRole;}
}
