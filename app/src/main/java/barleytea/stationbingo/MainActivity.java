package barleytea.stationbingo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        // Initial Data Insert
        final ContentValues values = new ContentValues();
        final SQLiteDatabase writer = dbHelper.getWritableDatabase();
        for (final String alphabet : alphabetSet) {
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET, alphabet);
            values.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, 0);
            writer.insert(DBContract.DBTable.TABLE_NAME, null, values);
        }
    }

    public void extractAlphabet(View view) {
        TextView setView = findViewById(R.id.alphabet_text);

        final SQLiteDatabase writer = dbHelper.getWritableDatabase();
        List<String> selectableAlphabets = getSelectableAlphabets();

        if (selectableAlphabets.size() == 0) {
            setView.setText("You have completed BINGO!");
            return;
        }

        Collections.shuffle(selectableAlphabets);
        final String selected = selectableAlphabets.get(0);

        ContentValues updateValues = new ContentValues();
        updateValues.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, 1);
        String updateSelection = DBContract.DBTable.COLUMN_NAME_ALPHABET + " = ?";
        String[] updateSelectionArgs = {selected};
        writer.update(
                DBContract.DBTable.TABLE_NAME,
                updateValues,
                updateSelection,
                updateSelectionArgs
        );

        setView.setText(selected);

        setStationNameOnView(selected);
    }

    private void setStationNameOnView(String initial) {
        CsvReader parser = new CsvReader();
        parser.reader(getApplicationContext());

        StationSelector stationSelector = new StationSelector(parser.objects);
        TextView setStationNameView = findViewById(R.id.station_name);
        TextView setStationNameKanaView = findViewById(R.id.station_name_kana);

        StationData selectedStationData = stationSelector.selectStation(initial);
        if (selectedStationData != null) {
            setStationNameView.setText(selectedStationData.getStationName());
            setStationNameKanaView.setText(selectedStationData.getStationNameKana());
        } else {
            setStationNameView.setText("no stations found");
            setStationNameKanaView.setText("no stations found");
        }
    }

    private List<String> getSelectableAlphabets() {
        final List<String> res = new ArrayList<>();
        final SQLiteDatabase reader = dbHelper.getReadableDatabase();
        final String[] projection = {
                DBContract.DBTable.COLUMN_NAME_ALPHABET
        };
        final String selection = DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " = ?";
        final String[] selectionArgs = {"0"};
        final String sortOrder = DBContract.DBTable.COLUMN_NAME_ALPHABET + " DESC";
        final Cursor cursor = reader.query(
                DBContract.DBTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String alphabet = cursor.getString(cursor.getColumnIndex(DBContract.DBTable.COLUMN_NAME_ALPHABET));
            res.add(alphabet);
        }
        cursor.close();
        return res;
    }

    public void clearHistory(View view) {
        final SQLiteDatabase writer = dbHelper.getWritableDatabase();

        ContentValues updateValues = new ContentValues();
        updateValues.put(DBContract.DBTable.COLUMN_NAME_ALPHABET_USED, 0);
        String updateSelection = DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " = ?";
        String[] updateSelectionArgs = {"1"};
        writer.update(
                DBContract.DBTable.TABLE_NAME,
                updateValues,
                updateSelection,
                updateSelectionArgs
        );
        clearAlphabetText(view);
    }

    private void clearAlphabetText(View view) {
        TextView setView = findViewById(R.id.alphabet_text);
        setView.setText("");
    }

    private static Set<String> alphabetSet = new HashSet<>(
            Arrays.asList(
                    "あ","い","う","え","お",
                    "か","き","く","け","こ",
                    "さ","し","す","せ","そ",
                    "た","ち","つ","て","と",
                    "な","に","ぬ","ね","の",
                    "は","ひ","ふ","へ","ほ",
                    "ま","み","む","め","も",
                    "や","ゆ","よ",
                    "ら","り","る","れ","ろ",
                    "わ",
                    "が","ぎ","ぐ","げ","ご",
                    "ざ","じ","ず","ぜ","ぞ",
                    "だ","ぢ","づ","で","ど",
                    "ば","び","ぶ","べ","ぼ",
                    "ぱ","ぴ","ぷ","ぺ","ぽ"
            )
    );

    @Override
    protected void onDestroy() {
        if(dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }

}
