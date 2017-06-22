package cn.cookiemouse.onenote.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.cookiemouse.onenote.R;

public class GuideActivity extends Activity {

    private static final String TAG = "GuideActivity";

    private static final int DELAY_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        applyPermission();

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5949fa7e");
    }

    private void toActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this, NoteListActivity.class);
                startActivity(intent);
                GuideActivity.this.finish();
            }
        }, DELAY_TIME);
    }

    private void applyPermission() {
        int check_save = ContextCompat.checkSelfPermission(GuideActivity.this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int check_record = ContextCompat.checkSelfPermission(GuideActivity.this
                , Manifest.permission.RECORD_AUDIO);

        if (PackageManager.PERMISSION_GRANTED != check_save) {
            ActivityCompat.requestPermissions(GuideActivity.this
                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 1);
            return;
        }

        if (PackageManager.PERMISSION_GRANTED != check_record) {
            ActivityCompat.requestPermissions(GuideActivity.this
                    , new String[]{Manifest.permission.RECORD_AUDIO}
                    , 2);
            return;
        }

        toActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    applyPermission();
                    break;
                } else {
                    GuideActivity.this.finish();
                }
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    applyPermission();
                    break;
                } else {
                    GuideActivity.this.finish();
                }
            }
            default: {
                Log.i(TAG, "onRequestPermissionsResult: ");
            }
        }
    }
}
