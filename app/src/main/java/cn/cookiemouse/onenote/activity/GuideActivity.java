package cn.cookiemouse.onenote.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
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

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5949fa7e");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toActivity();
            }
        }, 1000);

    }

    private void toActivity(){
        Intent intent = new Intent(GuideActivity.this, NoteListActivity.class);
        startActivity(intent);
    }
}
