package com.wgbb.vlc.ui;

/**
 * Created by GianniYan on 2016/2/20.
 */


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import com.wgbb.vlc.R;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.Media;
//import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.vlc.util.VLCInstance;


import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashMap;

public class VlcService extends Service {

    private boolean isStream = true;
    private static String TAG = "VlcService";
    private LibVLC mMediaPlayer;
    private boolean isBind = false;
    public MediaList playList;
    private String[] stringList;
    private boolean isFromOK;
    private char c;

    private int count;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mMediaPlayer = VLCInstance.getLibVlcInstance();
        } catch (LibVlcException e) {
            e.printStackTrace();
        }
        //EventHandler em = EventHandler.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        //filter.addAction("org.videolan.vlc.SleepIntent");
        //registerReceiver(serviceReceiver, filter);

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);

        mMediaPlayer.setMediaList();
        //mMediaPlayer.getMediaList().add("http://216.18.21.102:8080");
        mMediaPlayer.getMediaList().add("http://209.17.186.202:8275/1240");


        mMediaPlayer.eventVideoPlayerActivityCreated(true);

        //mMediaPlayer.playMRL("http://buddha.goodweb.cn/music/musicdownload_all/musicdownload/pms_dabei.mp3");
        //mMediaPlayer.playMRL("http://216.18.21.102:8080");
    }

    @Override
    public int onStartCommand( Intent intent,int flags, int startId ) {

        stringList = intent.getStringArrayExtra("PlayListString");

        if( stringList != null ) {
            c = stringList[0].charAt(0);
            if( c == 'T' ) {
                //mMediaPlayer.playMRL("http://216.18.21.102:8080");
                mMediaPlayer.playMRL("http://209.17.186.202:8275/1240");
            } else {
                mMediaPlayer.setMediaList();
                mMediaPlayer.getMediaList().clear();
                count = stringList.length;
                if( count == 1  ) {
                    mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, stringList[0]));
                    //mMediaPlayer.playIndex(0);
                } else {
                    for( int j = 1; j < count; j++ ) {
                        mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, stringList[j]));
                    }
                    //mMediaPlayer.play();
                }
                mMediaPlayer.play();
            }
        } else {
            mMediaPlayer.playIndex(0);
            //mMediaPlayer.play();
        }
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if(!isBind) {
            if (mMediaPlayer != null) {
                mMediaPlayer.eventVideoPlayerActivityCreated(false);
                EventHandler em = EventHandler.getInstance();
                em.addHandler(mVlcHandler);
                em.removeHandler(mVlcHandler);
            }
        //}
        /*if (mMediaPlayer != null) {
            mMediaPlayer.eventVideoPlayerActivityCreated(false);
            EventHandler em = EventHandler.getInstance();
            em.removeHandler(mVlcHandler);
        }*/
        mMediaPlayer.playMRL("");

        //super.onDestroy();
        /*if(!isBind) {
            mMediaPlayer.stop();
        }*/
    }


    @Override
    public IBinder onBind(Intent intent) {
        //mMediaPlayer.play();
        //isBind = true;
        /*synchronized (syncObject) {
            if(!isRunning){
                isRunning = true;
                serviceThread = new Thread(this);
                serviceThread.start();
            }
        }*/
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //mMediaPlayer.stop();
        //isBind = false;
        return super.onUnbind(intent);
    }



    /*private class LocalBinder extends Binder {
        VlcService getService() {
            return VlcService.this;
        }
    }
    public static VlcService getService(IBinder iBinder) {
        LocalBinder binder = (LocalBinder) iBinder;
        return binder.getService();
    }*/
    private Handler mVlcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.getData() == null)
                return;

            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerTimeChanged:
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    break;
                case EventHandler.MediaPlayerPlaying:
                    //mHandler.removeMessages(HANDLER_BUFFER_END);
                    //mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
                    break;
                case EventHandler.MediaPlayerBuffering:
                    break;
                case EventHandler.MediaPlayerLengthChanged:
                    break;
                case EventHandler.MediaPlayerEndReached:
                    break;
            }

        }
    };
}


