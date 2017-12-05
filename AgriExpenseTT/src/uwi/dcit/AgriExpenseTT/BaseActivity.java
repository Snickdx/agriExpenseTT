package uwi.dcit.AgriExpenseTT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import uwi.dcit.AgriExpenseTT.cloud.SignInManager;
import uwi.dcit.AgriExpenseTT.fragments.NavigationDrawerFragment;
import uwi.dcit.AgriExpenseTT.helpers.NavigationControl;
import uwi.dcit.AgriExpenseTT.helpers.NetworkHelper;


public abstract class BaseActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks , NavigationControl {

    protected final int SIGN_IN_REQUEST = 2;
    protected SignInManager signInManager;
    protected Fragment leftFrag,rightFrag;
    protected NavigationDrawerFragment mNavigationDrawerFragment;
    protected boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInManager = new SignInManager(BaseActivity.this,BaseActivity.this);
        isTablet = this.getResources().getBoolean(R.bool.isTablet);
    }

    public void setupNavDrawer(){//... chaining
        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.setUp( R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {//... here we use cases wrong
        /*
            Strategy s = FS.get(position);
            s.do();

        */


        switch (position) { // Check to ensure that the we are not relaunching the current activity
            case 0:// Home
                if (!(this instanceof Main))
                    startActivity(new Intent(this, Main.class));
                break;
            case 1://new cycle
                if (!(this instanceof NewCycle))
                    startActivity(new Intent(this, NewCycle.class));
                break;
            case 2://new purchase
                if (!(this instanceof NewPurchase))
                    startActivity(new Intent(this, NewPurchase.class));
                break;
            case 3://hire labour
                if (!(this instanceof HireLabour))
                    startActivity(new Intent(this, HireLabour.class));
                break;
            case 4://report manager
                if (!(this instanceof ManageReport))
                    startActivity(new Intent(this,ManageReport.class));
                break;
            case 5:// manage data
                if (!(this instanceof ManageData))
                    startActivity(new Intent(this,ManageData.class));
                break;
            case 6: // sign in
                requestSignIn();
                break;
            default:
                startActivity(new Intent(this, Main.class));
        }
    }

    public void requestSignIn() {
        Intent i = new Intent(getApplicationContext(), Backup.class);

        if (!this.signInManager.localAccountExists()) {
            if (!NetworkHelper.isNetworkAvailable(this)){ 		// No network available so display appropriate message
                new AlertDialog.Builder(this) // Use Dialog to provide better feedback to ensure... toast are not easily seen
                        .setIcon(android.R.drawable.ic_dialog_alert) //TODO Change to Error icon from material library
                        .setTitle("No Internet Connection")
                        .setMessage("Ensure that your device is connected to the internet before signing in.")
                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSignIn();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("BaseActivity", "Sign-in Activity Cancelled");
                                //TODO Put activity log here for tracking
                            }
                        })
                        .show();
                return;
            }
            // Connected to the Internet so we attempt to sign in
            startActivityForResult(i, SIGN_IN_REQUEST);  // Launch the Backup activity with the sign-up action passed
        }
        else if (this.signInManager.isSignedIn()){
            // If not signed attempt to login with existing account
            signInManager.signIn();
        }
    }

    @Override
    public void navigate(Fragment oldFrag,Fragment newFrag) {

    }

    @Override
    public Fragment getLeftFrag() {
        return leftFrag;
    }
    @Override
    public Fragment getRightFrag() {
        return rightFrag;
    }

    @Override
    public String[] getMenuOptions() {
        return new String[]{
                getString(R.string.menu_item_home),
                getString(R.string.menu_item_newCycle),
                getString(R.string.menu_item_newPurchase),
                getString(R.string.menu_item_hireLabour),
                getString(R.string.menu_item_genFile),
                getString(R.string.menu_item_manageData)
        };
    }

    @Override
    public int[] getMenuImages() {
        return new int[]{
                R.drawable.ic_home_black_36dp,
                R.drawable.mainmenu_cycle_triangle,
                R.drawable.mainmenu_shopping_cart,
                R.drawable.mainmenu_shovel_single,
                R.drawable.mainmenu_reports,
                R.drawable.mainmenu_data_settings
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),ManageData.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        switch (requestCode) {
            case SignInManager.RC_SIGN_IN:
                Log.d("BaseActivity", "Result produced" + data.toString());
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                this.signInManager.handleSignInResult(result);
                break;
            case SIGN_IN_REQUEST:
                if (resultCode == 1) {
                    final String country = data.getStringExtra("country");
                    final String county = data.getStringExtra("county");
                    Log.d("BaseActivity", "Sign In request returned with " + country + " - " + county);
                    signInManager.signIn(country, county);
                }
                break;
        }
    }
}
