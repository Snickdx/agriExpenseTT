package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ResourceContract extends Contract{


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
	protected String getID() {
		return _ID;
	}

	@Override
	protected String getName() {
		return this.name;
	}

	@Override
	protected String getType() {
	    return this.type;
	}


	public static abstract class ResourceEntry implements BaseColumns{
		public static final String TABLE_NAME = "resources";
		public static final String RESOURCES_NAME = "name";
		public static final String RESOURCES_TYPE = "type";
	}
}
