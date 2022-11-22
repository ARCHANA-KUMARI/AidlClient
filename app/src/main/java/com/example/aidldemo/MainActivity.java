package com.example.aidldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.example.aidlclient.databinding.ActivityMainBinding;
import com.example.common.ICommunicator;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "ArchanaMainActivity";
    private ActivityMainBinding mBinding;
    private ICommunicator mICommunicator;
    private CalcServiceConn mCalcSericeConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = mBinding.getRoot();
        setContentView(rootView);


        // Bind server service
        mBinding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: mCalcSericeConn" + mCalcSericeConn);
                if(mCalcSericeConn == null) {
                    mCalcSericeConn = new CalcServiceConn();
                }
                Intent intent = new Intent("com.example.aidldemo.services.CalculatorService");
                intent.setPackage("com.example.aidlserver");
                bindService(intent, mCalcSericeConn, Service.BIND_AUTO_CREATE);
            }
        });

        mBinding.btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: mICommunicator" + mICommunicator);
             if(mICommunicator != null){
                 try {
                     Log.i(TAG, "onClick: sum :" + mICommunicator.calculate(10,15));
                     Log.i(TAG, "onClick: Name:" + mICommunicator.getName());
                 } catch (RemoteException e) {
                     e.printStackTrace();
                 }
             }
            }
        });

    }

    class CalcServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: name " + name + "Binder Service" + service);
            mICommunicator = ICommunicator.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: name" + name);
          //  mICommunicator = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            ServiceConnection.super.onBindingDied(name);
            Log.i(TAG, "onBindingDied: name" + name);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            ServiceConnection.super.onNullBinding(name);
            Log.i(TAG, "onNullBinding: name" + name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        unbindService(mCalcSericeConn);
        mCalcSericeConn = null;
    }
}