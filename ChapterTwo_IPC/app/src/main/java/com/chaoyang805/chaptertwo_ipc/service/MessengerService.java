package com.chaoyang805.chaptertwo_ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.chaoyang805.chaptertwo_ipc.MyConstant;

/**
 * Created by chaoyang805 on 16/7/26.
 */

public class MessengerService extends Service {

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MyConstant.MSG_FROM_CLIENT:
                    Bundle data = msg.getData();
                    String msgStr = data.getString("msg");
                    if (msgStr != null) {
                        Log.d("MessengerService", msgStr);
                    }
                    Messenger client = msg.replyTo;

                    Bundle replyData = new Bundle();
                    replyData.putString("reply", "嗯,我已经收到你的消息了,但是我不会理你.");
                    Message replayMsg = Message.obtain(null, MyConstant.MSG_FROM_SERVER);
                    replayMsg.setData(replyData);
                    try {
                        if (client != null) {
                            client.send(replayMsg);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default: super.handleMessage(msg);
            }
        }
    }

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }
}
