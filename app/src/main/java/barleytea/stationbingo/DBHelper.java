package barleytea.stationbingo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "station_bingo.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DBContract.DBTable.TABLE_NAME + " (" +
                    DBContract.DBTable._ID + " INTEGER PRIMARY KEY," +
                    DBContract.DBTable.COLUMN_NAME_ALPHABET + " TEXT," +
                    DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " INTEGER)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DBContract.DBTable.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
