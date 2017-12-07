package uwi.dcit.AgriExpenseTT.fragments.help;

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

public class HelpNewCropCycleFragment extends HelpMenuItem {//Fragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.fragment_help_article_view_layout4, container, false);
		ContextForContent context_fc = new ContextForContent(new OperationAddNewCropCycleContent());
		context_fc.executeStrategy(view);
        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help New Crop Cycle Fragment");
		return view;
	}

}
