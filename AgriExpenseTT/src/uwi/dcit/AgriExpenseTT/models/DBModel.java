package uwi.dcit.AgriExpenseTT.models;

import android.database.Cursor;

/**
 * Created by Nicholas on 30/11/2017.
 */

public interface DBModel {
    public abstract int insert();
    public abstract void backup();
    public abstract Cursor getByName(String name);
    public abstract Cursor existsByName(String name);
    public abstract void deleteBackup();
    public abstract int delete(int rId);
    public abstract void flush();
    public abstract int init();
    public abstract DBModel get();
    public abstract DBModel getAll();
}

