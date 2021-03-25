package com.example.countriesdetails;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;


import androidx.lifecycle.LiveData;

import com.example.countriesdetails.RoomDatabase.CountryDao;
import com.example.countriesdetails.RoomDatabase.CountryRoomDatabase;
import com.example.countriesdetails.RoomDatabase.Data;

import java.util.List;

public class CountryRepository {

    private CountryDao countryDao;
    private LiveData<List<Data>> allCountries;

    CountryRepository(Application application) {
        CountryRoomDatabase db = CountryRoomDatabase.getDatabase(application);
        countryDao = db.countryDao();
        allCountries = countryDao.getAllCountries();
    }

    LiveData<List<Data>> getAllCountries() {
        return allCountries;
    }

    public void deleteAll()  {
        new deleteAllCountriesAsyncTask(countryDao).execute();
    }

    public void insert (Data data) {
        new insertAsyncTask(countryDao).execute(data);
    }

    private static class insertAsyncTask extends AsyncTask<Data, Void, Void> {

        private CountryDao mAsyncTaskDao;

        insertAsyncTask(CountryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Data... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllCountriesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CountryDao mAsyncTaskDao;

        deleteAllCountriesAsyncTask(CountryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

//    Data getCountry(String name) {
//        Log.d("inside", "yes");
//        AsyncTask<String, Void, Data> data = new getCountryAsyncTask(countryDao).execute(name);
//        Log.d("jayesh", data.getClass().getName());
////        return data.getClass();
//        return null;
//    }
//
//    private static class getCountryAsyncTask extends AsyncTask<String, Void, Data> {
//
//        private CountryDao mAsyncTaskDao;
//        public Data data;
//
//        public getCountryAsyncTask(CountryDao mAsyncTaskDao) {
//            this.mAsyncTaskDao = mAsyncTaskDao;
//        }
//
//        @Override
//        protected Data doInBackground(String... strings) {
//            data = mAsyncTaskDao.getCountry(strings[0]);
//            return data;
//        }
//    }

}
