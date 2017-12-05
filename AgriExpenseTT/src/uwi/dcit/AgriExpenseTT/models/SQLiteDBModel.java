package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public abstract class SQLiteDBModel implements DBModel, BaseColumns {

    //might do command obj pattern to handle diff exports
    protected abstract ContentValues getContentValues();
    protected abstract String getTable();
    protected abstract SQLiteDatabase getDB();
    protected abstract String getName();


    private int getLast(SQLiteDatabase db, String table){
        String code="select _id from " + table + "  ORDER BY _id DESC LIMIT 1;";
        Cursor cursor=db.rawQuery(code, null);
        if(cursor.getCount() < 0)return -1;
        cursor.moveToFirst();
        int res = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return res;
    }

    protected String getID() {
        return _ID;
    }

    @Override
    public void backup(){
        SQLiteDatabase db = getDB();
        db.execSQL("ALTER TABLE " + getTable() + " RENAME TO " +getTable()+ "_orig");
    }

    @Override
    public void deleteBackup(){
        SQLiteDatabase db = getDB();
        db.execSQL("DROP TABLE IF EXISTS " +getTable()+ "_orig");
    }

    @Override
    public int insert() {
        SQLiteDatabase db = getDB();
        db.insert(getTable(), null, getContentValues());
        return getLast(db, getTable());
    }

    @Override
    public int delete(int rId) {
        SQLiteDatabase db = getDB();
        db.delete(getName(), _ID+"="+rId, null);
        return 0;
    }

    @Override
    public void flush(){
        SQLiteDatabase db = getDB();
        String sql = "DROP TABLE IF EXISTS "+ getTable();
        db.execSQL(sql);
    }

    @Override
    public int init() {
        SQLiteDatabase db = getDB();
        String sql = "CREATE TABLE IF NOT EXISTS "+getTable()+" (" +getID()+" integer primary key autoincrement, name TEXT , type TEXT);";
        db.execSQL(sql);
        return 0;
    }

    @Override
    public Cursor getByName(String name){
        SQLiteDatabase db = getDB();
        return db.rawQuery("select "+getID()+" from "+getTable()+" where name ='"+name+"';", null);
    }

    @Override
    public Cursor existsByName(String name){
        SQLiteDatabase db = getDB();
        return db.rawQuery("SELECT name from " + getTable()+  " WHERE LOWER( name ) LIKE '%"+name+"%';", null);
    }

    @Override
    public DBModel get() {

        return null;
    }

    @Override
    public SQLiteDBModel getAll() {
        return null;
    }
}
