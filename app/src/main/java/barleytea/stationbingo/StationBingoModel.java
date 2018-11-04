package barleytea.stationbingo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class StationBingoModel {
    public int _id;
    public String alphabet;
    public String alphabetUsed;

    private StationBingoModel() {
        _id = -1;
    }

    private static StationBingoModel createUsingCursor(Cursor cursor, int offset) {
        StationBingoModel stationBingoModel = new StationBingoModel();
        stationBingoModel._id = cursor.getInt(offset);
        stationBingoModel.alphabet = cursor.getString(offset + 1);
        stationBingoModel.alphabetUsed = cursor.getString(offset + 2);
        return stationBingoModel;
    }

    public static List<StationBingoModel> list(SQLiteDatabase db, String selection, String selectionArg) {
        final List<StationBingoModel> stationBingoModels = new ArrayList<>();

        final String[] selectionArgs = {selectionArg};
        final String sortOrder = DBContract.DBTable.COLUMN_NAME_ALPHABET + " DESC";
        final Cursor cursor = db.query(
                DBContract.DBTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    stationBingoModels.add(createUsingCursor(cursor, 0));
                }
            }
            finally {
                cursor.close();
            }
        }

        return stationBingoModels;
    }

    public static StationBingoModel get(SQLiteDatabase db, String selection, String selectionArg) {
        StationBingoModel stationBingoModel = new StationBingoModel();

        final String[] projection = {
                DBContract.DBTable.COLUMN_NAME_ALPHABET
        };
        final String[] selectionArgs = {selectionArg};
        final String sortOrder = DBContract.DBTable.COLUMN_NAME_ALPHABET + " DESC";
        final Cursor cursor = db.query(
                DBContract.DBTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    stationBingoModel = createUsingCursor(cursor, 0);
                }
            }
            finally {
                cursor.close();
            }
        }

        return stationBingoModel;
    }

    public static void update(SQLiteDatabase db, String selection, String selectionArg, String updateValue) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, updateValue);
        String[] updateSelectionArgs = {selectionArg};
        db.update(
                DBContract.DBTable.TABLE_NAME,
                updateValues,
                selection,
                updateSelectionArgs
        );
    }

    public static void createTable(SQLiteDatabase db) {
        final ContentValues values = new ContentValues();
        Constants.ALPHABET_SET.forEach(x -> {
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET, x);
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, 0);
        });
        db.insert(DBContract.DBTable.TABLE_NAME, null, values);
    }
}
