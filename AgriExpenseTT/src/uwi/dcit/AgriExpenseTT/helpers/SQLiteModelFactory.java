package uwi.dcit.AgriExpenseTT.helpers;

import java.util.ArrayList;
import java.util.List;


import uwi.dcit.AgriExpenseTT.models.SQLiteDBModel;


public class SQLiteModelFactory {
    private static final SQLiteModelFactory ourInstance = new SQLiteModelFactory();

    public static SQLiteModelFactory getInstance() {
        return ourInstance;
    }

    public List<SQLiteDBModel> contractTypes = new ArrayList<>();


//    public SQLiteModelFactory(SQLiteDatabase db) {
//        contractTypes.add(new ResourceContract(db));
//        contractTypes.add(new ResourceContract(db));
//        contractTypes.add(new ResourceContract(db));
//        contractTypes.add(new ResourceContract(db));
//        contractTypes.add(new ResourceContract(db));
//    }
//
//    Contract createContract(String type, String name){
////        switch(type){
////            case "Planting material": return new MateralContract(name);
////                break;
////            case "Chemical": return new ChemicalContract(name);
////                break;
////            case "Soil amendment": return new SoilContract(name);
////                break;
////            case "Labour": return new LabourContract(name);
////                break;
////            case "Country": return new CountryContract(name);
////                break;
////            case "County": return new CountyContract(name);
////                break;
////            case "Fertilizer": return new FertilizerContract(name);
////                break;
////        }
//        return new ResourceContract(db);
//    }
}
