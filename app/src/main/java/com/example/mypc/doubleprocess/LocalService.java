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

public class LocalService extends Service {
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
        //再第二次还会绑定一次
        LocalService.this.bindService(new Intent(this,RemoteService.class),mConn,BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    class  MyBinder extends IProcessService.Stub{

        public String getServiceName(){
             return  "LocalService";
        }
    }
    class MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("TAG","本地链接远程服务成功");
            Toast.makeText(LocalService.this,"链接远程服务成功",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //对方（remoteService）被干掉了
            Log.i("TAG","remoteService服务被杀");
            Toast.makeText(LocalService.this,"remoteService服务被杀",Toast.LENGTH_LONG).show();
            //第二次启动对方（对方会走onStart再绑定我）
            LocalService.this.startService(new Intent(LocalService.this,RemoteService.class));
            //第二次绑定对方（我可以再次绑定对方）
            LocalService.this.bindService(new Intent(LocalService.this,RemoteService.class),mConn, Context.BIND_IMPORTANT);
        }
    }
}
