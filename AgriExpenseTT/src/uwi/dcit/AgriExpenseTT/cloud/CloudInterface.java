package uwi.dcit.AgriExpenseTT.cloud;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;
import uwi.dcit.AgriExpenseTT.helpers.TransactionLog;
import uwi.dcit.AgriExpenseTT.models.CloudKeyContract;

import uwi.dcit.AgriExpenseTT.models.CycleContract.CycleEntry;
import uwi.dcit.AgriExpenseTT.models.CycleResourceContract.CycleResourceEntry;
import uwi.dcit.AgriExpenseTT.models.RedoLogContract.RedoLogEntry;
import uwi.dcit.AgriExpenseTT.models.ResourcePurchaseContract.ResourcePurchaseEntry;
import uwi.dcit.AgriExpenseTT.models.TransactionLogContract.TransactionLogEntry;
import uwi.dcit.agriexpensesvr.accountApi.AccountApi;
import uwi.dcit.agriexpensesvr.accountApi.model.Account;
import uwi.dcit.agriexpensesvr.cycleApi.CycleApi;
import uwi.dcit.agriexpensesvr.cycleApi.model.Cycle;
import uwi.dcit.agriexpensesvr.cycleUseApi.CycleUseApi;
import uwi.dcit.agriexpensesvr.cycleUseApi.model.CycleUse;
import uwi.dcit.agriexpensesvr.resourcePurchaseApi.ResourcePurchaseApi;
import uwi.dcit.agriexpensesvr.resourcePurchaseApi.model.ResourcePurchase;
import uwi.dcit.agriexpensesvr.translogApi.TranslogApi;
import uwi.dcit.agriexpensesvr.translogApi.model.TransLog;

public class CloudInterface {
	SQLiteDatabase db;
	DbHelper dbh;
	TransactionLog tL;
	private final String TAG_NAME = "CloudInterface";

	public CloudInterface(Context context) {
		dbh= new DbHelper(context);
		db=dbh.getWritableDatabase();
		tL=new TransactionLog(dbh,db,context);
	}

	public CloudInterface(Context context,SQLiteDatabase db,DbHelper dbh) {
		this.dbh = dbh;
		this.db = db;
		tL = new TransactionLog(dbh,db,context);
	}

	public void updateCycle(){
		new CycleUpdater().execute();
	}
    public void updatePurchase(){
        new PurchaseUpdater().execute();
    }
    public void insertCycle(){
        new InsertCycle().execute();
    }

	public void insertCycleUseC() {
		new InsertCycleUse().execute();


	}

	public void insertPurchase() {
		new InsertPurchase().execute();
	}

	public void deleteCycle() {
		new DeleteCycle().execute();
	}

	public void deleteCycleUse() {
		new DeleteCycleUse().execute();
	}

	public void deletePurchase() {
		new DeletePurchase().execute();
	}

	public void insertLog() {
		new InsertLog().execute();
	}

