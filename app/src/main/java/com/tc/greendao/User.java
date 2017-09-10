package com.tc.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhao on 17-9-9.
 */

@Entity
public class User {

    @Id
    private Long id;
    @Property(nameInDb = "USERNAME")
    private String userName;
    @Property(nameInDb = "NICKNAME")
    private String nickName;
    @Property(nameInDb ="AGE")
    private int age;

    @Generated(hash = 1629431375)
    public User(Long id, String userName, String nickName, int age) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.age = age;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                '}';
    }
}
