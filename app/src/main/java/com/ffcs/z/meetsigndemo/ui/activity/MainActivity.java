package com.ffcs.z.meetsigndemo.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffcs.z.meetsigndemo.R;
import com.ffcs.z.meetsigndemo.bean.SignBean;
import com.ffcs.z.meetsigndemo.service.SignBroadcastService;
import com.ffcs.z.meetsigndemo.ui.base.BaseActivity;
import com.ffcs.z.meetsigndemo.utils.SoundPlayUtils;
import com.ffcs.z.meetsigndemo.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/24
 * 描述 ：
 * ============================================
 */

public class MainActivity extends BaseActivity {
    private static final int SDK_PERMISSION_REQUEST = 999;
    @Bind(R.id.show_state)
    TextView show;
    @Bind(R.id.showNetState)
    ImageView showNetState;

    private Intent mIntent;
    private SignReceiver mReceiver;
    private int currentNet = 3;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getPersimmions();
        createMusicFile();
        SoundPlayUtils.init(this);
//        bindService(new Intent(this, SignService.class), this, BIND_AUTO_CREATE);
        mIntent = new Intent(MainActivity.this, SignBroadcastService.class);
        startService(mIntent);
    }


    private void createMusicFile() {
        String facePath = Environment.getExternalStorageDirectory().toString() + File.separator + "会议音频";
        File file = new File(facePath);
        if (!file.exists()) file.mkdir();
        notifySystemToScan(facePath);
    }

    public void notifySystemToScan(String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    @Override
    protected void initData() {
        mReceiver = new SignReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ffcs.z.RECEIVER");
        registerReceiver(mReceiver, intentFilter);

    }


    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 获取存储权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        SignService.Binder binder = (SignService.Binder) service;
//        SignService myService = binder.getService();
//                 myService.setCallback(new SignService.Callback() {
//                         @Override
//                         public void onDataChange(SignBean bean) {
//                             Log.i("ServiceD","onDataChange:"+bean.getDinnerSignStatus());
//
//                     }); if(bean==null){
//
//                             }
//
//                             if (bean.getDinnerSignStatus() == 0) {
//                                 startActivity(new Intent(MainActivity.this, SignAcitivity.class));
//                             }
//                         }
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//
//    }


    public class SignReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {



            SignBean bean = (SignBean) intent.getSerializableExtra("SignBean");

            int error = bean.getNetWork();
            if (error != currentNet) {
                switch (error) {
                    case 1:
                        showNetState.setImageResource(R.drawable.ic_offline);
                        //离线

                        break;
                    case 0:
                        showNetState.setImageResource(R.drawable.ic_online);
                        //在线
                        break;
                }
                currentNet=error;

            }
            if (bean.getMeetinginfos()==null) {
                show.setText(UIUtils.getString(R.string.main_no_meet));
            } else if (error==0) {
                if(bean.getMeetinginfos().getMeetingSignStatus() == 0){
                Intent intent1 = new Intent(MainActivity.this, SignAcitivity.class);
                intent1.putExtra("meetInfoId", bean.getMeetinginfos().getMeetingInfoId());
                startActivity(intent1);}
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ServiceD", "onDestroy");
//        unbindService(this);
        stopService(mIntent);
        unregisterReceiver(mReceiver);
    }


}
