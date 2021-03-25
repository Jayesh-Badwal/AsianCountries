package com.example.countriesdetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.countriesdetails.RoomDatabase.Data;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICountryRVAdapter {

    private CountryViewModel countryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        countryViewModel = ViewModelProviders.of(this).get(CountryViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CountryListAdapter adapter = new CountryListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryViewModel.getAllCountries().observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(@Nullable final List<Data> data) {
                // Update the cached copy of the words in the adapter.
                adapter.setNames(data);
            }
        });
    }

    @Override
    public void onItemClick(Data data) {
        Intent intent = new Intent(getApplicationContext(), CountryData.class);
        intent.putExtra("name", data.getName());
        intent.putExtra("capital", data.getCapital());
        intent.putExtra("subregion", data.getSubregion());
        intent.putExtra("region", data.getRegion());
        intent.putExtra("borders", data.getBorders());
        intent.putExtra("languages", data.getLanguages());
        intent.putExtra("population", String.valueOf(data.getPopulation()));
        intent.putExtra("flag", data.getFlag());
        startActivity(intent);
    }

    public void deleteAllData(View view) {
        countryViewModel.deleteAll();
    }
}