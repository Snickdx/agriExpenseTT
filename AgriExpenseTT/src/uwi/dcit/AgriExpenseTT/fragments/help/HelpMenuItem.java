package uwi.dcit.AgriExpenseTT.fragments.help;

//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


/**
 * Created by DavidCharles on 12/2/2017.
 */

public abstract class HelpMenuItem extends Fragment {

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    public abstract void addToHelpList();
}
