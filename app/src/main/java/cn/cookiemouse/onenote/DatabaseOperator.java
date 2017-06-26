package cn.cookiemouse.onenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.onenote.data.NoteListData;
import cn.cookiemouse.onenote.utils.TimeFormat;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class DatabaseOperator {

    private static final String TAG = "DatabaseOperator";

    private static final String DB_NAME = "note_list.db";
    private static final String DB_TABLE = "note_list_table";

    private SQLiteDatabase mSqLiteDatabase;

    private OnDataChangedListener mOnDataChangedListener;

    public DatabaseOperator(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, DB_NAME);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();
        mOnDataChangedListener = (OnDataChangedListener) context;
    }

    // 增
    public void addNoteList(String text, String path, int type, int grade) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);
        contentValues.put("path", path);
        contentValues.put("type", type);
        contentValues.put("grade", grade);
        String time = Long.toString(System.currentTimeMillis());
        contentValues.put("time", time);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(DB_TABLE, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

        if (null == mOnDataChangedListener){
            throw new NullPointerException("OnDataChangedListener is null");
        }
        mOnDataChangedListener.onDataChanged();
    }

    // 查
    public List<NoteListData> getNoteList() {
        List<NoteListData> noteListDataList = new ArrayList<>();

        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(DB_TABLE
                    , new String[]{"text, path, type, grade, time"}
                    , null
                    , null
                    , null, null, null);

            while (cursor.moveToNext()) {
                String text = cursor.getString(0);
                String path = cursor.getString(1);
                int type = cursor.getInt(2);
                int grade = cursor.getInt(3);
                String time = cursor.getString(4);
                time = new TimeFormat().millisToDate(time);

                noteListDataList.add(new NoteListData(text, path, type, grade, time));
            }

            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

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
                    , "text=?"
                    , new String[]{noteListDataList.get(position).getText()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

        if (null == mOnDataChangedListener){
            throw new NullPointerException("OnDataChangedListener is null");
        }
        mOnDataChangedListener.onDataChanged();
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
                    , "text=?"
                    , new String[]{noteListDataList.get(position).getText()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

        if (null == mOnDataChangedListener){
            throw new NullPointerException("OnDataChangedListener is null");
        }
        mOnDataChangedListener.onDataChanged();
    }

    // 改，重载
    public void updateNoteListDate(int position, String text) {
        List<NoteListData> noteListDataList = getNoteList();
        if (position >= noteListDataList.size()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(DB_TABLE
                    , contentValues
                    , "text=?"
                    , new String[]{noteListDataList.get(position).getText()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

        if (null == mOnDataChangedListener){
            throw new NullPointerException("OnDataChangedListener is null");
        }
        mOnDataChangedListener.onDataChanged();
    }

    // 改
    public void updateNoteListDate(int position, int grade) {
        List<NoteListData> noteListDataList = getNoteList();
        if (position >= noteListDataList.size()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("grade", grade);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(DB_TABLE
                    , contentValues
                    , "text=?"
                    , new String[]{noteListDataList.get(position).getText()});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }

        if (null == mOnDataChangedListener){
            throw new NullPointerException("OnDataChangedListener is null");
        }
        mOnDataChangedListener.onDataChanged();
    }

    // 关闭
    public void closeDB() {
        mSqLiteDatabase.close();
    }

    public interface OnDataChangedListener{
        void onDataChanged();
    }
}
