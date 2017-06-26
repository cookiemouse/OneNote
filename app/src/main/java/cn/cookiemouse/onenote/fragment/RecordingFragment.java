package cn.cookiemouse.onenote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.cookiemouse.onenote.R;

/**
 * Created by cookie on 17-6-26.
 */

public class RecordingFragment extends DialogFragment {

    private static final String TAG = "RecordingFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        setCancelable(false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.diglog_recording, container);
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
