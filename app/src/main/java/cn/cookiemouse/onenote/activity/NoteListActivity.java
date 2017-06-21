package cn.cookiemouse.onenote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.onenote.R;
import cn.cookiemouse.onenote.SROperator;
import cn.cookiemouse.onenote.adapter.NoteListAdapter;
import cn.cookiemouse.onenote.data.NoteListData;

public class NoteListActivity extends AppCompatActivity {

    private ListView mListViewNote;
    private Button mButtonListen;
    private SROperator mSrOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initView();
    }

    private void initView() {
        mListViewNote = (ListView) findViewById(R.id.lv_activity_notelist);
        mButtonListen = (Button) findViewById(R.id.btn_activity_listen);

        mSrOperator = new SROperator(this);

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
                Toast.makeText(NoteListActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });

        final List<NoteListData> mNoteListDataList = new ArrayList<>();
        mNoteListDataList.add(new NoteListData());
        mNoteListDataList.add(new NoteListData());
        mNoteListDataList.add(new NoteListData());
        mNoteListDataList.add(new NoteListData());
        mNoteListDataList.get(0).setState(true);
        mNoteListDataList.get(2).setState(true);
        final NoteListAdapter adapter = new NoteListAdapter(NoteListActivity.this, mNoteListDataList);
        mListViewNote.setAdapter(adapter);

        mListViewNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (NoteListData data : mNoteListDataList) {
                    data.setState(false);
                }
                mNoteListDataList.get(i).setState(true);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
