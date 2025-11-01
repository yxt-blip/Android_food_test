package com.example.myshipingjiance;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myshipingjiance.db.UserDao;

import org.junit.Test;

public class DatabaseTest {
    @Test
    public void testUserDao(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserDao userDao =new UserDao(appContext);
        System.out.println(userDao.getCurrentUser());
    }
}
