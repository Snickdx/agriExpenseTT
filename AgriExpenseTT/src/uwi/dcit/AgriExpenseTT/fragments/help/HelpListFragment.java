package uwi.dcit.AgriExpenseTT.fragments.help;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;
import uwi.dcit.AgriExpenseTT.helpers.HelpTopics;

public class HelpListFragment extends ListFragment{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        // Create an array adapter for the list view, using the Topics array
		setListAdapter(new ArrayAdapter<>(getActivity(), layout, HelpTopics.Topics)); //TODO Convert this to a String Array XML rather than a programatic constant
		GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Help List Fragment");
    }
	
	@Override
    public void onStart() {
        super.onStart();
        this.setupListeners();
	}
	
	public void setupListeners(){
        this.getListView().setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

				HelpMenuItemFactory hmi_f = new HelpMenuItemFactory();
				HelpMenuItem hmi = hmi_f.getMenuItem(position);
				if(hmi!=null)
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.help_lists, hmi)
							.addToBackStack("Help List")
							.commit();
				else
					Toast.makeText(getActivity(), "Help Topic not found", Toast.LENGTH_SHORT).show();
				/*
				Fragment frag = null;
				Intent intent;

				switch (position){
					case 0:
//						intent =new Intent(getActivity(), IntroductionSlides.class);
//						startActivity(intent);
						frag = new HelpIntroFragment();
						break;
					case 1:
//						intent = new Intent(getActivity(), NewPurchaseSlides.class);
//						startActivity(intent);
						frag = new HelpNewPurchaseFragment();
						break;
					case 2:
//						intent = new Intent(getActivity(), NewCropCycleSlides.class);
//						startActivity(intent);
						frag = new HelpNewCropCycleFragment();
						break;
					case 3:
//						intent = new Intent(getActivity(), ManageResourceSlides.class);
//						startActivity(intent);
						frag = new HelpManageResourceFragment();
						break;
					case 4:
//						intent = new Intent(getActivity(), HiringLabourSlides.class);
//						startActivity(intent);
						frag = new HelpHiringLabourFragment();
						break;
					case 5:
//						intent = new Intent(getActivity(), CalculateSalesSlides.class);
//						startActivity(intent);
						frag = new HelpManageDataFragment();
						break;
					case 6:
//						intent = new Intent(getActivity(), GeneratingReportSlides.class);
//						startActivity(intent);
						frag = new HelpCalculateSalesFragment();
						break;
					case 7:
						frag = new HelpGenerateReportFragment();
						break;
					default:
						Toast.makeText(getActivity(), "Help Topic not found", Toast.LENGTH_SHORT).show();
						break;
				}
				
				
				if (frag != null)
					getFragmentManager()
						.beginTransaction()
						.replace(R.id.help_lists, frag)
						.addToBackStack("Help List")
						.commit();
				*/
				
			}
        });
	}
}
