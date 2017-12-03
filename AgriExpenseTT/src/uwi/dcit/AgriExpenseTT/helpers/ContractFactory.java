package uwi.dcit.AgriExpenseTT.helpers;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uwi.dcit.AgriExpenseTT.models.Contract;
import uwi.dcit.AgriExpenseTT.models.CountryContract;
import uwi.dcit.AgriExpenseTT.models.CountyContract;
import uwi.dcit.AgriExpenseTT.models.ResourceContract;


public class ContractFactory {
    private static final ContractFactory ourInstance = new ContractFactory();

    public static ContractFactory getInstance() {
        return ourInstance;
    }

    public List<Contract> contractTypes = new ArrayList<>();


//    public ContractFactory(SQLiteDatabase db) {
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
