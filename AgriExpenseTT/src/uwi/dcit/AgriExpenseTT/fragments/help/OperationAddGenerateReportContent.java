package uwi.dcit.AgriExpenseTT.fragments.help;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddGenerateReportContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {

        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("Generate Reports");
        TextView txt1 = (TextView) view.findViewById(R.id.article_text);
        txt1.setText(view.getResources().getString(R.string.help_generate_report));
    }

    @Override
    public void addImageContent(View view) {
        ImageView imgView1 = (ImageView) view.findViewById(R.id.article_image);
        imgView1.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.reportslide2, null));
    }
}
