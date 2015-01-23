package uwi.dcit.AgriExpenseTT.fragments;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dcit.agriexpensett.rPurchaseApi.model.RPurchase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;
import uwi.dcit.AgriExpenseTT.helpers.GAnalyticsHelper;
import uwi.dcit.AgriExpenseTT.helpers.NavigationControl;
import uwi.dcit.AgriExpenseTT.models.LocalCycle;
import uwi.dcit.AgriExpenseTT.models.LocalCycleUse;


public class FragmentCycleUseCategory extends Fragment {
	TextView catMain;
	TextView catDet1;
	TextView catDet2;
	Button btn_useage;
	Button btn_useMore;
	View view;
	String category;
	LocalCycle currCycle;
	Double catTotal=0.0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_cycleuse_category_card, container, false);
		initialSetup();
		calculate();
		setupClick();
		//cycleId or cycleObject
		//category
        GAnalyticsHelper.getInstance(this.getActivity()).sendScreenView("Cycle Use Category Fragment");
		return view;
	}
	

	private void initialSetup() {
		//getting views
		catMain=(TextView)view.findViewById(R.id.tv_cycle_catMain);
		catDet1=(TextView)view.findViewById(R.id.tv_cycle_catDet1);;
		catDet2=(TextView)view.findViewById(R.id.tv_cycle_catDet2);;
		btn_useage=(Button)view.findViewById(R.id.btn_Cycle_useage);
		btn_useMore=(Button)view.findViewById(R.id.btn_Cycle_useMore);
		View line=view.findViewById(R.id.line);
		//getting data
		category=getArguments().getString("category");
		
		if(category.equals(DHelper.cat_labour)){
			btn_useage.setText(category+" usage");
			btn_useMore.setText("Add Labour");
			line.setBackgroundColor(Color.parseColor(DHelper.colour_labour));
			btn_useMore.setBackgroundResource(R.drawable.btn_custom_labour);
		}else if(category.equals(DHelper.cat_other)){
			btn_useage.setText("useage of"+category);
			btn_useMore.setText("Add other expense");
			line.setBackgroundColor(Color.parseColor(DHelper.colour_other));
			btn_useMore.setBackgroundResource(R.drawable.btn_custom_other);
		}else{
			btn_useage.setText(category+" usage");
			btn_useMore.setText("Use more "+category);
			if(category.equals(DHelper.cat_plantingMaterial)){
				btn_useMore.setBackgroundResource(R.drawable.btn_custom_plantmaterial);
				line.setBackgroundColor(Color.parseColor(DHelper.colour_pm));
			}else if(category.equals(DHelper.cat_chemical)){
				btn_useMore.setBackgroundResource(R.drawable.btn_custom_chem);
				line.setBackgroundColor(Color.parseColor(DHelper.colour_chemical));
			}else if(category.equals(DHelper.cat_fertilizer)){
				btn_useMore.setBackgroundResource(R.drawable.btn_custom_fertilizer);
				line.setBackgroundColor(Color.parseColor(DHelper.colour_fertilizer));
			}else if(category.equals(DHelper.cat_soilAmendment)){
				btn_useMore.setBackgroundResource(R.drawable.btn_custom_soilam);
				line.setBackgroundColor(Color.parseColor(DHelper.colour_soilam));
			}
		}
		//getArguments().getParcelable("Cycle");
		currCycle=getArguments().getParcelable("cycle");
		
		//default texts
		catMain.setText(category);
		
	}
	
	private void calculate() {
		DbHelper dbh=new DbHelper(getActivity().getBaseContext());
		SQLiteDatabase db=dbh.getReadableDatabase();
		//getting aggregate and complex data 
		ArrayList<LocalCycleUse> useList=new ArrayList<LocalCycleUse>();
		DbQuery.getCycleUse(db, dbh, currCycle.getId(), useList,category);//fills list with currCycle uses of type category
		//DbQuery.getCycleUse(db, dbh, cycleid, list, type);
		ArrayList<String> Names=null;
		double[] Totals=null;
		if(!(useList.isEmpty())){
			Names=new ArrayList<String>();
			Totals=new double[useList.size()];
			Iterator<LocalCycleUse> itr=useList.iterator();
			while(itr.hasNext()){
				LocalCycleUse lcu=itr.next();
				catTotal+=lcu.getUseCost();//stores the total amount of money spent on plantMaterials
						
				RPurchase purchaseUse=DbQuery.getARPurchase(db, dbh,lcu.getPurchaseId());
				String name=DbQuery.findResourceName(db, dbh, purchaseUse.getResourceId());
						
				//calculates the total spent on each plantMaterial
				Iterator<String> i=Names.iterator();//list of plantMaterial names' iterator
				int pos=0;//start position for totals corresponding to each name
				boolean found=false;
				while(i.hasNext()){//goes through the names of the plantMaterials
					if(name.equals(i.next())){
						Totals[pos]+=lcu.getUseCost();
						found=true;
					}else{
						pos++;
					}
				}
				if(found==false){//if we didnt find the name in the list
					Names.add(name);//add the name to the list
					Totals[pos]=lcu.getUseCost();//set the corresponding cost
				}
			}
		}
		//----------------------SETUP SUB CATEGORYS IF ANY
		DecimalFormat df = new DecimalFormat("#.00"); 
		catDet1.setText("$"+Double.valueOf(df.format(catTotal))+" has been spent on "+category+" for this cycle so far");
		catDet2.setText("No main expense yet");
		if(Names!=null){
			int x=0,maxPos=0;
			Iterator<String> namesItr=Names.iterator();
			while(namesItr.hasNext()){
				if(Totals[x]>Totals[maxPos]){
					maxPos=x;
				}
				System.out.println(category+" "+namesItr.next());
				x++;
			}
			catDet2.setText("The most amount of money was spent on "
			+Names.get(maxPos)+" which costed $"+Double.valueOf(df.format(Totals[maxPos])));
		}else{
			catDet2.setText("No main expense yet");
		}
	}
	
	private void setupClick() {
		Click click=new Click();
		btn_useage.setOnClickListener(click);
		btn_useMore.setOnClickListener(click);

	}
	public class Click implements OnClickListener{

		@Override
		public void onClick(View v) {
            Fragment newFrag=null;
            if(v.getId()==R.id.btn_Cycle_useage){
				//Intent n=new Intent(getActivity(),ViewCycleUsege.class);
				Bundle arguments = new Bundle();
                arguments .putString("type", category);
				currCycle=getArguments().getParcelable("cycle");
				arguments.putString("id", "" + currCycle.getId());

			}else if(v.getId()==R.id.btn_Cycle_useMore){
//				IntentLauncher launcher=new IntentLauncher();
//				launcher.start();
                newFrag=new FragmentUseResource();
                Bundle arguments = new Bundle();
                arguments.putString("type",category);
                arguments.putParcelable("cycle", currCycle);
                arguments.putString("total",""+catTotal);
                newFrag.setArguments(arguments);
			}

            if(getActivity() instanceof NavigationControl) {
                if(((NavigationControl) getActivity()).getRightFrag() instanceof  FragmentEmpty
                        ||(((NavigationControl) getActivity()).getRightFrag().getClass()==newFrag.getClass()))
                    ((NavigationControl) getActivity()).navigate(((NavigationControl) getActivity()).getLeftFrag(),newFrag);
                else
                    ((NavigationControl) getActivity()).navigate(((NavigationControl) getActivity()).getRightFrag(),newFrag);
            }
		}
	}
	/*private class IntentLauncher extends Thread{
		@Override
		public void run(){
			Bundle b=new Bundle();
			b.putParcelable("cyc",currCycle);
			Intent n=new Intent(getActivity(),UseResource.class);
			n.putExtra("cyc",b);
			n.putExtra("type",category);
			n.putExtra("total",""+catTotal);
			getActivity().startActivity(n);
			getActivity().finish();
		}
	}
*/
}
