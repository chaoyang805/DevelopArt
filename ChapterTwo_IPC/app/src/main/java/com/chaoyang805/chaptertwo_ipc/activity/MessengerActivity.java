package com.chaoyang805.chaptertwo_ipc.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaoyang805.chaptertwo_ipc.MyConstant;
import com.chaoyang805.chaptertwo_ipc.R;
import com.chaoyang805.chaptertwo_ipc.service.MessengerService;

/**
 * Created by chaoyang805 on 16/7/26.
 */

public class MessengerActivity extends AppCompatActivity {

    private Messenger mService;
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            Bundle data = new Bundle();
            data.putString("msg", "Hello this is client");
            Message msg = Message.obtain(null, MyConstant.MSG_FROM_CLIENT);
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstant.MSG_FROM_SERVER:
                    Bundle replayData = msg.getData();
                    Log.d("MessengerActivity", replayData.getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    private EditText etInput;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        bindService(new Intent(this, MessengerService.class), mServiceConnection, BIND_AUTO_CREATE);

        etInput = (EditText) findViewById(R.id.etInput);
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = Message.obtain(null, MyConstant.MSG_FROM_CLIENT);
                Bundle data = new Bundle();
                data.putString("msg", etInput.getText().toString());
                msg.setData(data);
                msg.replyTo = mGetReplyMessenger;
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    etInput.setText("error");
                }
                etInput.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
