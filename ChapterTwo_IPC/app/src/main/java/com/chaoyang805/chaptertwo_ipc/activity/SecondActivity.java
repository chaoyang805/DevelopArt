package com.chaoyang805.chaptertwo_ipc.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.User;
import com.chaoyang805.chaptertwo_ipc.UserManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by chaoyang805 on 16/7/25.
 */

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("SecondActivity", "UserId is: " + UserManager.sUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFromFile();
    }

    private void readFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String myPath = Environment.getExternalStorageDirectory().getPath() + "/chaoyang805/";
                String cachePath = myPath + "cache";
                User u = null;
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(cachePath)));
                    u = (User) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (u != null) {
                    Log.d("TAG", u.userName);
                } else {
                    Log.d("TAG", "read failed");
                }
            }
        }).start();
    }
}
