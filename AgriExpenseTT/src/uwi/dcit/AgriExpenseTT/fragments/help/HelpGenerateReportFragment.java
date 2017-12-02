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

public class HelpGenerateReportFragment extends HelpMenuItem {//Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_help_article_view, container, false);
		
		TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
		txtHeading.setText("Generate Reports");

		ImageView imgView1 = (ImageView) view.findViewById(R.id.article_image);
		imgView1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable
				.reportslide2, null));


		TextView txt1 = (TextView) view.findViewById(R.id.article_text);
		txt1.setText(getResources().getString(R.string.help_generate_report));



        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help Generate Report Fragment");
		return view;
	}
	public void addToHelpList(){
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.help_lists, this)
				.addToBackStack("Help List")
				.commit();
	}
}
