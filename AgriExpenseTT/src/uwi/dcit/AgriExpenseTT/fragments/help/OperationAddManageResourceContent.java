package uwi.dcit.AgriExpenseTT.fragments.help;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddManageResourceContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {
        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("Managing Resources");
        TextView txtContent = (TextView)view.findViewById(R.id.article_text);
        txtContent.setText(view.getResources().getString(R.string.help_manage_resources));
        TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
        txt2.setText(view.getResources().getString(R.string.help_manage_resources_2));
        TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
        txt3.setText(view.getResources().getString(R.string.help_manage_resources_3));
        TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
        txt4.setText(view.getResources().getString(R.string.help_manage_resources_4));
    }

    @Override
    public void addImageContent(View view) {
        ImageView img = (ImageView)view.findViewById(R.id.article_image);
//		img.setImageDrawable(getResources().getDrawable(R.drawable.help_manage_resources));
        img.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_manage_resources, null));
        ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
//		imgView2.setImageDrawable(getResources().getDrawable(R.drawable.help_use_resource_home));
        imgView2.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_use_resource_home, null));
        ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
//		imgView3.setImageDrawable(getResources().getDrawable(R.drawable.help_use_resource_item));
        imgView3.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_use_resource_item, null));
        ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
//		imgView4.setImageDrawable(getResources().getDrawable(R.drawable.help_use_resource_item));
        imgView4.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_use_resource_item, null));
    }
}
