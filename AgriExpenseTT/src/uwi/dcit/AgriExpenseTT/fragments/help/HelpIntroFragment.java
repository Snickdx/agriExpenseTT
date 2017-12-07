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
		ContextForContent context_fc = new ContextForContent(new OperationAddIntroContent());
		context_fc.executeStrategy(view);
        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help Intro Fragment");
		return view;
	}

	@Override
    public void onStart() {
		super.onStart();
	}
}
