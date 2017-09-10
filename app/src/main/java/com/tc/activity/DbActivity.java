package com.tc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tc.application.R;
import com.tc.greendao.DbManager;
import com.tc.greendao.User;

import java.util.List;

public class DbActivity extends Activity {

    private static final String TAG = "DbActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        DbManager dbManager = DbManager.getInstance(this);
        for(int i=0;i<5;i++){
            User user = new User();
//            user.setId((long) i);
            user.setNickName("tian"+i);
            user.setUserName("name"+i);
            user.setAge(i+10);
            dbManager.insertUser(user);
        }
        List<User> userList = dbManager.queryUserList();
        for(User user :userList){
            Log.i(TAG,"user = "+user);
            if(user.getId() == 0){
                dbManager.deleteUser(user);
                Log.i(TAG,"delect "+user);
            }
            if(user.getId() == 1){
                user.setAge(100);
                dbManager.updateUser(user);
            }
        }
        userList = dbManager.queryUserList();

        for(User user:userList){
            Log.i(TAG,"new user = "+user);
        }

        List<User> userIdList = dbManager.queryUser(3);
        Log.i(TAG,"user id "+userIdList);
    }
}
