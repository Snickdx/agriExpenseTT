package uwi.dcit.AgriExpenseTT.models;

/**
 * Created by Nicholas on 30/11/2017.
 */

public interface DBModel {
    public abstract int insert();
    public abstract void backup();
    public abstract void deleteBackup();
    public abstract int delete(int rId);
    public abstract void flush();
    public abstract int init();
    public abstract DBModel get();
    public abstract DBModel getAll();
}

