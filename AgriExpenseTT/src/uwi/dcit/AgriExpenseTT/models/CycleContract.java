package uwi.dcit.AgriExpenseTT.models;

import android.provider.BaseColumns;

public class CycleContract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER";
	private static final String REAL_TYPE= " REAL";
	private static final String COMMA_SEP = ",";
	
	public static final String SQL_CREATE_CYCLE = "CREATE TABLE IF NOT EXISTS "+CycleEntry.TABLE_NAME+"("
			+CycleEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
			+CycleEntry.CROPCYCLE_CROPID + INT_TYPE + COMMA_SEP
            +CycleEntry.CROPCYCLE_NAME + TEXT_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_LAND_TYPE + TEXT_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_LAND_AMOUNT + INT_TYPE + COMMA_SEP 
			+CycleEntry.CROPCYCLE_DATE + INT_TYPE + COMMA_SEP 
			+CycleEntry.CROPCYCLE_TOTALSPENT + REAL_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_HARVEST_AMT + REAL_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_HARVEST_TYPE + TEXT_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_COSTPER + REAL_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_RESOURCE + TEXT_TYPE + COMMA_SEP
			+CycleEntry.CROPCYCLE_CLOSED + TEXT_TYPE + COMMA_SEP
			+"foreign key("+CycleEntry.CROPCYCLE_CROPID+") references "+ ResourceContract.table+"("+ ResourceContract._ID+"));";
		
	
	public static final String SQL_DELETE_CYCLE = "DROP TABLE IF EXISTS " + CycleEntry.TABLE_NAME;
	
	public static abstract class CycleEntry implements BaseColumns{
		public static final String TABLE_NAME="cropCycle";
		public static final String CROPCYCLE_CROPID="cropId";
		public static final String CROPCYCLE_LAND_TYPE="landType";
		public static final String CROPCYCLE_LAND_AMOUNT="landAmt";
		public static final String CROPCYCLE_DATE="cycledate";
		public static final String CROPCYCLE_TOTALSPENT="tspent";
		public static final String CROPCYCLE_HARVEST_TYPE="hType";
		public static final String CROPCYCLE_HARVEST_AMT="hAmt";
		public static final String CROPCYCLE_COSTPER="costPer";
		public static final String CROPCYCLE_COUNTY="county";
		public static final String CROPCYCLE_RESOURCE="cropName";
        public static final String CROPCYCLE_NAME="cyclename";
		public static final String CROPCYCLE_CLOSED="closed";
	}
}
