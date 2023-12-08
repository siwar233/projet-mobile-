package com.example.appel;

public class Teacher {

    public String fullName;
    public String email;

    public String gender;
    public boolean integrationWeb;
    public boolean ia;
    public boolean developmentMobile;


    public Teacher()
    {

    }



    public Teacher(String fullName, String email, String gender, boolean integrationWeb, boolean ia, boolean developmentMobile) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.integrationWeb = integrationWeb;
        this.ia = ia;
        this.developmentMobile = developmentMobile;
    }
}


