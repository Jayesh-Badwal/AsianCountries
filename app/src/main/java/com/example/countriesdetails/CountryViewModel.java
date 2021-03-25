package com.example.countriesdetails;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.countriesdetails.RoomDatabase.Data;

import java.util.List;

public class CountryViewModel extends AndroidViewModel {

    private CountryRepository countryRepository;

    private LiveData<List<Data>> allCountries;

    public CountryViewModel (Application application) {
        super(application);
        countryRepository = new CountryRepository(application);
        allCountries = countryRepository.getAllCountries();
    }

    LiveData<List<Data>> getAllCountries() {
        return allCountries;
    }

//    Data getCountry(String name) {
//        return countryRepository.getCountry(name);
//    }

    public void insert(Data data) { countryRepository.insert(data); }

    public void deleteAll() {countryRepository.deleteAll();}

}
