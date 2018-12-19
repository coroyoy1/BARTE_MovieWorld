package com.google.mbarte.barte_movieworld;

import com.google.firebase.storage.StorageReference;

public class Users {
    String userID;
    String fullname;
    String email;
    String password;
    String repassword;
    String birth;

    public Users()
    {

    }

    public Users(String userID, String fullname, String email, String password, String repassword, String birth) {
        this.userID = userID;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.repassword = repassword;
        this.birth = birth;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepassword() {
        return repassword;
    }

    public String getBirth()
    {
        return birth;
    }
}
