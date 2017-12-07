package uwi.dcit.AgriExpenseTT.fragments.help;

import android.view.View;

/**
 * Created by DavidCharles on 12/3/2017.
 */

public class ContextForContent {

    private StrategyForContent strategy_fc;

    public ContextForContent(StrategyForContent strategy_fc){
        this.strategy_fc = strategy_fc;
    }

    public void executeStrategy(View view){
        strategy_fc.addTextContent(view);
        strategy_fc.addImageContent(view);
    }




}
