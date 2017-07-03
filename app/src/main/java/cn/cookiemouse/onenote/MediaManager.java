package cn.cookiemouse.onenote;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by xp on 17-3-15.
 * 提供对外的方法有setSource、setLoop、isLoog、pause、replay、setProgress、destroy
 * 对外回调PlayListener==>onCompletion、onStart、onError
 * mediaPlayer.prepare();
 * mediaPlayer.prepareAsync();
 * mediaPlayer.start();
 * mediaPlayer.pause();
 * mediaPlayer.stop();
 * mediaPlayer.reset();
 * mediaPlayer.release();
 */

public class MediaManager {

    private final static String TAG = "MediaManager";

    private static MediaManager mediaManager;

    private MediaPlayer mediaPlayer;

    private PlayListener mPlayListener;

    //单例模式
    public static MediaManager getInstance() {
        if (null == mediaManager) {
            //仅同步实例化的代码块
            synchronized (MediaManager.class) {
                if (null == mediaManager) {
                    mediaManager = new MediaManager();
                }
            }
        }
        return mediaManager;
    }

    private MediaManager() {

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                if (null != mPlayListener) {
//                    mPlayListener.onStart();
//                } else {
//                    throw new UnsupportedOperationException("PlayListener is null");
//                }

                try {
                    mPlayListener.onStart();
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCompletion: PlayListener is null " + e);
                }

                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                try {
                    mPlayListener.onCompletion();
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCompletion: PlayListener is null " + e);
                }

//                if (null != mPlayListener) {
//                    mPlayListener.onCompletion();
//                } else {
//                    throw new UnsupportedOperationException("PlayListener is null");
//                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                try {
                    mPlayListener.onError("播放出错！");
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCompletion: PlayListener is null " + e);
                }

//                if (null != mPlayListener) {
//                    mPlayListener.onError("播放出错！");
//                } else {
//                    throw new UnsupportedOperationException("PlayListener is null");
//                }
                return false;
            }
        });
    }

    public void setResource(String path) {

        Log.i(TAG, "setSource path");
        if (null == mediaPlayer) {
            return;
        }

        //重置
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
    }

    public void pause(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void destroy() {
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaManager = null;
    }

    public interface PlayListener {
        //播放完成
        void onCompletion();

        //播放状态改变
        void onStart();

        //播放出错
        void onError(String error);
    }

    public void setPlayListener(PlayListener listener) {
        this.mPlayListener = listener;
    }

}

