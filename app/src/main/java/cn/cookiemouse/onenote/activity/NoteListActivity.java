package cn.cookiemouse.onenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.onenote.DatabaseOperator;
import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.SROperator;
import cn.cookiemouse.onenote.adapter.NoteListAdapter;
import cn.cookiemouse.onenote.data.DataGrade;
import cn.cookiemouse.onenote.data.DataType;
import cn.cookiemouse.onenote.data.NoteListData;
import cn.cookiemouse.onenote.fragment.RecordingFragment;

public class NoteListActivity extends AppCompatActivity implements DatabaseOperator.OnDataChangedListener
        , NoteListAdapter.OnControlListener {

    private static final String TAG = "NoteListActivity";

    private ListView mListViewNote;
    private LinearLayout mButtonListen;

    private SROperator mSrOperator;

    private List<NoteListData> mNoteListDataList;
    private NoteListAdapter adapter;

    private DatabaseOperator mDatabaseOperator;

    private RecordingFragment mRecordingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initView();

        setEventListener();
    }

    private void initView() {
        mListViewNote = (ListView) findViewById(R.id.lv_activity_notelist);
        mButtonListen = (LinearLayout) findViewById(R.id.btn_activity_listen);

        mSrOperator = new SROperator(this);

        mDatabaseOperator = new DatabaseOperator(this);

        mNoteListDataList = new ArrayList<>();

        adapter = new NoteListAdapter(NoteListActivity.this, mNoteListDataList);

        mRecordingFragment = new RecordingFragment();

        for (NoteListData data : mDatabaseOperator.getNoteList()) {
            mNoteListDataList.add(data);
        }

        mListViewNote.setAdapter(adapter);
    }

    private void setEventListener() {
        mButtonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // : 17-6-21 录制按钮事件
                mSrOperator.startListen();
                mRecordingFragment.show(getSupportFragmentManager(), "RecordingFragment");
            }
        });

        mSrOperator.setOnResultListener(new SROperator.OnResultListener() {
            @Override
            public void onResult(String result) {
                Log.i(TAG, "onResult: -->" + result);
                if ("".equals(result)){
                    Toast.makeText(NoteListActivity.this, "您好像没有说话", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDatabaseOperator.addNoteList(result
                        , ""
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
        mDatabaseOperator.removeNoteListData(position);
    }

    @Override
    public void onEdit(int position) {
        Log.i(TAG, "onEdit: ");
    }

    @Override
    public void onPlay(int position) {
        Log.i(TAG, "onPlay: ");
    }

    @Override
    public void onGradeChanged(int position, int grade) {
        mDatabaseOperator.updateNoteListDate(position, grade);
        //为了保持展开状态
        mNoteListDataList.get(position).setState(true);
    }
}
