package uwi.dcit.AgriExpenseTT.fragments.help;

import android.content.Context;

/**
 * Created by DavidCharles on 12/2/2017.
 */

public class HelpMenuItemFactory {

    HelpMenuItem getMenuItem(int position){

        if(position==0)
            return new HelpIntroFragment();
        if(position==1)
            return new HelpNewPurchaseFragment();
        if(position==2){
            return  new HelpNewCropCycleFragment();
        }
        if(position==3)
            return new HelpManageResourceFragment();
        if(position==4)
            return new HelpHiringLabourFragment();
        if(position==5)
            return new HelpManageDataFragment();
        if(position==6)
            return new HelpCalculateSalesFragment();
        if(position==7)
            return new HelpGenerateReportFragment();
        return null;

    }

}
