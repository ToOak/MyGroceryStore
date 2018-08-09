package com.example.xushuailong.mygrocerystore.bean;

import android.view.View;

public class User {
    private String name;
    private String nickName;
    private boolean isMale;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String name, String nickName, boolean isMale, int age) {
        this.name = name;
        this.nickName = nickName;
        this.isMale = isMale;
        this.age = age;
    }

    public void onNameClick(View view) {

    }
}
