package com.example.appel;

public class User {
    public String fullName;
    public String email;
    public String dateOfBirth;
    public String gender;
    public String mobile;

    public User()
    {

    }

    public User(String fullName, String email, String dateOfBirth, String gender, String mobile) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.mobile = mobile;
    }
}

