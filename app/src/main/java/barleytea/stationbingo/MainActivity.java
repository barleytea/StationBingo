package barleytea.stationbingo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper = null;
    private TextView setViewInitialString;
    private TextView setStationNameView;
    private TextView setStationNameKanaView;

    @State CharSequence initialString = null;
    @State CharSequence stationName = null;
    @State CharSequence stationNameKana = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Icepick.restoreInstanceState(this, savedInstanceState);

        setViewInitialString = findViewById(R.id.alphabet_text);
        setStationNameView = findViewById(R.id.station_name);
        setStationNameKanaView = findViewById(R.id.station_name_kana);

        setViewInitialString.setText(initialString);
        setStationNameView.setText(stationName);
        setStationNameKanaView.setText(stationNameKana);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        initialString = setViewInitialString.getText();
        stationName = setStationNameView.getText();
        stationNameKana = setStationNameKanaView.getText();
        Icepick.saveInstanceState(this, outState);
    }

    public void extractAlphabet(View view) {
        List<String> selectableAlphabets = getSelectableAlphabets();

        if (selectableAlphabets.size() == 0) {
            setViewInitialString.setText("You have completed BINGO!");
            return;
        }

        Collections.shuffle(selectableAlphabets);
        final String selectionArg = selectableAlphabets.get(0);

        final String selection = DBContract.DBTable.COLUMN_NAME_ALPHABET + " = ?";
        final String updateValue = "1";
        DBUtil.write(getApplicationContext(), db -> {
            StationBingoModel.update(db, selection, selectionArg, updateValue);
            return true;
        });

        initialString = selectionArg;
        setViewInitialString.setText(selectionArg);

        setStationNameOnView(selectionArg);
    }

    private boolean setStationNameOnView(String initial) {
        CsvReader parser = new CsvReader();
        parser.reader(getApplicationContext());

        StationSelector stationSelector = new StationSelector(parser.objects);


        StationData selectedStationData = stationSelector.selectStation(initial);
        if (selectedStationData == null) {
            setStationNameView.setText("no stations found");
            setStationNameKanaView.setText("no stations found");
            return false;
        } else {
            stationName = selectedStationData.getStationName();
            stationNameKana = selectedStationData.getStationNameKana();
            setStationNameView.setText(stationName);
            setStationNameKanaView.setText(stationNameKana);
            return true;
        }
    }

    private List<String> getSelectableAlphabets() {
        final String selectionArg = "0";
        final String selection = DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " = ?";
        final List<String> res =
                DBUtil.read(getApplicationContext(), db -> StationBingoModel.list(db, selection, selectionArg))
                        .stream()
                        .map(x -> x.alphabet)
                        .collect(Collectors.toList());
        return res;
    }

    public void clearHistory(View view) {
        final String updateValue = "0";
        final String updateSelection = DBContract.DBTable.COLUMN_NAME_ALPHABET_USED + " = ?";
        final String updateSelectionArg = "1";

        DBUtil.write(getApplicationContext(), db -> {
            StationBingoModel.update(db, updateSelection, updateSelectionArg, updateValue);
            return true;
        });

        clearAlphabetText();
    }

    private void clearAlphabetText() {
        TextView setViewAlp = findViewById(R.id.alphabet_text);
        TextView setViewStationName = findViewById(R.id.station_name);
        TextView setViewStationNameKana = findViewById(R.id.station_name_kana);

        setViewAlp.setText("");
        setViewStationName.setText("");
        setViewStationNameKana.setText("");
    }

    @Override
    protected void onDestroy() {
        if(dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}
