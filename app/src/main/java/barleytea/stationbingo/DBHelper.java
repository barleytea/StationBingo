package barleytea.stationbingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "station_bingo.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DBContract.DBTable.TABLE_NAME + " (" +
                    DBContract.DBTable._ID + " INTEGER PRIMARY KEY," +
                    DBContract.DBTable.COLUMN_NAME_ALPHABET + " TEXT," +
                    DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " INTEGER)";

    private static final String SQL_DELETE_UNNECESSARY_ALPHABETS =
            "DELETE FROM " + DBContract.DBTable.TABLE_NAME +
            " WHERE " + DBContract.DBTable.COLUMN_NAME_ALPHABET + " NOT IN (" +
                    StringUtils.repeat("?", Constants.ALPHABET_SET.size()) + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion >= 2) {
            deleteUnnecessaryAlphabetsFromDB(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void deleteUnnecessaryAlphabetsFromDB(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_UNNECESSARY_ALPHABETS,
                Constants.ALPHABET_SET.toArray(new String[Constants.ALPHABET_SET.size()]));
    }

    private void insertInitialData(SQLiteDatabase db) {
        final ContentValues values = new ContentValues();
        Constants.ALPHABET_SET.forEach(alphabet -> {
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET, alphabet);
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, 0);
            db.insert(DBContract.DBTable.TABLE_NAME, null, values);
        });
    }
}
