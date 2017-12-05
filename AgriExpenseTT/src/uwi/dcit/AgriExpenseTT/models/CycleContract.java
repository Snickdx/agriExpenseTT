package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import uwi.dcit.AgriExpenseTT.helpers.DbQuery;

public class CycleContract extends SQLiteDBModel {

    public static String table ="cropCycle";
    private SQLiteDatabase db;
    private String land;
    private double landQty;
    private long date;
    private String name;
    private String crop;

    public CycleContract(SQLiteDatabase db) {
        this.db = db;
    }

    public CycleContract(SQLiteDatabase db, String crop, String land, double landQty, long date, String name){
        this.db= db;
        this.crop = crop;
        this.land = land;
        this.landQty = landQty;
        this.date  = date;
        this.name = name;

    }


    @Override
    public int init(){
        db.execSQL("CREATE TABLE IF NOT EXISTS cropCycle(" +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, cropid INTEGER , cyclename TEXT, landType TEXT, landAmt INTEGER, cycleDate INTEGER, tspent REAL, hAmt REAL, hType TEXT, costPer REAL, cropName TEXT, closed TEXT, foreign key(cropid) references "+ ResourceContract.table+" ("+ ResourceContract._ID+"));");
        return 0;
    }

    @Override
    public ContentValues getContentValues() {

        ContentValues cv = new ContentValues();
        cv.put("cropId", DbQuery.getNameResourceId(db, crop));
        cv.put("croptName", crop);
        cv.put("landType",land);
        cv.put("landAmt", landQty);
        cv.put("cycledate", date);
        cv.put("cyclename", name);
        return cv;
    }

    @Override
    protected String getTable() {
        return null;
    }

    @Override
    protected SQLiteDatabase getDB() {
        return null;
    }

    @Override
    protected String getName() {
        return null;
    }


    public static abstract class CycleEntry implements BaseColumns{
        public static final String TABLE_NAME="cropCycle";
        public static final String CROPCYCLE_CROPID="cropId";
        public static final String CROPCYCLE_LAND_TYPE="landType";
        public static final String CROPCYCLE_LAND_AMOUNT="landAmt";
        public static final String CROPCYCLE_DATE="cycledate";
        public static final String CROPCYCLE_TOTALSPENT="tspent";
        public static final String CROPCYCLE_HARVEST_TYPE="hType";
        public static final String CROPCYCLE_HARVEST_AMT="hAmt";
        public static final String CROPCYCLE_COSTPER="costPer";
        public static final String CROPCYCLE_COUNTY="county";
        public static final String CROPCYCLE_RESOURCE="cropName";
        public static final String CROPCYCLE_NAME="cyclename";
        public static final String CROPCYCLE_CLOSED="closed";
    }
}
