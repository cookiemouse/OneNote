package cn.cookiemouse.onenote;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.io.File;

import cn.cookiemouse.onenote.JsonBean.ResultJson;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class SROperator {
    private static final String TAG = "SROperator";

    private SpeechRecognizer mSpeechRecognizer;
    private RecognizerListener mRecognizerListener;
    private String str_path;
    private String str_result = "";
    private OnResultListener mOnResultListener;

    public SROperator(Context context) {
        initSR(context);
    }

    private void initSR(Context context) {
        str_path = Environment.getExternalStorageDirectory().getPath() + "/test";
        File f = new File(str_path);
        if (!f.exists()) {
            f.mkdirs();
        }

        mSpeechRecognizer = SpeechRecognizer.createRecognizer(context, new InitListener() {
            @Override
            public void onInit(int i) {
                Log.i(TAG, "onInit: i-->" + i);
            }
        });

        mRecognizerListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
                Log.i(TAG, "onVolumeChanged: ");
            }

            @Override
            public void onBeginOfSpeech() {
                Log.i(TAG, "onBeginOfSpeech: ");
                str_result = "";
            }

            @Override
            public void onEndOfSpeech() {
                Log.i(TAG, "onEndOfSpeech: ");
                if (null == mOnResultListener) {
                    throw new NullPointerException("OnResultListener is null");
                }
                mOnResultListener.onEndOfSpeech();
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                Log.i(TAG, "onResult: -->" + recognizerResult.getResultString());
                Gson gson = new Gson();
                ResultJson resultJson = gson.fromJson(recognizerResult.getResultString()
                        , ResultJson.class);
                for (ResultJson.WsBean wsBean : resultJson.getWs()) {
                    for (ResultJson.WsBean.CwBean cwBean : wsBean.getCw()) {
                        str_result += cwBean.getW();
                    }
                }
                if (resultJson.isLs()){
                    if (null == mOnResultListener) {
                        throw new NullPointerException("OnResultListener is null");
                    }
                    mOnResultListener.onResult(str_result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.i(TAG, "onError: ");
                str_result = "";
                if (null == mOnResultListener) {
                    throw new NullPointerException("OnResultListener is null");
                }
                mOnResultListener.onResult(str_result);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                Log.i(TAG, "onEvent: ");
            }
        };

        mSpeechRecognizer.setParameter(SpeechConstant.NET_TIMEOUT, "8000");
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "3500");
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1500");
        str_path += "/temp";
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, str_path);
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
    }

    public void startListen() {
        mSpeechRecognizer.startListening(mRecognizerListener);
    }

    public boolean isListening() {
        return mSpeechRecognizer.isListening();
    }

    public void stopListening() {
        mSpeechRecognizer.stopListening();
    }

    public interface OnResultListener {
        void onResult(String result);
        void onEndOfSpeech();
    }

    public void setOnResultListener(OnResultListener listener) {
        this.mOnResultListener = listener;
    }
}

/*
 *  SpeechConstant.NET_TIMEOUT： 网络连接超时时间
    SpeechConstant.KEY_SPEECH_TIMEOUT：语音输入超时时间
    SpeechConstant.LANGUAGE：语言
    SpeechConstant.ACCENT：语言区域
    SpeechConstant.DOMAIN：应用领域
    SpeechConstant.CLOUD_GRAMMAR：云端语法ID
    SpeechConstant.LOCAL_GRAMMAR：本地语法ID
    SpeechConstant.AUDIO_SOURCE：音频源
    SpeechConstant.VAD_BOS：前端点超时
    SpeechConstant.VAD_EOS：后端点超时
    SpeechConstant.VAD_ENABLE：启用VAD
    SpeechConstant.SAMPLE_RATE：识别采样率
    SpeechConstant.ASR_NBEST：句子级多候选
    SpeechConstant.ASR_WBEST：词级多候选
    SpeechConstant.ASR_PTT：设置是否有标点符号
    SpeechConstant.RESULT_TYPE：识别结果类型
    SpeechConstant.ASR_AUDIO_PATH：识别录音保存路径
    SpeechConstant.ENGINE_TYPE：引擎类型；
    ResourceUtil.ASR_RES_PATH：离线资源路径；
    ResourceUtil.ENGINE_START：启动离线引擎；
    ResourceUtil.GRM_BUILD_PATH：离线语法路径；
    当前识别支持未压缩的16位，单声道，采样率为16000或8000，字节顺序为 Little-Endian的Windows PCM音频。
 * **/