	public Account insertAccount(String namespace, long time, String country, String county) {
		if (time == -1)
			time = System.currentTimeMillis() / 1000L;
		AccountApi.Builder builder = new AccountApi.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
		builder = CloudEndpointUtils.updateBuilder(builder);
		AccountApi endpoint = builder.build();
		Account acc = new Account();
		acc.setAccount(namespace);
		acc.setLastUpdated(time);
		acc.setCountry(country);
		acc.setCounty(county);
		try {
			Log.i(TAG_NAME, "Name:" + namespace + " Country:" + country + " County:" + county);
			acc = endpoint.getOrInsertAccount(acc.getAccount(), acc.getCounty(), acc.getCountry()).execute();
			DbQuery.insertAccountTask(db, dbh, acc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return acc;
	}

	public void insertAccount2(String namespace, long time, String country, String county) {
		if (time == -1)
			time = System.currentTimeMillis() / 1000L;
		new insertAccountTask(namespace, time, country, county).execute();
	}

	public void updateUpAccC(Long time) {
		Account acc = DbQuery.getUpAcc(db);
		if (acc != null) {
			acc.setLastUpdated(time);
			new updateUpAcc().execute(acc);
		} else {
			Log.wtf(TAG_NAME, "No Account Stored in Local Database");
		}
	}

	public Account getAccount(String namespace) {
		AccountApi.Builder builder = new AccountApi.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
		builder = CloudEndpointUtils.updateBuilder(builder);
		AccountApi endpoint = builder.build();
		Account acc = null;
		try {
			acc = endpoint.getAccount(namespace).execute();
//			acc=endpoint.getAccount((long) 1,namespace).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return acc;
	}

	public void flushToCloud() {
		insertCycle();
		insertPurchase();
		insertCycleUseC();
		updateCycle();
		updatePurchase();
		deletePurchase();
		deleteCycle();
		//We need to set the time of both the app's database and the cloud to the same.
		Account localAccount = DbQuery.getUpAcc(db);
		AccountApi.Builder builder = new AccountApi.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				null);
		builder = CloudEndpointUtils.updateBuilder(builder);
		AccountApi endpoint = builder.build();
		Account cloudAccount;
		try {
			cloudAccount = endpoint.getAccount(localAccount.getAccount()).execute();
			if (cloudAccount != null) {
				long time = System.currentTimeMillis() / 1000;
				cloudAccount.setLastUpdated(time);
				DbQuery.updateAccount(db, time);
				endpoint.updateAccount(cloudAccount).execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CycleUpdater extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			CycleApi.Builder builder = new CycleApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			CycleApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();
			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_UPDATE, CycleEntry.TABLE_NAME);
			Iterator<Integer> logI=logIds.iterator();
			Iterator<Integer> rowI=rowIds.iterator();
			while(logI.hasNext()){
				int logId = logI.next(), rowId = rowI.next();//the current primary key of CROP CYCLE Table
				Cycle c = DbQuery.getCycle(db, dbh, rowId);
				if (c != null) {
					c.setAccount(DbQuery.getAccountName(db));
					Log.i("UPDATE CYCLE", "Cycle:" + c);
					//String keyrep=DbQuery.getKey(db, dbh, CycleEntry.TABLE_NAME, c.getId());
					//Log.i("KEYPREP UDATE",">>>>>>>>"+keyrep);
					//c.setKeyrep(keyrep);
					if (c.getTotalSpent() == 0.00)
						c.setTotalSpent(-1.00);
					try {
						Log.i("YOU ARE IN!", "INSIDE THE UPDATE CODE!");
						//c=endpoint.insertCycle(c).execute();
						c = endpoint.updateCycle(c).execute();
						//                    endpoint.updateCycle(c);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("could not update cycle");
						c = null;
					}
					//we stored they key as text in the account field of c when we returned
					//System.out.println(c.getAccount());
					//store key of inserted cycle into cloud - cloud key table
					//DbQuery.insertCloudKey(db, dbh, CycleEntry.TABLE_NAME, c.getAccount(),rowId);
					//remove from redo log
                    try {
                        DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//getting the transaction from the transaction log that matches this operation
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_TABLE + "='" + CycleEntry.TABLE_NAME + "' and "
							+ TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='" + TransactionLog.TL_UPDATE + "'";
					Cursor cursor = db.rawQuery(code, null);
					//select from transaction log
					//where transaction's rowId = rowId
					//and transactions's table = table
					//and transactions's operation = operation
					//but there can be multiple operations (updates) on a particular object (row[Id] of a Table)
					//what should we do in this case !? :s
					cursor.moveToLast();//only for updaates we should move to last because the last update would hold the most current data
					int id = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, id, TransactionLog.TL_INS);
					Log.i("Cycle Update!", "At ID:" + id);
					insertLog();
				}
			}
			return null;
		}

	}

	private class PurchaseUpdater extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ResourcePurchaseApi.Builder builder = new ResourcePurchaseApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			ResourcePurchaseApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();
			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_UPDATE, ResourcePurchaseEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();//the current primary key of CROP CYCLE Table
				ResourcePurchase p = DbQuery.getARPurchase(db, dbh, rowId);
				if (p != null) {
					p.setAccount(DbQuery.getAccountName(db));
					//String keyrep=DbQuery.getKey(db, dbh, ResourcePurchaseEntry.TABLE_NAME, p.getPId());
					if (p.getQtyRemaining() == 0) {
						p.setQtyRemaining(-1.00);
					}
					try {
						//p.setKeyrep(keyrep);
						p = endpoint.updateRPurchase(p).execute();
					} catch (Exception e) {

						return null;
					}
					//we stored they key as text in the account field of c when we returned
					//System.out.println(c.getAccount());
					//store key of inserted cycle into cloud - cloud key table
					//DbQuery.insertCloudKey(db, dbh, CycleEntry.TABLE_NAME, c.getAccount(),rowId);
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//getting the transaction from the transaction log that matches this operation
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_TABLE + "='" + ResourcePurchaseEntry.TABLE_NAME + "' and "
							+ TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='" + TransactionLog.TL_UPDATE + "'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToLast();//only for updates explained in cycleUpdate
					int id = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, id, TransactionLog.TL_INS);
					insertLog();
				}
			}
			return null;
		}

	}

	public class InsertCycle extends AsyncTask<Void, Object, Object> {

		@Override
		protected Cycle doInBackground(Void... params) {
			CycleApi.Builder builder = new CycleApi.Builder(
					AndroidHttp.newCompatibleTransport(),
					new JacksonFactory(),
					null);

			builder = CloudEndpointUtils.updateBuilder(builder);
			CycleApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();

			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, CycleEntry.TABLE_NAME);

			Iterator<Integer> logI=logIds.iterator();
			Iterator<Integer> rowI=rowIds.iterator();

			while(logI.hasNext()){

				int logId = logI.next(),
						rowId = rowI.next();              // The current primary key of CROP CYCLE Table

				Cycle c = DbQuery.getCycle(db, dbh, rowId);

				if (c != null) {
					c.setAccount(DbQuery.getAccountName(db));   // Uses the account rep as the namespace
					Log.i("TEST MEEE", "Cycle:" + c.getStartDate());
					try {
						c = endpoint.insertCycle(c).execute();

					} catch (Exception e) {

						return null;
					}
					//we stored they key as text in the account field of c when we returned
					System.out.println(c.getAccount());
					//store key of inserted cycle into cloud - cloud key table
					DbQuery.insertCloudKey(db, dbh, CycleEntry.TABLE_NAME, c.getKeyrep(), rowId);
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
                    }catch (Exception e){e.printStackTrace();}
					//getting the transaction from the transaction log that matches this operation
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_TABLE + "='" + CycleEntry.TABLE_NAME + "' and "
							+ TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='" + TransactionLog.TL_INS + "'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					int id = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, id, TransactionLog.TL_INS);
					insertLog();
					//When we have finished inserting a cycle, we would like to record the timestamp of the change made to the local database.
					//This would be reflected in the lastUpdated variable.
//					DbQuery.updateAccount(db,System.currentTimeMillis()/1000L);

				}
			}
			return null;
		}

	}

	public class InsertCycleUse extends AsyncTask<Void, Object, Object> {

		@Override
		protected Cycle doInBackground(Void... params) {
			CycleUseApi.Builder builder = new CycleUseApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			CycleUseApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();
			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, CycleResourceEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();
				CycleUse c = DbQuery.getACycleUse(db, dbh, rowId);

				if (c != null) {
					c.setAccount(DbQuery.getAccountName(db));
					try {
						Log.i("INSERTING!", "HEREe");
						c = endpoint.insertCycleUse(c).execute();
					} catch (Exception e) {
						return null;
					}
					//we stored they key as text in the account field of c when we returned
					System.out.println(c.getAccount());
					//store key of inserted cycleuse into cloud - cloud key table
					DbQuery.insertCloudKey(db, dbh, CycleResourceEntry.TABLE_NAME, c.getAccount(), rowId);
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//getting the transaction from the transaction log that matches this operation
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_TABLE + "='" + CycleResourceEntry.TABLE_NAME + "' and "
							+ TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='" + TransactionLog.TL_INS + "'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					int id = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, id, TransactionLog.TL_INS);
					insertLog();
				}
			}
			return null;
		}

	}

	public class InsertPurchase extends AsyncTask<Void, Object, Object> {

		@Override
		protected ResourcePurchase doInBackground(Void... params) {
			ResourcePurchaseApi.Builder builder = new ResourcePurchaseApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			ResourcePurchaseApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();
			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, ResourcePurchaseEntry.TABLE_NAME);
			Iterator<Integer> logI=logIds.iterator();
			Iterator<Integer> rowI=rowIds.iterator();
			while(logI.hasNext()){
				int logId=logI.next(),rowId=rowI.next();
				ResourcePurchase purchase = DbQuery.getARPurchase(db, dbh, rowId);

				if (purchase != null) {
					purchase.setAccount(DbQuery.getAccountName(db));
					int rowID = DbQuery.getLast(db, dbh, ResourcePurchaseEntry.TABLE_NAME);
					Log.i("ROW ID>>>>", "LAST ID FROM RESOURCE PURCHASE TABLE:" + rowID);
					try {
						Log.i(TAG_NAME, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + purchase + "Time:" + purchase
								.getPurchaseDate());
						purchase = endpoint.insertRPurchase(purchase).execute();
					} catch (Exception e) {
						return null;
					}

					//we stored they key as text in the account field of c when we returned
					System.out.println(purchase.getAccount());
					//store key of inserted cycleuse into cloud - cloud key table
					Log.i(TAG_NAME, "INSERTING CLOUD KEY!! >>>" + purchase.getKeyrep());
					DbQuery.insertCloudKey(db, dbh, ResourcePurchaseEntry.TABLE_NAME, purchase.getKeyrep(), rowId);
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//getting the transaction from the transaction log that matches this operation
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_TABLE + "='" + ResourcePurchaseEntry.TABLE_NAME + "' and "
							+ TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='" + TransactionLog.TL_INS + "'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					int id = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, id, TransactionLog.TL_INS);
					insertLog();
				}
			}
			return null;
		}

	}

	public class DeleteCycle extends AsyncTask<Void, Object, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			CycleApi.Builder builder = new CycleApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			CycleApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();

			//DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, dbh.TABLE_RESOURCE_PURCHASES);
			DbQuery.getRedo(db, dbh, rowIds, logIds, "del", CycleEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();
				Cycle c = new Cycle();
				c.setId(rowId);
				c.setAccount(DbQuery.getAccountName(db));
				c.setKeyrep(DbQuery.getKey(db, dbh, CycleEntry.TABLE_NAME, rowId));
				if (c.getId() != 0) {//was never inserted :o
					try {
						endpoint.removeCycle(c.getKeyrep(), c.getAccount()).execute();
					} catch (Exception e) {
						c = null;
					}
				}

				if (c != null) {//transactioon was succesful or keyrep was not found
					int id = DbQuery.getCloudKeyId(db, dbh, CycleEntry.TABLE_NAME, rowId);
					if (id != -1) {
						//remove key of cycle that was deleted from cloud
						try {
							DbQuery.deleteRecord(db, dbh, CloudKeyContract.table, id);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//getting the transaction from the transaction log that matches this operation
//					String code="select * from "+TransactionLogEntry.TABLE_NAME+" where "+TransactionLogEntry.TRANSACTION_LOG_TABLE+"="+CycleEntry.TABLE_NAME+
//							" and "+TransactionLogEntry.TRANSACTION_LOG_ROWID+"="+rowId+" and "+TransactionLogEntry.TRANSACTION_LOG_OPERATION+"=del";

					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_ROWID + " = " + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='del'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						String tableName = cursor.getString(cursor.getColumnIndex(TransactionLogEntry.TRANSACTION_LOG_TABLE));
						int i = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
						Log.i("CHECKING", "FROM CURSOR" + i + "  FROM LOGID:" + logId + "  FROM ROWID" + rowId);
						if (tableName.equals(CycleEntry.TABLE_NAME)) {
							int Tid = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
							Log.i("DELETING CYCLE", "DELETIONNNNNNNNN ID:" + Tid);
							DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, Tid, TransactionLog.TL_INS);
							insertLog();
						}
						cursor.moveToNext();
					}
					cursor.close();
//					int Tid=cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
//					cursor.close();
//					Log.i("DELETING CYCLE","DELETIONNNNNNNNN ID:"+Tid);
//					inserting this record of the transaction to the redo log to later be inserted into the cloud
//					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, Tid, TransactionLog.TL_INS);
//					insertLog();
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			/*db.close();
			db=dbh.getWritableDatabase();
			db.execSQL("drop table if exists "+DbHelper.TABLE_NAME);
			dbh.createRedoLog(db);
			db.close();
			db=dbh.getReadableDatabase();*/
			super.onPostExecute(result);
		}


	}

	public class DeleteCycleUse extends AsyncTask<Void, Object, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			CycleUseApi.Builder builder = new CycleUseApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			CycleUseApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();

			//DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, dbh.TABLE_RESOURCE_PURCHASES);
			DbQuery.getRedo(db, dbh, rowIds, logIds, "del", CycleResourceEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();
				CycleUse c = new CycleUse();
				c.setId(rowId);
				c.setAccount(DbQuery.getAccountName(db));
				String keyrep = DbQuery.getKey(db, dbh, CycleResourceEntry.TABLE_NAME, rowId);
				c.setKeyrep(keyrep);
				Log.i("KEYREPPPPPP", "" + keyrep);
				if (keyrep != null) {
					try {
						endpoint.removeCycleUse(c.getKeyrep(), c.getAccount()).execute();
					} catch (Exception e) {
						c = null;
					}
				}
				if (c != null) {
					int id = DbQuery.getCloudKeyId(db, dbh, CycleResourceEntry.TABLE_NAME, rowId);
					if (id != -1) {
						//remove key of cycle that was deleted from cloud
						try {
							DbQuery.deleteRecord(db, dbh, CloudKeyContract.table, id);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//getting the transaction from the transaction log that matches this operation
//					String code="select * from "+TransactionLogEntry.TABLE_NAME+" where "+TransactionLogEntry.TRANSACTION_LOG_TABLE+"="+CycleResourceEntry.TABLE_NAME+
//							" and "+TransactionLogEntry.TRANSACTION_LOG_ROWID+"="+rowId+" and "+TransactionLogEntry.TRANSACTION_LOG_OPERATION+"='del'";
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='del'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					int Tid = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, Tid, TransactionLog.TL_INS);
					insertLog();

				}
			}
			return null;
		}

	}

	public class DeletePurchase extends AsyncTask<Void, Object, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			ResourcePurchaseApi.Builder builder = new ResourcePurchaseApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
			         null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			ResourcePurchaseApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();

			//DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, dbh.TABLE_RESOURCE_PURCHASES);
			DbQuery.getRedo(db, dbh, rowIds, logIds, "del", ResourcePurchaseEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();
				ResourcePurchase p = new ResourcePurchase();
				p.setPId(rowId);
				p.setAccount(DbQuery.getAccountName(db));
				String k = DbQuery.getKey(db, dbh, ResourcePurchaseEntry.TABLE_NAME, rowId);
				p.setKeyrep(k);
				Log.i("KEY", "KEY IS::" + k);
				Log.i("RP", "REMOVING RES P. OBJECCT:::>>" + p);
				try {
					Log.i("RP", "REMOVING RES P.");
					endpoint.removeRPurchase(p.getKeyrep(), p.getAccount()).execute();
				} catch (Exception e) {
					e.printStackTrace();
					p = null;
				}
				if (p != null) {//the removal was successful OR there was not ever inserted into the cloud
					int id = DbQuery.getCloudKeyId(db, dbh, ResourcePurchaseEntry.TABLE_NAME, rowId);
					if (id != -1) {//if the key exists
						try {
							DbQuery.deleteRecord(db, dbh, CloudKeyContract.table, id);
						} catch (Exception e) {
							e.printStackTrace();
						}

						//getting the transaction from the transaction log that matches this operation
					}

					//remove from redo log
					try {
						DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//remove key of cycle that was deleted from cloud
//					String code="select * from "+TransactionLogEntry.TABLE_NAME+" where "+TransactionLogEntry.TRANSACTION_LOG_TABLE+"='"+ResourcePurchaseEntry.TABLE_NAME+"'"
//							+ " and "+TransactionLogEntry.TRANSACTION_LOG_ROWID+"="+rowId+" and "+TransactionLogEntry.TRANSACTION_LOG_OPERATION+"='del'";
					String code = "select * from " + TransactionLogEntry.TABLE_NAME + " where " + TransactionLogEntry.TRANSACTION_LOG_ROWID + "=" + rowId + " and " + TransactionLogEntry.TRANSACTION_LOG_OPERATION + "='del'";
					Cursor cursor = db.rawQuery(code, null);
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						String tableName = cursor.getString(cursor.getColumnIndex(TransactionLogEntry.TRANSACTION_LOG_TABLE));
						if (tableName.equals(ResourcePurchaseEntry.TABLE_NAME)) {
							int Tid = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
							DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, Tid, TransactionLog.TL_INS);
							Log.i("DELETION", "TRANSACTION LOG DELETION----PURCHASE");
							insertLog();
						}
						cursor.moveToNext();
					}

//					int Tid = cursor.getInt(cursor.getColumnIndex(TransactionLogEntry._ID));
					cursor.close();

					//inserting this record of the transaction to the redo log to later be inserted into the cloud
//					DbQuery.insertRedoLog(db, dbh, TransactionLogEntry.TABLE_NAME, Tid, TransactionLog.TL_INS);
//					Log.i("DELETION","TRANSACTION LOG DELETION----CYCLE");
//					insertLog();

				}
			}

			return null;
		}
	}

	public class InsertLog extends AsyncTask<Void, Object, Object> {

		@Override
		protected Void doInBackground(Void... params) {
			TranslogApi.Builder builder = new TranslogApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			TranslogApi endpoint = builder.build();
			ArrayList<Integer> rowIds = new ArrayList<>();
			ArrayList<Integer> logIds = new ArrayList<>();
			DbQuery.getRedo(db, dbh, rowIds, logIds, TransactionLog.TL_INS, TransactionLogEntry.TABLE_NAME);
			Iterator<Integer> logI = logIds.iterator();
			Iterator<Integer> rowI = rowIds.iterator();
			while (logI.hasNext()) {
				int logId = logI.next(), rowId = rowI.next();
				//Cycle c=DbQuery.getCycle(db, dbh, rowId);
				Log.i("ID , ROWID", "ID:" + logId + "RowID:" + rowId);
				TransLog t = DbQuery.getLog(db, dbh, rowId);
				String k = DbQuery.getKey(db, dbh, t.getTableKind(), t.getRowId());//gets the key for the related object in the cloud
				t.setKeyrep(k);//stores the keyrep for its relating object
				Log.i("Transaction Log", "Transaction Log Insertion::" + t);
				t.setAccount(DbQuery.getAccountName(db));

				int rowID = DbQuery.getLast(db, dbh, TransactionLogEntry.TABLE_NAME);
				Log.i("ROW ID>>>>", "LAST ID FROM TRANSACTION LOG TABLE:" + rowID + "ID FROM VARIABLE:" + t.getId());
				try {
					t = endpoint.insertTransLog(t).execute();
//					updateUpAccC(t.getTransTime());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				updateUpAccC(t.getTransTime());
				try {
					DbQuery.deleteRecord(db, dbh, RedoLogEntry.TABLE_NAME, logId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public class insertAccountTask extends AsyncTask<Void, Void, Void> {
		String namespace;
		long time;
		String country, county;

		public insertAccountTask(String namespace, long time, String country, String county) {
			this.namespace = namespace;
			this.time = time;
			this.country = country;
			this.county = county;
		}
		@Override
		protected Void doInBackground(Void... params) {
			AccountApi.Builder builder = new AccountApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
			         null);
			builder = CloudEndpointUtils.updateBuilder(builder);
            AccountApi endpoint = builder.build();
			Account acc = new Account();
			acc.setAccount(namespace);
			acc.setLastUpdated(time);
			acc.setCounty(county);
			acc.setCountry(country);
			try {
				Log.i("myTestToInsertttttt", "Name:" + namespace + "Country:" + country + "County:" + county);
//				endpoint.getOrInsertAccount(namespace, "SVG", "St George's").execute();
				acc = endpoint.getOrInsertAccount(acc.getAccount(), acc.getCountry(), acc.getCounty()).execute();
				DbQuery.insertAccountTask(db, dbh, acc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}


	}

	public class updateUpAcc extends AsyncTask<Account, Void, Void> {
		@Override
		protected Void doInBackground(Account... params) {
			AccountApi.Builder builder = new AccountApi.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					null);
			builder = CloudEndpointUtils.updateBuilder(builder);
			AccountApi endpoint = builder.build();
			Account acc = params[0];
			try {
				endpoint.updateAccount(acc).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
	
}
