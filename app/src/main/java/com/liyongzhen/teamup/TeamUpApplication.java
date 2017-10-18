package com.liyongzhen.teamup;

import android.app.Application;

/**
 * Created by Administrator on 8/16/2017.
 */

public class TeamUpApplication extends Application {

    private static TeamUpApplication sharedApplication;
//    private static ArrayList<CurrencyModel> currencyModels;
//    public static DBHelper mDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
//        AndroidNetworking.initialize(getApplicationContext());
//        sharedApplication = this;
//        currencyModels = new ArrayList<>();
//        mDbHelper = new DBHelper(this);
//
//        Intent i = new Intent(this, CryptoService.class);
//        startService(i);
    }

    public static TeamUpApplication getSharedApplication() {
        return sharedApplication;
    }

//    public static void setCurrencyModels(ArrayList<CurrencyModel> models) {
//        currencyModels.clear();
//        currencyModels.addAll(models);
//    }
//
//    public static ArrayList<CurrencyModel> getCurrencyModels() {
//        return currencyModels;
//    }
//
//    public static ArrayList<String> getCurrencyNames() {
//        ArrayList<String> names = new ArrayList<>();
//        for (CurrencyModel item: currencyModels) {
//            names.add(item.getCoinFullName());
//        }
//        return names;
//    }
}
