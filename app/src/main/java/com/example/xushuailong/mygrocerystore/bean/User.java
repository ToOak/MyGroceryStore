package com.example.xushuailong.mygrocerystore.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.example.xushuailong.mygrocerystore.utils.LogUtil;

public class User implements Parcelable, Inter1, Inter2 {
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
//        LogUtil.e("inter a: " + a);
    }

    public void onNameClick(View view) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.nickName);
        // todo 1
        dest.writeByte(this.isMale ? (byte) 1 : (byte) 0);
        dest.writeInt(this.age);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.nickName = in.readString();
        // todo 2
        this.isMale = in.readByte() != 0;
        this.age = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

interface Inter1 {
//    void fun();

    int a = 1;
}

interface Inter2 {
//    int fun();

    int a = 2;
}

