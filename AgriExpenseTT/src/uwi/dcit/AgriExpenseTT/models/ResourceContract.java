package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ResourceContract extends Contract{
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";

	public static final String SQL_CREATE_RESOURCE = "CREATE TABLE IF NOT EXISTS "+ResourceEntry.TABLE_NAME+"("
			+ResourceEntry._ID+" integer primary key autoincrement,"
			+ResourceEntry.RESOURCES_NAME + TEXT_TYPE + COMMA_SEP
			+ResourceEntry.RESOURCES_TYPE + TEXT_TYPE +");";

	public static final String SQL_DELETE_RESOURCE = "DROP TABLE IF EXISTS "+ ResourceEntry.TABLE_NAME;

	private SQLiteDatabase db;
	private String name;
	private String type;
	private String table;

	public ResourceContract(SQLiteDatabase db, String name, String type){
		this.db = db;
		this.name = name;
		this.type = type;
		this.table = "resources";
	}

    public ResourceContract(SQLiteDatabase db){
        this.db = db;
        this.table = "resources";
    }

	@Override
	protected ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("type", type);
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
