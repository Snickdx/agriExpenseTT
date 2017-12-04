package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class CountryContract extends SQLiteDBModel{


    public CountryContract(SQLiteDatabase db){
        this.db = db;
    }

    public CountryContract(SQLiteDatabase db, String name, String type){
        this.db = db;
        this.name = name;
        this.type = type;
    }

    protected String name;
    protected String type;
    protected SQLiteDatabase db;

    public final static String table = "countries";

    @Override
    protected ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("country", name);
        cv.put("subdivision", type);
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

    public static String [][] getCountries(){
        String [][] countries = {
                {"Anguilla", "district"},
                {"Antigua & Barbuda", "parish"},
                {"Bahamas", "island"},
                {"Barbados", "parish"},
                {"Belize","district"},
                {"British Virgin Islands","island"},
                {"Cayman Islands","island"},
                {"Dominica", "parish"},
                {"Grenada", "parish"},
                {"Guyana","region"},
                {"Haiti","district"},
                {"Jamaica", "parish"},
                {"Montserrat","parish"},
                {"St Kitts & Nevis","parish"},
                {"St Lucia","parish"},
                {"St Vincent and the Grenadines", "parish"},
                {"Suriname", "district"},
                {"Trinidad and Tobago","county"},
                {"Turks & Caicos Islands", "island"}
        };
        return countries;
    }

    @Override
    public int  init(){
        db.execSQL("CREATE TABLE IF NOT EXISTS countries ( "+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, country TEXT, subdivision TEXT)");
        return 0;
    }
}
