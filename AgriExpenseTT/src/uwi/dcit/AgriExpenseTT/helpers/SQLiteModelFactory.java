package uwi.dcit.AgriExpenseTT.helpers;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


import uwi.dcit.AgriExpenseTT.models.CloudKeyContract;
import uwi.dcit.AgriExpenseTT.models.CountryContract;
import uwi.dcit.AgriExpenseTT.models.CountyContract;
import uwi.dcit.AgriExpenseTT.models.ResourceContract;
import uwi.dcit.AgriExpenseTT.models.SQLiteDBModel;


public class SQLiteModelFactory {
    private static SQLiteModelFactory ourInstance = null;

    SQLiteDatabase db;


    public static SQLiteModelFactory getInstance(SQLiteDatabase db) {
        if(ourInstance == null){
            ourInstance = new SQLiteModelFactory(db);
        }
        return ourInstance;
    }

    public List<SQLiteDBModel> contractTypes = new ArrayList<>();


    private SQLiteModelFactory(SQLiteDatabase db) {
        this.db = db;
        contractTypes.add(new ResourceContract(db));
        contractTypes.add(new CloudKeyContract(db));
        contractTypes.add(new CountryContract(db));
        contractTypes.add(new CountyContract(db));
    }



    SQLiteDBModel createContract(String type, String name){
        switch(type){
            case "Planting material": return new ResourceContract(db, name, type);
            case "Chemical": return new ResourceContract(db, name, type);
            case "Soil amendment": new ResourceContract(db, name, type);
            case "Country": return new CountryContract(db, name, type);
            case "Fertilizer": new ResourceContract(db, name, type);
        }
        return new ResourceContract(db);
    }

    SQLiteDBModel createContract(String type, int country, String name){
        switch(type){
            case "County": return new CountyContract(db, country, name);
        }
        return null;
    }


}
