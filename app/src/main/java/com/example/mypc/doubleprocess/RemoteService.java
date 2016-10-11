package com.example.mypc.doubleprocess;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MyPC on 2016/10/9.
 */

public class RemoteService extends Service {

    private Binder mBinder;
    private MyConn mConn;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mBinder=new MyBinder();
        if(mConn==null){
            mConn=new MyConn();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //第一次绑定对方。
        RemoteService.this.bindService(new Intent(this,LocalService.class),mConn,BIND_IMPORTANT);

        return super.onStartCommand(intent, flags, startId);
    }

    class  MyBinder extends IProcessService.Stub{


        public String getServiceName(){
            return  "RemoteService";
        }
    }

    class  MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("TAG","远程链接本地服务成功");
            Toast.makeText(RemoteService.this,"链接本地服务成功",Toast.LENGTH_LONG).show();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
                //对方（localService）被干掉了
            Log.i("TAG","localService服务被杀");
            Toast.makeText(RemoteService.this,"localService服务被杀",Toast.LENGTH_LONG).show();
            //启动对方（对方会走onStart绑定我）
            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
            //绑定对方（我再绑定对方）
            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),mConn, Context.BIND_IMPORTANT);
        }
    }
}
