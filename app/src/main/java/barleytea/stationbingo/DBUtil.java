package barleytea.stationbingo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil {
    public static <T> T read(Context context, DBProcess<T> handler) {
        T obj = null;
        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        try {
            obj = handler.process(db);
        }
        finally {
            db.close();
        }
        return obj;
    }

    public static void write(Context context, DBProcess<Object> handler) {
        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        try {
            handler.process(db);
        }
        finally {
            db.close();
        }
    }
}
