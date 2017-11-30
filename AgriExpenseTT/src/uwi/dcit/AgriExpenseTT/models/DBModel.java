package uwi.dcit.AgriExpenseTT.models;

/**
 * Created by Nicholas on 30/11/2017.
 */

public interface DBModel {
    public abstract int insert();
    public abstract int delete();
    public abstract int create();
    public abstract DBModel get();
    public abstract DBModel getAll();
}

