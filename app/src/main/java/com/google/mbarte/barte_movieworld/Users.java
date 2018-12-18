package com.google.mbarte.barte_movieworld;

public class Users {
    String userID;
    String fullname;
    String email;
    String password;
    String repassword;

    public Users()
    {

    }

    public Users(String userID, String fullname, String email, String password, String repassword) {
        this.userID = userID;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.repassword = repassword;
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
}
