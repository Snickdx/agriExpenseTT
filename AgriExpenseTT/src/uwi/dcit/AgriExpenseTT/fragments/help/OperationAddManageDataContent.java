package uwi.dcit.AgriExpenseTT.fragments.help;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddManageDataContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {
        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("Manage Data");
        TextView txt1 = (TextView)view.findViewById(R.id.article_text);
        txt1.setText(view.getResources().getString(R.string.help_manage_data_1));
        TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
        txt2.setText(view.getResources().getString(R.string.help_manage_data_2));
        TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
        txt3.setText(view.getResources().getString(R.string.help_manage_data_3));
        TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
        txt4.setText(view.getResources().getString(R.string.help_manage_data_4));
    }

    @Override
    public void addImageContent(View view) {
        ImageView imgView1 =(ImageView)view.findViewById(R.id.article_image);
        imgView1.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_manage_data, null));
        ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
        imgView2.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_manage_data_edit_cycle, null));
        ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
        imgView3.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_manage_data_editpurchases_details, null));
        ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
        imgView4.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_manage_data_delete_record, null));
    }
}
