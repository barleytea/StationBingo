package barleytea.stationbingo;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    final List<StationData> objects = new ArrayList<StationData>();
    public void reader(Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("station_tokyo.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StationData data = new StationData();
                final String[] rowData = line.split(",");
                data.setStationCd(rowData[0]);
                data.setStationGlobalCd(rowData[1]);
                data.setStationName(rowData[2]);
                data.setStationNameKana(rowData[3]);
                data.setStationNameInitial(rowData[4]);
                data.setLineCd(rowData[5]);
                data.setAdd(rowData[6]);
                data.setLon(rowData[7]);
                data.setLat(rowData[8]);
                data.setSort(rowData[9]);

                objects.add(data);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
