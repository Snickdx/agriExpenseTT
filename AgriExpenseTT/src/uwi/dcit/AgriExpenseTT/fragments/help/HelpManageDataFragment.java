package uwi.dcit.AgriExpenseTT.fragments.help;

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

public class HelpManageDataFragment extends HelpMenuItem {//Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_help_article_view_layout4, container, false);
		
		TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
		txtHeading.setText("Manage Data");
		
		ImageView imgView1 =(ImageView)view.findViewById(R.id.article_image);
//		imgView1.setImageDrawable(getResources().getDrawable(R.drawable.help_manage_data));
		imgView1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_manage_data, null));
		
		TextView txt1 = (TextView)view.findViewById(R.id.article_text);
		txt1.setText(getResources().getString(R.string.help_manage_data_1));
		
		ImageView imgView2 =(ImageView)view.findViewById(R.id.article_image_2);
//		imgView2.setImageDrawable(getResources().getDrawable(R.drawable.help_manage_data_edit_cycle));
		imgView2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_manage_data_edit_cycle, null));
		
		TextView txt2 = (TextView)view.findViewById(R.id.article_text_2);
		txt2.setText(getResources().getString(R.string.help_manage_data_2));
		
		ImageView imgView3 =(ImageView)view.findViewById(R.id.article_image_3);
//		imgView3.setImageDrawable(getResources().getDrawable(R.drawable.help_manage_data_editpurchases_details));
		imgView3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_manage_data_editpurchases_details, null));
		
		TextView txt3 = (TextView)view.findViewById(R.id.article_text_3);
		txt3.setText(getResources().getString(R.string.help_manage_data_3));
		
		ImageView imgView4 =(ImageView)view.findViewById(R.id.article_image_4);
//		imgView4.setImageDrawable(getResources().getDrawable(R.drawable.help_manage_data_delete_record));
		imgView4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_manage_data_delete_record, null));
		
		TextView txt4 = (TextView)view.findViewById(R.id.article_text_4);
		txt4.setText(getResources().getString(R.string.help_manage_data_4));

        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help Manage Data Fragment");
        
		return view;
	}

}
