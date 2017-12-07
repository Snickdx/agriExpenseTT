package uwi.dcit.AgriExpenseTT.fragments.help;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by DavidCharles on 12/4/2017.
 */

public class TestClassV9 {
    LinearLayout mainView;

    public TestClassV9(Context context){
        mainView = new LinearLayout(context);
        mainView.setOrientation(LinearLayout.VERTICAL);
    }

    public void addItem(View v){
        mainView.addView(v);
        mainView.postInvalidate();
    }

    public View getView(){
        return mainView;
    }
}


