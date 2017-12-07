package uwi.dcit.AgriExpenseTT.fragments.help;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/6/2017.
 */

public class OperationAddIntroContent extends StrategyForContent {
    @Override
    public void addTextContent(View view) {
        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("Introduction To David Edit 7");
        TextView txtContent = (TextView)view.findViewById(R.id.article_text);
        txtContent.setText(view.getResources().getString(R.string.help_intro));
    }

    @Override
    public void addImageContent(View view) {
        ImageView img = (ImageView)view.findViewById(R.id.article_image);
        img.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_homescreen, null));
    }
}
