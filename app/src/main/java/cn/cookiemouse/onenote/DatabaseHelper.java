package cn.cookiemouse.onenote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iflytek.cloud.thirdparty.V;

/**
 * Created by cookiemouse on 17-6-21.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;

    private static final String DATABASE_TABLE = "note_list_table";

    //  path相当于主键，具有唯一性
    private static final String CREATE_TABLE = "create table " +
            DATABASE_TABLE +
            "(text TEXT,path TEXT,type INTEGER,grade INTEGER, time TEXT)";

    public DatabaseHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate: --> 1");
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.i(TAG, "onCreate: --> 2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 升级数据库
    }


}
