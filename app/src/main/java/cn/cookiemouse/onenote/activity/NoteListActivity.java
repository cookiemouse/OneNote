package cn.cookiemouse.onenote.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.onenote.DatabaseOperator;
import cn.cookiemouse.onenote.MediaManager;
import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.SROperator;
import cn.cookiemouse.onenote.adapter.NoteListAdapter;
import cn.cookiemouse.onenote.data.DataGrade;
import cn.cookiemouse.onenote.data.DataType;
import cn.cookiemouse.onenote.data.NoteListData;
import cn.cookiemouse.onenote.flag.NoteFlag;
import cn.cookiemouse.onenote.fragment.RecordingFragment;

public class NoteListActivity extends AppCompatActivity implements DatabaseOperator.OnDataChangedListener
        , NoteListAdapter.OnControlListener {

    private static final String TAG = "NoteListActivity";

    private static final String PATH_ROOT = Environment.getExternalStorageDirectory().getPath();

    private ListView mListViewNote;
    private LinearLayout mButtonListen;

    private SROperator mSrOperator;

    private List<NoteListData> mNoteListDataList;
    private NoteListAdapter adapter;

    private DatabaseOperator mDatabaseOperator;

    private RecordingFragment mRecordingFragment;

    private MediaManager mMediaManager;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initView();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mNoteListDataList) {
            mNoteListDataList.clear();
        }

        for (NoteListData data : mDatabaseOperator.getNoteList()) {
            mNoteListDataList.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mMediaManager.destroy();
        mDatabaseOperator.closeDB();
        super.onDestroy();
    }

    private void initView() {
        mListViewNote = (ListView) findViewById(R.id.lv_activity_notelist);
        mButtonListen = (LinearLayout) findViewById(R.id.btn_activity_listen);

        mSrOperator = new SROperator(this);

        mDatabaseOperator = new DatabaseOperator(this);

        mNoteListDataList = new ArrayList<>();

        adapter = new NoteListAdapter(NoteListActivity.this, mNoteListDataList);

        mRecordingFragment = new RecordingFragment();

        mMediaManager = MediaManager.getInstance();

        mListViewNote.setAdapter(adapter);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    private void setEventListener() {
        mButtonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // : 17-6-21 录制按钮事件
                mMediaManager.pause();
                mSrOperator.startListen();
                mRecordingFragment.show(getSupportFragmentManager(), "RecordingFragment");
            }
        });

        mSrOperator.setOnResultListener(new SROperator.OnResultListener() {
            @Override
            public void onResult(String result) {
                Log.i(TAG, "onResult: -->" + result);
                if ("".equals(result)) {
                    mToast.setText("您好像没有说话！");
                    mToast.show();
                    return;
                }

                long a = System.currentTimeMillis() / 1000;

                String name = "" + a;

                rename(name);

                mDatabaseOperator.addNoteList(result
                        , name
                        , DataType.TYPE_PERSONAL
                        , DataGrade.GRADE_NORMAL);

                // 滑动到最末尾
                mListViewNote.smoothScrollToPosition(mNoteListDataList.size());
            }

            @Override
            public void onEndOfSpeech() {
                mRecordingFragment.dismiss();
                Log.i(TAG, "onNoResult: ");
            }
        });

        mListViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mNoteListDataList.get(i).isState()) {
                    mNoteListDataList.get(i).setState(false);
                    adapter.notifyDataSetChanged();
                    return;
                }
                for (NoteListData data : mNoteListDataList) {
                    data.setState(false);
                }
                mNoteListDataList.get(i).setState(true);
                adapter.notifyDataSetChanged();

                // 让展开item置中
                int top = mListViewNote.getFirstVisiblePosition() + 1;
                int bottom = mListViewNote.getLastVisiblePosition() - 1;
                int middle = ((bottom - top) / 2);
                int position_visible = i - top;
                if (position_visible <= middle) {
                    mListViewNote.smoothScrollToPosition(i - 3);
                } else {
                    mListViewNote.smoothScrollToPosition(i + 3);
                }
            }
        });
    }

    //  改名
    private void rename(String name) {
        String path = PATH_ROOT + "/test/temp";
        String path2 = PATH_ROOT + "/test/" + name + ".wav";
        File file = new File(path);
        if (file.renameTo(new File(path2))) {
            Log.i(TAG, "rename: true");
        } else {
            Log.i(TAG, "rename: false");
        }
    }

    @Override
    public void onDataChanged() {
        Log.i(TAG, "onDataChanged: ");
        mNoteListDataList.clear();
        for (NoteListData data : mDatabaseOperator.getNoteList()) {
            mNoteListDataList.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDelete(int position) {
        Log.i(TAG, "onDelete: ");

        String path = PATH_ROOT
                + "/test/"
                + mDatabaseOperator.getNoteList().get(position).getPath()
                + ".wav";
        File file = new File(path);

        mDatabaseOperator.removeNoteListData(position);

        if (!file.exists()) {
            return;
        }
        if (file.delete()){
            Log.i(TAG, "onDelete: 删除成功");
        }
    }

    @Override
    public void onEdit(int position) {
        Log.i(TAG, "onEdit: ");
        Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
        intent.putExtra(NoteFlag.NOTE_EDIT_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onCopyed(int position) {
        mToast.setText("文本已复制到粘贴板！");
        mToast.show();
    }

    @Override
    public void onPlay(int position) {
        Log.i(TAG, "onPlay: ");

        String path = PATH_ROOT
                + "/test/"
                + mDatabaseOperator.getNoteList().get(position).getPath()
                + ".wav";

        Log.i(TAG, "onPlay: path-->" + path);

        File file = new File(path);
        if (file.exists()) {
            mMediaManager.setResource(path);
        } else {
            mToast.setText("未找到语音文件");
            mToast.show();
        }


//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.prepareAsync();
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });

    }

    @Override
    public void onGradeChanged(int position, int grade) {
        mDatabaseOperator.updateNoteListDate(position, grade);
        //为了保持展开状态
        mNoteListDataList.get(position).setState(true);
    }
}
