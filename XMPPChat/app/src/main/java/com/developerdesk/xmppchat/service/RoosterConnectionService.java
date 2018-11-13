package com.developerdesk.xmppchat.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.developerdesk.xmppchat.Interface.ConnectionCallback;
import com.developerdesk.xmppchat.datamodel.ChatDataModel;
import com.developerdesk.xmppchat.xmpp_operation.RoosterConnection;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by gakwaya on 4/28/2016.
 */
public class RoosterConnectionService extends Service implements ConnectionCallback {
    private static final String TAG ="RoosterService";
    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;
    private ConnectionCallback connectionCallback;
    //We use this handler to post messages to
    //the background thread.

    public static final String SEND_MESSAGE = "com.blikoon.rooster.sendmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";

    public static RoosterConnection.ConnectionState sConnectionState;
    public static RoosterConnection.LoggedInState sLoggedInState;
    private RoosterConnection mConnection;


    public RoosterConnectionService() {

    }

    private final IBinder binder = new LocalBinder();

    @Override
    public void connectedSuccessfully() {
        connectionCallback.connectedSuccessfully();
    }

    @Override
    public void connectionError() {
connectionCallback.connectionError();
    }

    public class LocalBinder extends Binder {
        public RoosterConnectionService getService() {
            return RoosterConnectionService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate()");
    }


    public void start()
    {
        Log.d(TAG," Service Start() function called.");
        if(!mActive)
        {
            mActive = true;
            if( mThread ==null || !mThread.isAlive())
            {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Looper.prepare();
                        mTHandler = new Handler();
                        //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                        Looper.loop();

                    }
                });
                mThread.start();
            }


        }


    }

    public void stop()
    {
        Log.d(TAG,"stop()");
        mActive = false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
//                if( mConnection != null)
//                {
//
//                }
                //CODE HERE IS MEANT TO SHUT DOWN OUR CONNECTION TO THE SERVER.
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand()");
        start();
        return Service.START_STICKY;
        //RETURNING START_STICKY CAUSES OUR CODE TO STICK AROUND WHEN THE APP ACTIVITY HAS DIED.
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
        stop();
    }



    private void initConnection(String userName,String password,ConnectionCallback connectionCallback)
    {
        Log.d(TAG,"initConnection()");
        if( mConnection == null)
        {
            mConnection = new RoosterConnection(this,userName,password,connectionCallback);
        }
        try
        {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("sendmessage-event"));
            mConnection.connect();

        }catch (IOException |SmackException |XMPPException e)
        {
            Log.d(TAG,"Something went wrong while connecting ,make sure the credentials are right and try again");
            connectionCallback.connectionError();
            e.printStackTrace();
            //Stop the service all together.
            stopSelf();
        }

    }

    public void connect(String userName, String password, ConnectionCallback connectionCallback)
    {
        this.connectionCallback = connectionCallback;
        initConnection(userName,password,connectionCallback);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            mConnection.sendmessage(message,"vijay@desktop-qqj7qad/Smack");

        }
    };
}