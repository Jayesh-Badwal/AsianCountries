package com.example.countriesdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CountryData extends AppCompatActivity {

    String cName, cCapital, cRegion, cSubregion, cBorders, cLanguages, cFlag, cPopulation;
    TextView name, capital, region, subregion, population, borders, languages;
    ImageView flag;
    private static OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);

        Intent intent = getIntent();
        cName = intent.getStringExtra("name");
        cCapital = intent.getStringExtra("capital");
        cRegion = intent.getStringExtra("region");
        cSubregion = intent.getStringExtra("subregion");
        cBorders = intent.getStringExtra("borders");
        cLanguages = intent.getStringExtra("languages");
        cPopulation = intent.getStringExtra("population");
        cFlag = intent.getStringExtra("flag");

        name = findViewById(R.id.name);
        capital = findViewById(R.id.capital);
        region = findViewById(R.id.region);
        subregion = findViewById(R.id.subregion);
        population = findViewById(R.id.population);
        borders = findViewById(R.id.borders);
        languages = findViewById(R.id.languages);
        flag = findViewById(R.id.imageView);

//        Picasso.get().load(cFlag).into(flag);
        fetchSvg(this, cFlag, flag);

        name.setText(cName);
        capital.setText("Capital: " + cCapital);
        subregion.setText("Sub-region: " + cSubregion);
        region.setText("Region: " + cRegion);
        borders.setText("Shared borders: " + cBorders);
        languages.setText("Languages spoken: " + cLanguages);
        population.setText("Population: " + cPopulation);


    }

    public static void fetchSvg(Context context, String url, ImageView flag) {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1014))
                    .build();
        }

        // here we are making HTTP call to fetch data from URL.
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // we are adding a default image if we gets any error.
                flag.setImageResource(R.drawable.no_internet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // sharp is a library which will load stream which we generated
                // from url in our target imageview.
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(flag);
                stream.close();
            }
        });
    }
}