package uwi.dcit.AgriExpenseTT.fragments.help;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;

public class HelpIntroFragment extends HelpMenuItem {//Fragment{
	final static String ARG_POSITION = "position";
	View view;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fragment_help_article_view, container, false);
		
		TextView txtHeading = (TextView)view.findViewById(R.id.article_heading);
		txtHeading.setText("Introduction To David Edit 6");
		
		ImageView img = (ImageView)view.findViewById(R.id.article_image);
//		img.setImageDrawable(getResources().getDrawable(R.drawable.help_homescreen));
		img.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.help_homescreen, null));

		
		TextView txtContent = (TextView)view.findViewById(R.id.article_text);
		txtContent.setText(getResources().getString(R.string.help_intro));

        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help Intro Fragment");
		return view;
	}
	public void addToHelpList(){



		FragmentManager manager = getActivity().getSupportFragmentManager();
//		 getFragmentManager()
		manager.beginTransaction()
				.replace(R.id.help_lists, this)
				.addToBackStack("Help List")
				.commitNow();

	}
	@Override
    public void onStart() {
		super.onStart();
	}
}
