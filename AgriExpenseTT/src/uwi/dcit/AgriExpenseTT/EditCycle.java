package uwi.dcit.AgriExpenseTT;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DataManager;
import uwi.dcit.AgriExpenseTT.helpers.DateFormatHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;
import uwi.dcit.AgriExpenseTT.helpers.TextHelper;
import uwi.dcit.AgriExpenseTT.models.CycleContract;
import uwi.dcit.AgriExpenseTT.models.CycleContract.CycleEntry;
import uwi.dcit.AgriExpenseTT.models.LocalCycle;

public class EditCycle extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private EditText et_landQty;

    private TextView tv_crop;
    private TextView tv_landType;
    private TextView tv_landQty;
    private TextView tv_date;

    private final int REQ_CROP = 1;
    private String crop = null;
    private final int REQ_LANDTYPE = 2;
    private String land = null;
    private double landQty;
    private long date;
	
	SQLiteDatabase db;
	DbHelper dbh;
	
	LocalCycle cycle;
    private EditText et_name;
    private String name;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_cycle);
        GAnalyticsHelper.getInstance(this.getApplicationContext()).sendScreenView("Edit Cycle");
        // Initialize Database
		dbh = new DbHelper(this);
        db = dbh.getWritableDatabase();

        // Retrieve and populate UI With selected Cycle
        initialize();

        // Prevent unwanted display of the keyboard TODO Evaluate necessity
        findViewById(R.id.contEditCycle).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!(v instanceof EditText)) hideSoftKeyboard();
                return false;
            }
        });
	}

	private void initialize() {

        cycle   = getIntent().getExtras().getParcelable("cycle");
		crop    = DbQuery.findResourceName(db, dbh, cycle.getCropId());
		land    = cycle.getLandType();
		landQty = cycle.getLandQty();
		date    = cycle.getTime();
        name    = cycle.getCycleName();
		
		//Get Text Views
		tv_crop     = (TextView)findViewById(R.id.tv_editcycle_cropVal);
		tv_landType = (TextView)findViewById(R.id.tv_editcycle_landVal);
		tv_landQty  = (TextView)findViewById(R.id.tv_editcycle_landQtyVal);
		tv_date     = (TextView)findViewById(R.id.tv_editcycle_dateVal);
        et_landQty  = (EditText)findViewById(R.id.et_editCycle_landQty);
        et_name     = (EditText)findViewById(R.id.et_editCycle_name);

		//initialize views
		tv_crop.setText(crop);
		tv_landType.setText(land);
		tv_landQty.setText(String.valueOf(landQty));
        et_name.setText(name);

		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(date);

		tv_date.setText(DateFormatHelper.formatDisplayDate(cal));
	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED){
			return;
		}
		if(requestCode == REQ_CROP){
			crop = data.getExtras().getString("content");
            ((TextView)findViewById(R.id.tv_editcycle_cropVal)).setText(crop);
		}else if(requestCode == REQ_LANDTYPE){
			land = data.getExtras().getString("content");
            ((TextView)findViewById(R.id.tv_editcycle_landVal)).setText(land);
		}
	}

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void editCrop(View v){
        Intent i=new Intent(EditCycle.this,EditChooseLists.class);
        i.putExtra("desc",DHelper.cat_plantingMaterial);
        startActivityForResult(i,REQ_CROP);
    }

    public void editLand(View v){
        Intent i=new Intent(EditCycle.this,EditChooseLists.class);
        i.putExtra("desc", "land");
        startActivityForResult(i,REQ_LANDTYPE);
    }

    public void editDate(View v){
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        (new DatePickerDialog(this, this, year, month, day)).show();
    }
	
	public void updateCycle(View v) {
		if(!et_landQty.getText().toString().equals("")){
			landQty = Double.parseDouble(et_landQty.getText().toString());
		}
        name = TextHelper.formatUserText(et_name.getText().toString());

		ContentValues cv = new CycleContract(db, crop, land, landQty, date, name).getContentValues();

		Toast.makeText(getApplicationContext(),"Updating "+ " "+name+crop+" "+land+" "+landQty+" "+date, Toast.LENGTH_SHORT).show();

		DataManager dm=new DataManager(EditCycle.this, db, dbh);
		boolean result = dm.updateCycle(cycle, cv);

        if (result) Toast.makeText(getApplicationContext(), "Cycle was successfully Updated", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(), "Cycle was not updated", Toast.LENGTH_SHORT).show();

		finish();
	}

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = Calendar.getInstance();
        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        String format = DateFormatHelper.formatDisplayDate(cal);
        tv_date.setText(format);
        date = cal.getTimeInMillis();
    }

}
