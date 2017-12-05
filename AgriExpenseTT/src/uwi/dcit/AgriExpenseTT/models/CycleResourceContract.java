package uwi.dcit.AgriExpenseTT.models;

import android.provider.BaseColumns;

public class CycleResourceContract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_CYCLE_RESOURCE = "CREATE TABLE IF NOT EXISTS "
			+CycleResourceEntry.TABLE_NAME + "("
			+CycleResourceEntry._ID+" integer primary key autoincrement,"
			+CycleResourceEntry.CYCLE_RESOURCE_CYCLEID + INT_TYPE + COMMA_SEP
			+CycleResourceEntry.CYCLE_RESOURCE_PURCHASE_ID + INT_TYPE + COMMA_SEP
			+CycleResourceEntry.CYCLE_RESOURCE_TYPE + TEXT_TYPE + COMMA_SEP
//			+CycleResourceEntry.CYCLE_NAME + TEXT_TYPE + COMMA_SEP
			+CycleResourceEntry.CYCLE_RESOURCE_QTY + INT_TYPE + COMMA_SEP
			+CycleResourceEntry.CYCLE_RESOURCE_USECOST + " real" + COMMA_SEP
			+CycleResourceEntry.CYCLE_RESOURCE_QUANTIFIER + TEXT_TYPE + COMMA_SEP
            +CycleResourceEntry.CYCLE_DATE_USED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + COMMA_SEP
			+"foreign key("+CycleResourceEntry.CYCLE_RESOURCE_CYCLEID+") references "+ CycleContract.table+"("+ CycleContract.CycleEntry._ID+"),"
			+"foreign key("+CycleResourceEntry.CYCLE_RESOURCE_PURCHASE_ID+") references "+ ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME+"("+ ResourcePurchaseContract.ResourcePurchaseEntry._ID+"))";
		
	
	public static final String SQL_DELETE_CYCLE_RESOURCE =
			"DROP TABLE IF EXISTS "+ CycleResourceEntry.TABLE_NAME;
	
	public static abstract class CycleResourceEntry implements BaseColumns{
		public static final String TABLE_NAME="cycleResources";
//		public static final String CYCLE_NAME="cycleName";
		public static final String CYCLE_RESOURCE_PURCHASE_ID="pId";
		public static final String CYCLE_RESOURCE_TYPE="type";
		public static final String CYCLE_RESOURCE_QTY="qty";
		public static final String CYCLE_RESOURCE_QUANTIFIER="quantifier";
		public static final String CYCLE_RESOURCE_CYCLEID="cycleId";
		public static final String CYCLE_RESOURCE_USECOST="useCost";
        public static final String CYCLE_DATE_USED="dateused";
	}
}
