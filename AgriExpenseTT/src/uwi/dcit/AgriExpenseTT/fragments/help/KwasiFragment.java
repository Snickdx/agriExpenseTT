package uwi.dcit.AgriExpenseTT.fragments.help;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by DavidCharles on 12/4/2017.
 */

public class KwasiFragment extends HelpMenuItem {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getContext();

        TestClassV9 testClassV9 = new TestClassV9(context);
        TestClassV9Item item1 = new TestClassV9Item(context);
        TestClassV9Item item2 = new TestClassV9Item(context);
        TestClassV9Item item3= new TestClassV9Item(context);
        item1.setHeading("Head1");
        item2.setHeading("Head2");
        item3.setHeading("Head3");
        testClassV9.addItem(item1.getView());
        testClassV9.addItem(item2.getView());
        testClassV9.addItem(item3.getView());
        return  testClassV9.getView();
    }
}
