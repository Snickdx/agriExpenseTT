package uwi.dcit.AgriExpenseTT.fragments.help;

import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddNewPurchaseContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {
        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("New Purchase");
        TextView txt1 = (TextView)view.findViewById(R.id.article_text);
        txt1.setText(view.getResources().getString(R.string.help_purchase_new_resource_1));
        TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
        txt2.setText(view.getResources().getString(R.string.help_purchase_new_resource_2));
        TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
        txt3.setText(view.getResources().getString(R.string.help_purchase_new_resource_3));
        TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
        txt4.setText(view.getResources().getString(R.string.help_purchase_new_resource_4));
    }

    @Override
    public void addImageContent(View view) {
        ImageView imgView1 =(ImageView)view.findViewById(R.id.article_image);
        imgView1.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_new_purchase_category, null));
        ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
        imgView2.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_new_purchase_type, null));
        ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
        imgView3.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_new_purchase_quantity, null));
        ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
        imgView4.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_expense_categories, null));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imgView4.setMaxWidth(imgView4.getMaxWidth() * 2);
        }

    }
}
