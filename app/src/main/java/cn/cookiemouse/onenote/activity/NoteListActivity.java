package cn.cookiemouse.onenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class NoteListActivity extends AppCompatActivity implements DatabaseOperator.OnDataChangedListener
        , NoteListAdapter.OnControlListener{

    private static final String TAG = "NoteListActivity";

    private ListView mListViewNote;
    private Button mButtonListen;

    private SROperator mSrOperator;

    private List<NoteListData> mNoteListDataList;
    private NoteListAdapter adapter;

    private DatabaseOperator mDatabaseOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initView();

        setEventListener();
    }

    private void initView() {
        mListViewNote = (ListView) findViewById(R.id.lv_activity_notelist);
        mButtonListen = (Button) findViewById(R.id.btn_activity_listen);

        mSrOperator = new SROperator(this);

        mDatabaseOperator = new DatabaseOperator(this);

        mNoteListDataList = new ArrayList<>();

        adapter = new NoteListAdapter(NoteListActivity.this, mNoteListDataList);

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
            }
        });

        mSrOperator.setOnResultListener(new SROperator.OnResultListener() {
            @Override
            public void onResult(String result) {
                mDatabaseOperator.addNoteList(result
                        , ""
                        , DataType.TYPE_PERSONAL
                        , DataGrade.GRADE_NORMAL);
                Toast.makeText(NoteListActivity.this, result, Toast.LENGTH_SHORT).show();
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
}
