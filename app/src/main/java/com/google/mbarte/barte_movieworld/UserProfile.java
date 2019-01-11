package com.google.mbarte.barte_movieworld;

public class UserProfile {
    public String fullname;
    public String email;
    public String birth;

    public UserProfile()
    {

    }

    public UserProfile(String fullname, String email, String birth)
    {
        this.fullname = fullname;
        this.email = email;
        this.birth = birth;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth() {
        return birth;
    }
}
