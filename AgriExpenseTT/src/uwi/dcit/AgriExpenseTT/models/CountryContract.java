package uwi.dcit.AgriExpenseTT.models;

import android.provider.BaseColumns;

public class CountryContract {
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_COUNTRIES = 
			"CREATE TABLE IF NOT EXISTS "+CountryEntry.TABLE_NAME + " ( "+
			CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT "+ COMMA_SEP +
			CountryEntry.COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP + 
			CountryEntry.COLUMN_NAME_TYPE + TEXT_TYPE + " )";
	
	public static final String SQL_DELETE_COUNTRIES = 
		"DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME;

	public static final String [][] countries = {
		{"Anguilla", "district"},
		{"Antigua & Barbuda", "parish"}, 
		{"Bahamas", "island"},
		{"Barbados", "parish"},
		{"Belize","district"},
		{"British Virgin Islands","island"},
		{"Cayman Islands","island"},
		{"Dominica", "parish"},
		{"Grenada", "parish"},
		{"Guyana","region"},
		{"Haiti","district"},
		{"Jamaica", "parish"},
		{"Montserrat","parish"},
		{"St Kitts & Nevis","parish"},
		{"St Lucia","parish"},
		{"St Vincent and the Grenadines", "parish"},
		{"Suriname", "district"},
		{"Trinidad and Tobago","county"},
		{"Turks & Caicos Islands", "island"}
	};

	public static abstract class CountryEntry implements BaseColumns{
		public static final String TABLE_NAME = "countries";
		public static final String COLUMN_NAME_COUNTRY = "country ";
		public static final String COLUMN_NAME_TYPE = "subdivision ";
		
	}
}
