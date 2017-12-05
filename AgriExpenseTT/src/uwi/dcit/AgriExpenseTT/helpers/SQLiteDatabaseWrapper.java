package uwi.dcit.AgriExpenseTT.helpers;

import android.database.sqlite.SQLiteDatabase;

public class SQLiteDatabaseWrapper extends Database{

    private SQLiteDatabase db;

    public SQLiteDatabaseWrapper(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public SQLiteDatabase getDB() {
        return db;
    }
}
