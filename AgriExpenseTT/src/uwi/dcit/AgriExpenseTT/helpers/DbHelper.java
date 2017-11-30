package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uwi.dcit.AgriExpenseTT.models.CloudKeyContract;
import uwi.dcit.AgriExpenseTT.models.CountryContract;
import uwi.dcit.AgriExpenseTT.models.CountyContract;
import uwi.dcit.AgriExpenseTT.models.CycleContract;
import uwi.dcit.AgriExpenseTT.models.CycleResourceContract;
import uwi.dcit.AgriExpenseTT.models.CycleResourceContract.CycleResourceEntry;
import uwi.dcit.AgriExpenseTT.models.LabourContract;
import uwi.dcit.AgriExpenseTT.models.RedoLogContract;
import uwi.dcit.AgriExpenseTT.models.RedoLogContract.RedoLogEntry;
import uwi.dcit.AgriExpenseTT.models.ResourceContract;
import uwi.dcit.AgriExpenseTT.models.ResourcePurchaseContract;
import uwi.dcit.AgriExpenseTT.models.TransactionLogContract;
import uwi.dcit.AgriExpenseTT.models.TransactionLogContract.TransactionLogEntry;
import uwi.dcit.AgriExpenseTT.models.UpdateAccountContract;

public class DbHelper extends SQLiteOpenHelper{

	private static final int VERSION = 174;
	private static final String DATABASE_NAME = "agriDb";
	private static final String TAG_NAME = "AgriExpenseDBHelper";
	private Context ctx;
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null,VERSION);
		this.ctx = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
        Log.i(TAG_NAME, "Creating AgriExpense DB for first time");
		createDb(db);
		populate(db, new TransactionLog(this,db,ctx));
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//We will be required to implement upgrade functionality that is specific to each version of the upgrade
		Log.i(TAG_NAME, "Upgrade detected. Old version: "+ oldVersion + " New version: "+newVersion);

		if (oldVersion < 174){ // Made Changes that are incompatible and data will be dropped
			this.dropTables(db);
			this.onCreate(db);
		}

        // TODO Add logic to place the crop name as the cycle name for existing cycle records
        // TODO Add Date to CycleResource and place timestamp automatically for values

		if (oldVersion < 170){
			Log.d(TAG_NAME, "version too old to support Removing all tables so far and restart");
			this.dropTables(db);
			this.onCreate(db);
		}
        if (oldVersion  <= 171 && !columnExists(db, CycleContract.CycleEntry.TABLE_NAME, CycleContract.CycleEntry._ID)) {
			
			Log.d(TAG_NAME, "Running Update of table structure");
			this.tableColumnModify(db);
			
			Log.d(TAG_NAME, "Running installation of countries");
			this.createCountries(db); // Create if not exist so if previously created this will do nothing
			this.insertDefaultCountries(db);
			
			Log.d(TAG_NAME, "Running installation of counties");
			this.createCounties(db);
			this.insertDefaultCounties(db);
			
			Log.d(TAG_NAME, "Running upgrade of crop/plating material lists");
			this.updateCropList(db);
		}
        if (oldVersion < 172){
            db.beginTransaction();

            db.execSQL("ALTER TABLE " + CycleContract.CycleEntry.TABLE_NAME + " ADD COLUMN "+ CycleContract.CycleEntry.CROPCYCLE_NAME + " TEXT");
            // Place the resource name as the default name of the cycle
            updateCycleCropName(db);

            // Place the update date as the date for the previously created resources purchased
            updatePurchaseRecs(db);

            // Add Date Column to CycleResource
            db.execSQL("ALTER TABLE " + CycleResourceContract.CycleResourceEntry.TABLE_NAME + " ADD COLUMN "+ CycleResourceContract.CycleResourceEntry.CYCLE_DATE_USED +  " TIMESTAMP");
            updateCycleResource(db);

            db.setTransactionSuccessful();
            db.endTransaction();
        }
		
		Log.d(TAG_NAME, "Completed upgrading the database to version " + VERSION);

