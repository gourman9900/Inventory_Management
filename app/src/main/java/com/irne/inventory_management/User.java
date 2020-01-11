package com.irne.inventory_management;

public class User {
    public String name,email,signup_id;

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignup_id() {
        return signup_id;
    }

    public void setSignup_id(String signup_id) {
        this.signup_id = signup_id;
    }
}
