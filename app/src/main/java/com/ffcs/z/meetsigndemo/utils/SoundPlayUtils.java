package com.ffcs.z.meetsigndemo.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.util.Log;

import com.ffcs.z.meetsigndemo.R;

import java.io.File;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/5/8
 * 描述 ：
 * ============================================
 */

public class SoundPlayUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        // 初始化声音
        mContext = context;


        String SuccessPath= Environment.getExternalStorageDirectory().toString()+ File.separator + "会议音频"+ File.separator+"Success.wav";
        String FailPath= Environment.getExternalStorageDirectory().toString()+ File.separator + "会议音频"+ File.separator+"Fail.wav";
        String RepeatPath= Environment.getExternalStorageDirectory().toString()+ File.separator + "会议音频"+ File.separator+"Repeat.wav";
        File successFile=new File(SuccessPath);
        File failFile=new File(FailPath);

        File repeatFile=new File(FailPath);

        if(successFile.exists()){
            mSoundPlayer.load(SuccessPath, 2);// 1成功
            Log.i("successFile","成功签到的音乐存在！" );
        }else{
            mSoundPlayer.load(mContext, R.raw.success, 2);// 1成功
            Log.i("successFile","成功签到的音乐不存在！" );

        }


        if(failFile.exists()){
            mSoundPlayer.load(FailPath, 2);// 2失败
            Log.i("failFile","失败签到的音乐存在！" );

        }else{
            mSoundPlayer.load(mContext,R.raw.fail, 2);// 2失败
            Log.i("failFile","失败签到的音乐不存在！" );

        }



        if(repeatFile.exists()){
            mSoundPlayer.load(RepeatPath, 2);// 2失败
            Log.i("repeatFile","重复签到的音乐存在！" );

        }else{
            mSoundPlayer.load(mContext,R.raw.repeat, 2);// 2失败
            Log.i("repeatFile","重复签到的音乐不存在！" );

        }



        mSoundPlayer.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.i("TAGA","sampleId:"+sampleId+"Status:"+status);

            }
        });



//        if(sett.getFailerURL()==null){
//            mSoundPlayer.load(mContext, R.raw.gun, 1);// 1成功
//            mSoundPlayer.load(mContext,R.raw.destroy, 1);// 2失败
//        }else{
//            mSoundPlayer.load(sett.getSuccessURL(), 1);// 1成功
//            mSoundPlayer.load(sett.getFailerURL(), 1);// 2失败
//        }



        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play( int  soundID) {
            /*   mSoundPlayer.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.i("TAGA","sampleId:"+sampleId+"Status:"+status);

            }
            });*/
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
        Log.d("int  soundID", "play: "+soundID);
        }}


