package uwi.dcit.AgriExpenseTT.fragments.help;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddHiringLabourContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {
        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("Hire Labour");
        TextView txt1 = (TextView)view.findViewById(R.id.article_text);
        txt1.setText(view.getResources().getString(R.string.help_hiring_labour_1));
        TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
        txt2.setText(view.getResources().getString(R.string.help_hiring_labour_3));
        TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
        txt3.setText(view.getResources().getString(R.string.help_hiring_labour_3));
        TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
        txt4.setText(view.getResources().getString(R.string.help_hiring_labour_4));
        TextView txt5 = (TextView)view.findViewById(R.id.article_text_5);
        txt5.setText(view.getResources().getString(R.string.help_hiring_labour_5));
    }

    @Override
    public void addImageContent(View view) {

        ImageView imgView1 =(ImageView)view.findViewById(R.id.article_image);
//		imgView1.setImageDrawable(getResources().getDrawable(R.drawable.help_hire_labour));
        imgView1.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_hire_labour, null));
        ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
//		imgView2.setImageDrawable(getResources().getDrawable(R.drawable.help_hire_labour_timeframe));
        imgView2.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_hire_labour_timeframe, null));
        ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
//		imgView3.setImageDrawable(getResources().getDrawable(R.drawable.help_hire_labour_cropcycle));
        imgView3.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_hire_labour_cropcycle, null));
        ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
//		imgView4.setImageDrawable(getResources().getDrawable(R.drawable.help_hire_labour_time));
        imgView4.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_hire_labour_time, null));
        ImageView imgView5 =(ImageView)view.findViewById(R.id.article_image_5);
//		imgView5.setImageDrawable(getResources().getDrawable(R.drawable.help_hire_labour_enterdetails));
        imgView5.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_hire_labour_enterdetails, null));

    }
}
