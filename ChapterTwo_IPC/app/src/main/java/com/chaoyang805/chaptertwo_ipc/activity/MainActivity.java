package com.chaoyang805.chaptertwo_ipc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.User;
import com.chaoyang805.chaptertwo_ipc.UserManager;
import com.chaoyang805.chaptertwo_ipc.bindpool.BinderPool;
import com.chaoyang805.chaptertwo_ipc.bindpool.BinderPoolActivity;
import com.chaoyang805.chaptertwo_ipc.contentprovider.ProviderActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserManager.sUserId = 2;
    }

    @Override
    protected void onResume() {
        super.onResume();

        User u = new User(1, "chaoyang805", true);
//        writeToFile(u);
//        readFromFile();
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

    private void writeToFile(final Serializable object) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                String myPath = Environment.getExternalStorageDirectory().getPath() + "/chaoyang805/";
                String cachePath = myPath + "cache";
                File dir = new File(myPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cachedFile = new File(cachePath);
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new FileOutputStream(cachedFile));
                    oos.writeObject(object);
                    Log.d("TAG", "Persist User");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (oos != null) {
                            oos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void enterSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void enterThird(View view) {

        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    public void enterMessengerActivity(View view) {
        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }

    public void enterBookActivity(View view) {
        Intent intent = new Intent(this, BookManagerActivity.class);
        startActivity(intent);
    }

    public void enterProviderActivity(View view) {
        Intent intent = new Intent(this, ProviderActivity.class);
        startActivity(intent);
    }

    public void enterBinderPoolActivity(View view) {
        Intent intent = new Intent(this, BinderPoolActivity.class);
        startActivity(intent);
    }
}
