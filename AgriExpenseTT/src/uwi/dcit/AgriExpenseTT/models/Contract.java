package uwi.dcit.AgriExpenseTT.models;


import android.database.sqlite.SQLiteDatabase;


public abstract class Contract extends SQLiteDBModel{

    protected String name;
    protected String type;
    protected String table;
    protected SQLiteDatabase db;

}
