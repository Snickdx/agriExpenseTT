package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Nicholas on 29/11/2017.
 */

public abstract class SQLiteDBModel implements DBModel, BaseColumns {


    protected abstract ContentValues getContentValues();
    protected abstract String getTable();
    protected abstract SQLiteDatabase getDB();
    protected abstract String getID();
    protected abstract String getName();
    protected abstract String getType();

    private int getLast(SQLiteDatabase db, String table){
        String code="select _id from " + table + "  ORDER BY _id DESC LIMIT 1;";
        Cursor cursor=db.rawQuery(code, null);
        if(cursor.getCount() < 0)return -1;
        cursor.moveToFirst();
        int res = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return res;
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
        db.delete(getName(), getID()+"="+rId, null);
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
    public DBModel get() {

        return null;
    }

    @Override
    public SQLiteDBModel getAll() {
        return null;
    }
}
