package uwi.dcit.AgriExpenseTT.models;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ResourceContract extends Contract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_RESOURCE = "CREATE TABLE IF NOT EXISTS "+ResourceEntry.TABLE_NAME+"("
			+ResourceEntry._ID+" integer primary key autoincrement,"
			+ResourceEntry.RESOURCES_NAME + TEXT_TYPE + COMMA_SEP
			+ResourceEntry.RESOURCES_TYPE + TEXT_TYPE +");";
	
	public static final String SQL_DELETE_RESOURCE = "DROP TABLE IF EXISTS "+ ResourceEntry.TABLE_NAME;

	private SQLiteDatabase db;

	ResourceContract(SQLiteDatabase db){
		this.db = db;
	}


	@Override
	protected ContentValues getContentValues() {
		return null;
	}

	@Override
	protected String getTable() {
		return "resources";
	}

	@Override
	protected String getType() {
		return "type";
	}

	@Override
	protected String getName() {
		return "name";
	}

	@Override
	protected SQLiteDatabase getDB() {
		return this.db;
	}

    @Override
    protected int getID() {
        return 0;
    }


    public static abstract class ResourceEntry implements BaseColumns{
		public static final String TABLE_NAME = "resources";
		public static final String RESOURCES_NAME = "name";
		public static final String RESOURCES_TYPE = "type";
	}
}
