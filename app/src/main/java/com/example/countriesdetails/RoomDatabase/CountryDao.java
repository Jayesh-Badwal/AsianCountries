package com.example.countriesdetails.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Data data);

    @Query("DELETE FROM country_data")
    void deleteAll();

    @Query("SELECT * from country_data ORDER BY name ASC")
    LiveData<List<Data>> getAllCountries();

    @Query("SELECT * from country_data LIMIT 1")
    Data[] getAnyCountry();

    @Query(("SELECT * from country_data WHERE name LIKE :name"))
    Data getCountry(String name);

}
