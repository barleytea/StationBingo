package barleytea.stationbingo;

import android.database.sqlite.SQLiteDatabase;

public interface DBProcess<T> {
    T process(SQLiteDatabase db);
}

