package uwi.dcit.AgriExpenseTT.models;

import android.provider.BaseColumns;

public class ResourcePurchaseContract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	
	public static final String SQL_CREATE_RESOURCE_PURCHASE =
			"CREATE TABLE IF NOT EXISTS "+ResourcePurchaseEntry.TABLE_NAME+"("
			+ResourcePurchaseEntry._ID+ INT_TYPE + " primary key autoincrement,"
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_RESID + INT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_TYPE+ TEXT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_QUANTIFIER + TEXT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_QTY + INT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_REMAINING + INT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_COST+" REAL" + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_DATE+ INT_TYPE + COMMA_SEP
			+ResourcePurchaseEntry.RESOURCE_PURCHASE_RESOURCE + TEXT_TYPE + COMMA_SEP
			+"foreign key("+ResourcePurchaseEntry.RESOURCE_PURCHASE_RESID+") references "+ ResourceContract.table + "(" + ResourceContract._ID + "));";
	
	public static final String SQL_DELETE_RESOURCE_PURCHASE = 
			"DROP TABLE IF EXISTS " + ResourcePurchaseEntry.TABLE_NAME;

	public static abstract class ResourcePurchaseEntry implements BaseColumns{
		public static final String TABLE_NAME = "resPurchases";
//		public static final String RESOURCE_PURCHASE_ID = "id";
		public static final String RESOURCE_PURCHASE_RESID = "rId";
		public static final String RESOURCE_PURCHASE_TYPE = "type";
		public static final String RESOURCE_PURCHASE_QUANTIFIER = "quantifier";
		public static final String RESOURCE_PURCHASE_QTY = "qty";
		public static final String RESOURCE_PURCHASE_COST = "cost";
		public static final String RESOURCE_PURCHASE_REMAINING = "remaining";
		public static final String RESOURCE_PURCHASE_DATE = "purchasedate";
		public static final String RESOURCE_PURCHASE_RESOURCE = "resource";
	}
}
