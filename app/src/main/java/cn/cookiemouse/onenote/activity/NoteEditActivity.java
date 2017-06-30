package cn.cookiemouse.onenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cookiemouse.onenote.DatabaseOperator;
import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.flag.NoteFlag;

public class NoteEditActivity extends AppCompatActivity implements DatabaseOperator.OnDataChangedListener {

    private static final String TAG = "NoteEditActivity";

    private EditText mEditText;
    private ImageView mImageView;

    private DatabaseOperator mDatabaseOperator;

    private int position = 0;

    private String mStringText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        init();

        setEventListener();
    }


    private void init() {
        mEditText = (EditText) findViewById(R.id.et_activity_note_edit);
        mImageView = (ImageView) findViewById(R.id.iv_layout_note_edit_1);

        mDatabaseOperator = new DatabaseOperator(this);

        position = getIntent().getIntExtra(NoteFlag.NOTE_EDIT_POSITION, 0);

        mStringText = mDatabaseOperator.getNoteList().get(position).getText();

        mEditText.setText(mStringText);

        //  将光标置文本末尾
        mEditText.setSelection(mStringText.length());
    }

    private void setEventListener() {

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_DONE: {
                        saveText();
                    }
                    default: {
                        Log.i(TAG, "onEditorAction: default");
                    }
                }
                return true;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText();
            }
        });
    }

    @Override
    public void onDataChanged() {
        Log.i(TAG, "onDataChanged: ");
        finish();
    }

    //  保存操作
    private void saveText() {
        mStringText = mEditText.getText().toString();
        mDatabaseOperator.updateNoteListDate(position, mStringText);
    }
}
