package uwi.dcit.AgriExpenseTT;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.fragments.NewPurchaseLists;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;


public class NewPurchase extends ActionBarActivity {
	TextView sub_head;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_cycle_redesigned);
		setupUI();
		setupInitialFrag();

        //Google Analytics
        GAnalyticsHelper.getInstance(this.getApplicationContext()).sendScreenView("New Purchase");
	}
	
	private void setupInitialFrag() {
		Bundle arguments = new Bundle();
		arguments.putString("type","category");
		
		ListFragment listfrag = new NewPurchaseLists();
		listfrag.setArguments(arguments);
		
		getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.NewCycleListContainer,listfrag)
			.commit();
	}

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
	
	public void setupUI() {
		View v=findViewById(R.id.container_newcycle);
		TouchL l=new TouchL();
		v.setOnTouchListener(l);
	}
	
	public class TouchL implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
//			if(v.getId()!=R.id.et_listReuse_search)
//				hideSoftKeyboard();
			return false;
		}
	   
   }
	@Override
	public void onBackPressed(){
	    FragmentManager fm = getSupportFragmentManager();
	    if (fm.getBackStackEntryCount() > 0) {
	        Log.i("MainActivity", "popping backstack");
	        fm.popBackStack();
	    } else {
	        Log.i("MainActivity", "nothing on backstack, calling super");
	        super.onBackPressed();  
	    }
	}
	public void replaceSub(String text){
		sub_head=(TextView)findViewById(R.id.tv_mainNew_subheader);
		sub_head.setText(text);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_purchase_redesign, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
