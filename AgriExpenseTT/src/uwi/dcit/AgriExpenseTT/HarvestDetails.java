package uwi.dcit.AgriExpenseTT;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;
import uwi.dcit.AgriExpenseTT.models.CycleContract;
import uwi.dcit.AgriExpenseTT.models.LocalCycle;

public class HarvestDetails extends BaseActivity {
	private final int REQ_MEASURE=1;
	String qtfr;
	double qty;
	Button btn_qtfr;
	TextView tv_qty;
	EditText et_amt;
	LocalCycle currCycle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_harvest_details);
		setup();
        GAnalyticsHelper.getInstance(this.getApplicationContext()).sendScreenView("Harvest Details");
	}
	private void setup() {
		btn_qtfr=(Button)findViewById(R.id.btn_harvestDet_qtfr);
		Click c=new Click();
		btn_qtfr.setOnClickListener(c);
		tv_qty=(TextView)findViewById(R.id.tv_harvestDet_qty);
		Button btn_save=(Button)findViewById(R.id.btn_harvestDet_done);
		btn_save.setOnClickListener(c);
		et_amt=(EditText)findViewById(R.id.et_harvestDet_qty);
		Bundle b=getIntent().getExtras().getBundle("cyc");
		currCycle=b.getParcelable("cycle");
		qtfr=currCycle.getHarvestType();
		qty=currCycle.getHarvestAmt();
		et_amt.setText(""+qty);

        View v=findViewById(R.id.cont_harvestDet_main);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!(v instanceof EditText)){
                    hideSoftKeyboard();
                }
                return false;
            }
        });
	}

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_CANCELED){
			return;
		}
		if(requestCode==REQ_MEASURE){
			qtfr=data.getExtras().getString("content");
			btn_qtfr.setText(qtfr);
			tv_qty.setText("Number of "+qtfr+" of crop you've harvested or expect to harvest");
			//TextView t=(TextView)findViewById(R.id.tv_editPurchase_resVal);
			//t.setText(resource);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.harvest_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	public class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_harvestDet_qtfr) {
				Intent i = new Intent(HarvestDetails.this, EditChooseLists.class);
				i.putExtra("desc", "measurement");
				startActivityForResult(i, REQ_MEASURE);
			} else if (v.getId() == R.id.btn_harvestDet_done) {
				if (!(et_amt.getText().toString() == null || (et_amt.getText().toString().equals(""))))
					qty = Double.parseDouble(et_amt.getText().toString());
				save();
				Intent i = new Intent();
				i.putExtra("qtfr", qtfr);
				i.putExtra("amt", qty);
				setResult(1, i);//used to set the results for the parent activity ( the one that launched this one)
				finish();
			}
		}

		private void save() {
			ContentValues cv = new ContentValues();
			cv.put(CycleContract.CycleEntry.CROPCYCLE_HARVEST_AMT, qty);
			cv.put(CycleContract.CycleEntry.CROPCYCLE_HARVEST_TYPE, qtfr);
			DbHelper dbh = new DbHelper(HarvestDetails.this);
			SQLiteDatabase db = dbh.getWritableDatabase();
			db.update(CycleContract.table, cv, CycleContract.CycleEntry._ID + "=" + currCycle.getId(), null);
		}

	}
}
