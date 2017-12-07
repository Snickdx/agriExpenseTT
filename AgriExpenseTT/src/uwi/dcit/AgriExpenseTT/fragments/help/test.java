package uwi.dcit.AgriExpenseTT.fragments.help;

//import android.support.v4.app.Fragment;
//import android.support.v4.content.res.ResourcesCompat;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by DavidCharles on 12/3/2017.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;

import java.io.Serializable;






public class test {//extends Fragment {//implements Serializable  {

//    public test(Context context){
////        this.context=context;
//    }
//


    public View addContent(View view){


        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("New Crop Cycle My Test 2 David");
        TextView txtContent = (TextView)view.findViewById(R.id.article_text);
        txtContent.setText(view.getResources().getString(R.string.help_cropcycle_new_1));
       ImageView img = (ImageView) view.findViewById(R.id.article_image);
        img.setImageDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.help_new_cropcycle_crop, null));

        return view;
    }

  /*  public void addTextContent(View view){

        TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
        txtHeading.setText("New Crop Cycle");
        this.view=view;
        TextView txtContent = (TextView)view.findViewById(R.id.article_text);
        txtContent.setText(getResources().getString(R.string.help_cropcycle_new_1));
        TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
        txt2.setText(getResources().getString(R.string.help_cropcycle_new_2));
        TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
        txt3.setText(getResources().getString(R.string.help_cropcycle_new_3));
        TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
        txt4.setText(getResources().getString(R.string.help_cropcycle_new_4));
        return view;

    }
    public void addImageContent(View view){
        ImageView img = (ImageView) view.findViewById(R.id.article_image);
        img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_new_cropcycle_crop, null));
        this.view=view;
        ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
        imgView2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_new_cropcycle_land, null));
        ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
        imgView3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_new_cropcycle_date, null));
        ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
        imgView4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_new_cropcycle_details, null));
        return view;
    }*/

}
