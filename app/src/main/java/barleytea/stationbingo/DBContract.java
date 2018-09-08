package barleytea.stationbingo;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract() {}

    public static class DBTable implements BaseColumns {
        public static final String TABLE_NAME = "station_bingo_table";
        public static final String COLUMN_NAME_ALPHABET = "alphabet_col";
        public static final String COLUMN_NAME_ALPHABET_USED = "alphabet_used_col";
    }
}
