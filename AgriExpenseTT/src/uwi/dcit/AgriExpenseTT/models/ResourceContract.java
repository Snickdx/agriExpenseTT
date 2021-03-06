package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ResourceContract extends SQLiteDBModel{

	protected String name;
	protected String type;
	protected SQLiteDatabase db;

	public final static String table = "resources";

	public ResourceContract(SQLiteDatabase db, String name, String type){
		this.db = db;
		this.name = name;
		this.type = type;
	}

	public ResourceContract(SQLiteDatabase db){
	    this.db = db;
	}


	@Override
	protected ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("type", type);
		return cv;
	}

	@Override
	public String getTable() {
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

    protected String getType() {
        return type;
    }


}
