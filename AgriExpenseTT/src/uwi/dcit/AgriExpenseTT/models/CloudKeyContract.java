package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CloudKeyContract extends SQLiteDBModel{

    public static String table = "cloudKey";
    public static String cloudKey = "key";
    private String ctable;
    private String name;
    private int id;

    private SQLiteDatabase db;

    public CloudKeyContract(SQLiteDatabase db) {
        this.db = db;
    }

    public CloudKeyContract(SQLiteDatabase db, String k, int id, String table) {
        this.name = k;
        this.id = id;
        this.db = db;
        this.ctable = table;
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("ctable", ctable);
        cv.put("key", name);
        cv.put("rowid", id);
        return cv;
    }

    @Override
    protected String getTable() {
        return table;
    }

    @Override
    protected SQLiteDatabase getDB() {
        return db;
    }

    @Override
    protected String getName() {
        return name;
    }

    @Override
    public int init(){
        db.execSQL("CREATE TABLE IF NOT EXISTS cloudKey("+_ID + " integer primary key autoincrement, `key` TEXT, `rowid` INTEGER, `ctable` TEXT)");
        return 0;
    }

}
