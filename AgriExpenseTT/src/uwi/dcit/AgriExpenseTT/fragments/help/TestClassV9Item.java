package uwi.dcit.AgriExpenseTT.fragments.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/4/2017.
 */

public class TestClassV9Item {
    private View v;
    public TestClassV9Item(Context context){
        v = LayoutInflater.from(context).inflate(R.layout.item_to_inflate,null,false);
    }
    public void setHeading(String str){
        ((TextView)v.findViewById(R.id.article_heading)).setText(str);
    }
    public View getView(){
        return v;
    }
}
