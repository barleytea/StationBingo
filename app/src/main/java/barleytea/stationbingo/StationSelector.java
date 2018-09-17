package barleytea.stationbingo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StationSelector {

    private List<StationData> stationDataList;

    public StationSelector(List<StationData> stationDataList) {
        this.stationDataList = stationDataList;
    }

    public StationData selectStation(String initial) {
        final List<StationData> stationDataListWithGivenInitial = getStationDataListWithGivenInitial(initial);
        Collections.shuffle(stationDataListWithGivenInitial);
        if (stationDataListWithGivenInitial.size() == 0) {
            return null;
        }
        return stationDataListWithGivenInitial.get(0);
    }

    private List<StationData> getStationDataListWithGivenInitial(String initial) {
        final ArrayList<StationData> res = new ArrayList<>();
        // TO DO: change into more effective way
        for (StationData stationData : stationDataList) {
            if (stationData.getStationNameInitial().equals(initial)) {
                res.add(stationData);
            }
        }
        return res;
    }
}
