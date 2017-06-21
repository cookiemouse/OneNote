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
import android.view.Window;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.cookiemouse.onenote.R;

public class GuideActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        applyPermission();

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5949fa7e");
    }

    private void toActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this, NoteListActivity.class);
                startActivity(intent);
                GuideActivity.this.finish();
            }
        }, 1000);
    }

    private void applyPermission(){
        int check = ContextCompat.checkSelfPermission(GuideActivity.this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED == check){
            toActivity();
            return;
        }
        ActivityCompat.requestPermissions(GuideActivity.this
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            return;
//        }
        toActivity();
    }
}
