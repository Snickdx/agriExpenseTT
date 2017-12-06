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

    public  List<SQLiteDBModel> getDefaults(SQLiteDatabase db){
        List<SQLiteDBModel> defaults = new ArrayList<>();

        defaults.add(createContract("Fertilizer","Fersan (7.12.40 + 1TEM)"));
        defaults.add(createContract("Fertilizer","Magic Grow (7.12.40 + TE HYDROPHONIC)"));
        defaults.add(createContract("Fertilizer","Hydro YARA Liva (15.0.15)"));
        defaults.add(createContract("Fertilizer","Techni - Grow (7.12.27 + TE)"));
        defaults.add(createContract("Fertilizer","Ferqidd (10.13.32 + TE)"));
        defaults.add(createContract("Fertilizer","Plant Prod (7.12.27 + TE)"));
        defaults.add(createContract("Fertilizer","Flower Plus (9.18.36 + TE)"));
        defaults.add(createContract("Fertilizer","Iron Chelate Powder (FE - EDTA)"));
        defaults.add(createContract("Fertilizer","Magnesium Sulphate (Mg SO4)"));
        defaults.add(createContract("Fertilizer","12-24-12 FERTILIZER"));
        defaults.add(createContract("Fertilizer","HARVEST MORE 10-55-10"));
        defaults.add(createContract("Fertilizer","HARVEST MORE 13-0-44"));
        defaults.add(createContract("Fertilizer","HARVEST MORE 5-5-45"));
        defaults.add(createContract("Fertilizer","NPK 12-12-17"));
        defaults.add(createContract("Fertilizer","UREA 46-0-0"));
        defaults.add(createContract("Fertilizer","PLANT BOOSTER"));
        defaults.add(createContract("Fertilizer","MIRACLE GRO ALL PROPOSE PLANT FOOD"));
        defaults.add(createContract("Fertilizer","SCOTTS FLOWER AND VEGETABLE PLANT FOOD"));
        defaults.add(createContract("Soil amendment", "Cow manure"));
        defaults.add(createContract("Soil amendment", "Compost"));
        defaults.add(createContract("Soil amendment", "Gypsum"));
        defaults.add(createContract("Soil amendment", "Limestone"));
        defaults.add(createContract("Soil amendment", "Sulphur"));
        defaults.add(createContract("Soil amendment", "Molasses"));
        defaults.add(createContract("Soil amendment", "Chicken manure"));
        defaults.add(createContract("Soil amendment", "Horse manure"));
        defaults.add(createContract("Soil amendment", "Calphos"));
        defaults.add(createContract("Soil amendment", "Sharp sand"));
        defaults.add(createContract("Chemical", "Fungicide"));
        defaults.add(createContract("Chemical","Insecticide"));
        defaults.add(createContract("Chemical","Weedicide"));
        defaults.add(createContract("Chemical","Algicides"));
        defaults.add(createContract("Chemical","Antimicrobials"));
        defaults.add(createContract("Chemical","Biopesticides"));
        defaults.add(createContract("Chemical","Biocides"));
        defaults.add(createContract("Chemical","Fumigants"));
        defaults.add(createContract("Chemical","Herbicides"));
        defaults.add(createContract("Chemical","Miticides"));
        defaults.add(createContract("Chemical","Microbial pesticides"));
        defaults.add(createContract("Chemical","Molluscicides"));
        defaults.add(createContract("Chemical","Nematicides"));
        defaults.add(createContract("Chemical","Ovicides"));
        defaults.add(createContract("Chemical","Pheromones"));
        defaults.add(createContract("Chemical","Repellents"));
        defaults.add(createContract("Chemical","Rodenticides"));
        return defaults;
    }

    SQLiteDBModel createContract(String type, int country, String name){
        switch(type){
            case "County": return new CountyContract(db, country, name);
        }
        return null;
    }


}
