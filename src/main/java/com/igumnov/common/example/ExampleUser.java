package com.igumnov.common.example;

import com.igumnov.common.orm.Id;

public class ExampleUser {

    @Id(autoIncremental = false)
    private String userName;
    private String userPassword;

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
