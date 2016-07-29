package com.chaoyang805.chaptertwo_ipc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.UserManager;

/**
 * Created by chaoyang805 on 16/7/25.
 */

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Log.d("ThirdActivity", "UserId is: " + UserManager.sUserId);
    }
}
