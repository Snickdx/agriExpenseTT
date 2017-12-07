package uwi.dcit.AgriExpenseTT.fragments.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uwi.dcit.AgriExpenseTT.R;

public class HelpCalculateSalesFragment extends HelpMenuItem {//Fragment {
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_help_article_view, container, false);
        ContextForContent context_fc = new ContextForContent(new OperationAddCalculateSalesContent());
        context_fc.executeStrategy(view);
        return view;
    }

}