//        db.close();
	}


	//*************************** DB Stuff

    private void tableColumnModify(SQLiteDatabase db){
		// Using Transactions to help ensure some level of security
		db.beginTransaction();
		
		// Backup old data by renaming tables
		this.createBackup(db);
		
		// Create table with new structures
		this.createDb(db);
		
		// Translate the data from 
		this.translateData(db);

		//delete the existing old tables
		this.dropBackups(db);
		
		// mark the transaction as successful
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private void createBackup(SQLiteDatabase db){
		db.execSQL("ALTER TABLE " + ResourceContract.ResourceEntry.TABLE_NAME + " RENAME TO " + ResourceContract.ResourceEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + CycleContract.CycleEntry.TABLE_NAME + " RENAME TO " + CycleContract.CycleEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME + " RENAME TO " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + CycleResourceEntry.TABLE_NAME + " RENAME TO " + CycleResourceEntry.TABLE_NAME + "_orig");
		// db.execSQL("ALTER TABLE " + LabourEntry.TABLE_NAME + "RENAME TO " + LabourEntry.TABLE_NAME + "_orig");
		
		db.execSQL("ALTER TABLE " + CloudKeyContract.CloudKeyEntry.TABLE_NAME + " RENAME TO " + CloudKeyContract.CloudKeyEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + RedoLogEntry.TABLE_NAME + " RENAME TO " + RedoLogEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + TransactionLogEntry.TABLE_NAME + " RENAME TO " + TransactionLogEntry.TABLE_NAME + "_orig");
		db.execSQL("ALTER TABLE " + UpdateAccountContract.UpdateAccountEntry.TABLE_NAME + " RENAME TO " + UpdateAccountContract.UpdateAccountEntry.TABLE_NAME + "_orig");

		// db.execSQL("ALTER TABLE " + CountryEntry.TABLE_NAME + " RENAME TO " + CountryEntry.TABLE_NAME + "_orig");
		// db.execSQL("ALTER TABLE " + CountyEntry.TABLE_NAME + " RENAME TO " + CountyEntry.TABLE_NAME + "_orig");
	}
	
	private void translateData(SQLiteDatabase db){
		db.execSQL("INSERT INTO " + ResourceContract.ResourceEntry.TABLE_NAME + "(" + ResourceContract.ResourceEntry._ID  + ", name, type)  SELECT _id, name, type FROM  " + ResourceContract.ResourceEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + CycleContract.CycleEntry.TABLE_NAME + "(" + CycleContract.CycleEntry._ID +", cropId, landType, landAmt, cycledate, tspent, hType, hAmt, costPer, county, cropName ) SELECT _id, cropId, landType, landAmt, cycledate, tspent, hType, hAmt, costPer, county, cropName FROM " + CycleContract.CycleEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME + "(" + ResourcePurchaseContract.ResourcePurchaseEntry._ID  + ", rId, type, quantifier, qty, cost, remaining, date, resource)  SELECT _id, rId, type, quantifier, qty, cost, remaining, date, resource FROM " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + CycleResourceEntry.TABLE_NAME + "(" + CycleResourceEntry._ID  + ", pId, type, qty, quantifier, cycleId, useCost) SELECT _id, pId, type, qty, quantifier, cycleId, useCost FROM  " + CycleResourceEntry.TABLE_NAME + "_orig");
		// db.execSQL("INSERT INTO " + LabourEntry.TABLE_NAME + "(" + LabourEntry._ID  + ", labour, name) SELECT id, labour, name FROM  " + LabourEntry.TABLE_NAME + "_orig");

		db.execSQL("INSERT INTO " + CloudKeyContract.CloudKeyEntry.TABLE_NAME + "(" + CloudKeyContract.CloudKeyEntry._ID  + ", key, ctable, rowid ) SELECT _id, key, ctable, rowid  FROM " + CloudKeyContract.CloudKeyEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + RedoLogEntry.TABLE_NAME + "(" + RedoLogEntry._ID  + ", redotable, row_id, operation)  SELECT _id, redotable, row_id, operation FROM " + RedoLogEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + TransactionLogEntry.TABLE_NAME + "(" + TransactionLogEntry._ID  + ", transtable, rowid, operation, transtime)  SELECT _id, transtable, rowid, operation, transtime FROM  " + TransactionLogEntry.TABLE_NAME + "_orig");
		db.execSQL("INSERT INTO " + UpdateAccountContract.UpdateAccountEntry.TABLE_NAME + "(" + UpdateAccountContract.UpdateAccountEntry._ID  + ", acc, county, address, lastUpdated, signedIn, cloudKey)  SELECT _id, acc, county, address, lastUpdated, signedIn, cloudKey FROM " + UpdateAccountContract.UpdateAccountEntry.TABLE_NAME + "_orig");
	
		// db.execSQL("INSERT INTO " + CountryEntry.TABLE_NAME + "(" + CountryEntry._ID  + ", country, subdividion) SELECT (id, key, ctable, rowid ) FROM  " + CountryEntry.TABLE_NAME + "_orig");
		// db.execSQL("INSERT INTO " + CountyEntry.TABLE_NAME + "(" + CountyEntry._ID  + ", county, country) SELECT (id, county, country)  FROM  " + CountyEntry.TABLE_NAME + "_orig");
	}

	private void dropBackups(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + ResourceContract.ResourceEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + CycleContract.CycleEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + CycleResourceEntry.TABLE_NAME + "_orig");
		// db.execSQL("DROP TABLE IF EXISTS " + LabourEntry.TABLE_NAME + "_orig");

		db.execSQL("DROP TABLE IF EXISTS " + CloudKeyContract.CloudKeyEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + RedoLogEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + TransactionLogEntry.TABLE_NAME + "_orig");
		db.execSQL("DROP TABLE IF EXISTS " + UpdateAccountContract.UpdateAccountEntry.TABLE_NAME + "_orig");
	}
	
	private void createDb(SQLiteDatabase db) {
		createResources(db);
		createCropCycle(db);
		createResourcePurchases(db);
		createResourceUse(db);
		createLabour(db);

		createCloudKeys(db);
		createRedoLog(db);
		createTransactionLog(db);
		createUpdateAccount(db);
		
		createCountries(db);
		createCounties(db);
	}
	
	private void dropTables(SQLiteDatabase db) {
		db.beginTransaction();

		db.execSQL(CycleResourceContract.SQL_DELETE_CYCLE_RESOURCE);
		db.execSQL(ResourcePurchaseContract.SQL_DELETE_RESOURCE_PURCHASE);
		db.execSQL(CycleContract.SQL_DELETE_CYCLE);
		db.execSQL(ResourceContract.SQL_DELETE_RESOURCE);
		db.execSQL(CloudKeyContract.SQL_DELETE_CLOUD_KEY);
		db.execSQL(RedoLogContract.SQL_DELETE_REDO_LOG);
		db.execSQL(TransactionLogContract.SQL_DELETE_TRANSACTION_LOG);
		db.execSQL(UpdateAccountContract.SQL_DELETE_UPDATE_ACCOUNT);
		db.execSQL(CountryContract.SQL_DELETE_COUNTRIES);
		db.execSQL(CountyContract.SQL_DELETE_COUNTIES);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private boolean columnExists(SQLiteDatabase db, String tableName, String columnName){
		Cursor cursor = db.rawQuery("PRAGMA table_info("+tableName+");", null);
		while (cursor.moveToNext()){
			if (cursor.getString(cursor.getColumnIndex("name")).equalsIgnoreCase(columnName)){
				cursor.close();
				return true;
			}
		}
		cursor.close();
		return false;
	}


	//*************************** Create  Models

	private void createUpdateAccount(SQLiteDatabase db){db.execSQL(UpdateAccountContract.SQL_CREATE_UPDATE_ACCOUNT);}

	private void createCloudKeys(SQLiteDatabase db) {db.execSQL(CloudKeyContract.SQL_CREATE_CLOUD_KEY);}
	
	private void createCropCycle(SQLiteDatabase db) {
		db.execSQL(CycleContract.SQL_CREATE_CYCLE);
	} 
	
	private void createResources(SQLiteDatabase db) {db.execSQL(ResourceContract.SQL_CREATE_RESOURCE);}
	
	private void createTransactionLog(SQLiteDatabase db) {db.execSQL(TransactionLogContract.SQL_CREATE_TRANSACTION_LOG);}
	
	private void createResourcePurchases(SQLiteDatabase db) {db.execSQL(ResourcePurchaseContract.SQL_CREATE_RESOURCE_PURCHASE);}
	
	private void createResourceUse(SQLiteDatabase db) {db.execSQL(CycleResourceContract.SQL_CREATE_CYCLE_RESOURCE);}
	
	private void createRedoLog(SQLiteDatabase db) {
		db.execSQL(RedoLogContract.SQL_CREATE_REDO_LOG);
	}
	
	private void createCountries(SQLiteDatabase db) {db.execSQL(CountryContract.SQL_CREATE_COUNTRIES);}
	
	private void createCounties(SQLiteDatabase db) {
		db.execSQL(CountyContract.SQL_CREATE_COUNTIES);
	}

	private void createLabour(SQLiteDatabase db) {
		db.execSQL(LabourContract.SQL_CREATE_LABOUR);
	}



	//******************************* Insert Default Values

	private void insertDefaultCrops(SQLiteDatabase db) {
		//planting material - reference cardi - Caribbean Agricultural Research and Development Institute
		ArrayList<String> crops = CropDataHelper.getCrops(ctx);
		for (String crop : crops) {
			Log.i(TAG_NAME, "Inserting Crop: " + crop + " to database");
			DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, crop);
		}
	}

	private void insertDefaultFertilizers(SQLiteDatabase db) {
		//fertilizer -Plant Doctors tt
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Fersan (7.12.40 + 1TEM)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Magic Grow (7.12.40 + TE HYDROPHONIC)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Hydro YARA Liva (15.0.15)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Techni - Grow (7.12.27 + TE)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Ferqidd (10.13.32 + TE)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Plant Prod (7.12.27 + TE)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Flower Plus (9.18.36 + TE)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Iron Chelate Powder (FE - EDTA)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "Magnesium Sulphate (Mg SO4)");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "12-24-12 FERTILIZER");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "HARVEST MORE 10-55-10");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "HARVEST MORE 13-0-44");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "HARVEST MORE 5-5-45");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "NPK 12-12-17");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "UREA 46-0-0");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "PLANT BOOSTER");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "MIRACLE GRO ALL PROPOSE PLANT FOOD");
		DbQuery.insertResource(db, this, DHelper.cat_fertilizer, "SCOTTS FLOWER AND VEGETABLE PLANT FOOD");
	}
	
	private void insertDefaultSoilAdds(SQLiteDatabase db) {
		//soil amendments -Plant Doctors tt
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Cow manure");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Compost");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Gypsum");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Limestone");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Sulphur");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Molasses");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Chicken manure");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Horse manure");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Calphos");
		DbQuery.insertResource(db, this, DHelper.cat_soilAmendment, "Sharp sand");
	}
	
	private void insertDefaultChemicals(SQLiteDatabase db) {
		//chemical --http://en.wikipedia.org/wiki/Pesticide#Classified_by_type_of_pest
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Fungicide");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Insecticide");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Weedicide");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Algicides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Antimicrobials");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Biopesticides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Biocides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Fumigants");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Herbicides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Miticides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Microbial pesticides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Molluscicides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Nematicides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Ovicides");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Pheromones");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Repellents");
		DbQuery.insertResource(db, this, DHelper.cat_chemical, "Rodenticides");
	}
	
	private void insertDefaultCountries(SQLiteDatabase db) {
		for (String [] country : CountryContract.countries){
			DbQuery.insertCountry(db, country[0], country[1]);
		}
	}
	
	private void insertDefaultCounties(SQLiteDatabase db) {
		for (String [] county : CountyContract.counties){
			DbQuery.insertCounty(db, county[0], county[1]);
		}
	}

	private void populate(SQLiteDatabase db, TransactionLog tL) {
		//create user Account
//		UpAcc acc = new UpAcc();
//		acc.setSignedIn(0);
//		acc.setLastUpdated(System.currentTimeMillis() / 1000L);
//		DbQuery.insertAccountTask(db, acc);

		insertDefaultCrops(db);
		insertDefaultFertilizers(db);
		insertDefaultSoilAdds(db);
		insertDefaultChemicals(db);
		insertDefaultCountries(db);
		insertDefaultCounties(db);
	}


	//************* Update
    private void updatePurchaseRecs(SQLiteDatabase  db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, null);
        // Update Existing Dates to the current date
        while(cursor.moveToNext()){
            ContentValues cv = new ContentValues();
            cv.put(ResourcePurchaseContract.ResourcePurchaseEntry.RESOURCE_PURCHASE_DATE,  DateFormatHelper.getDateUnix(new Date()) );
        }
        cursor.close();
    }

    private void updateCycleCropName(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + CycleContract.CycleEntry.TABLE_NAME, null);
        while(cursor.moveToNext()){
            ContentValues cv = new ContentValues();
            cv.put(CycleContract.CycleEntry.CROPCYCLE_NAME, cursor.getColumnIndex(CycleContract.CycleEntry.CROPCYCLE_RESOURCE));
        }
        cursor.close();
    }

    private void updateCycleResource(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + CycleResourceContract.CycleResourceEntry.TABLE_NAME , null);
        while(cursor.moveToNext()){
            ContentValues cv = new ContentValues();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cv.put(CycleResourceContract.CycleResourceEntry.CYCLE_DATE_USED, cal.getTimeInMillis());
        }
        cursor.close();
    }

	private void updateCropList(SQLiteDatabase db) {
		//VEGETABLES
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BHAGI");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BORA (BODI) BEAN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CARAILLI");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CHOI SUM (CHINESE CABBAGE)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CORN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CUCUMBER");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "DASHEEN BUSH");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "GREEN FIG");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "COWPEA (GUB GUB)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "JACK BEAN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "JHINGI");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "LAUKI");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "RADISH (MOORAI)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "OCHRO");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PAKCHOY");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PIMENTO PEPPER");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PLANTAIN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "VINE SPINACH (POI BHAGI)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PUMPKIN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "SAIJAN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "SATPUTIYA");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "SEIM");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "STRING BEAN");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "SQUASH");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "TOMATO");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "WATERCRESS");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "WING BEAN");

		//ROOT CROPS
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BEET");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CUSH CUSH");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "GINGER");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "LEREN (TOPI TAMBU)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "TUMERIC (SAFFRON)");

		//herbs
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "ANISE SEED");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BASIL");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BAY LEAF");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CELERY");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CHIVE");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CURRY LEAF");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "DILL");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "FENNEL");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "MARJORAM");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "MINT");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "OREGANO");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PARSLEY");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "ROSEMARY");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CULANTRO (SHADON BENI / BANDANIA)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "TARRAGON");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "THYME - FRENCH");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "THYME - SPANISH");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "THYME - FINE");


		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PAW PAW");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PEANUTS");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "NUTMEG");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BANANA");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BREADFRUIT");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "BREADNUT (CHATAIGNE)");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CHERRY");
		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "CARAMBOLA");

		DbQuery.insertResource(db, this, DHelper.cat_plantingMaterial, "PUMPKIN");

	}


}
