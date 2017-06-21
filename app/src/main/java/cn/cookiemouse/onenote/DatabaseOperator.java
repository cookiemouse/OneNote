package cn.cookiemouse.onenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.onenote.data.NoteListData;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class DatabaseOperator {

    private static final String TAG = "DatabaseOperator";

    private static final String DB_NAME = "note_list.db";
    private static final String DB_TABLE = "note_list_table";

    private SQLiteDatabase mSqLiteDatabase;

    public DatabaseOperator(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, DB_NAME);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    // 增
    public void addNoteList(String text, String path, int type, int grade) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);
        contentValues.put("path", path);
        contentValues.put("type", type);
        contentValues.put("grade", grade);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(DB_TABLE, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }


        Log.i(TAG, "addNoteList: --> " + "加一条目 ");
    }

    // 查
    public List<NoteListData> getNoteList() {
        List<NoteListData> noteListDataList = new ArrayList<>();

        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(DB_TABLE
                    , new String[]{"text, path, type, grade"}
                    , "path=?"
                    , new String[]{"122"}
                    , null, null, null);

            Log.i(TAG, "getNoteList: cursor.move 1-->" + cursor.moveToFirst());
            while (cursor.moveToNext()) {
                Log.i(TAG, "getNoteList: --> while");

                String text = cursor.getString(0);
                String path = cursor.getString(1);
                int type = cursor.getInt(2);
                int grade = cursor.getInt(3);

                noteListDataList.add(new NoteListData(text, path, type, grade));
            }

            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }


        Log.i(TAG, "getNoteList: --> " + noteListDataList.size());
        return noteListDataList;
    }

    // 删
    public void removeNoteListData(int position) {
        List<NoteListData> noteListDataList = getNoteList();
        if (position >= noteListDataList.size()) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(DB_TABLE
                    , "path=?"
                    , new String[]{noteListDataList.get(position).getPath()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    // 改
    public void updateNoteListDate(int position, String text, String path, int type, int grade) {
        List<NoteListData> noteListDataList = getNoteList();
        if (position >= noteListDataList.size()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);
        contentValues.put("path", path);
        contentValues.put("type", type);
        contentValues.put("grade", grade);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(DB_TABLE
                    , contentValues
                    , "path=?"
                    , new String[]{noteListDataList.get(position).getPath()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    // 关闭
    public void closeDB() {
        mSqLiteDatabase.close();
    }
}
