package com.example.countriesdetails.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.countriesdetails.MainActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@Database(entities = {Data.class}, version = 1, exportSchema = false)
public abstract class CountryRoomDatabase extends RoomDatabase {

    public abstract CountryDao countryDao();

    private static CountryRoomDatabase INSTANCE;

    public static CountryRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CountryRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CountryRoomDatabase.class, "country_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CountryDao mDao;

        PopulateDbAsync(CountryRoomDatabase db) {
            mDao = db.countryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created

            // If we have no words, then create the initial list of words
            if (mDao.getAnyCountry().length < 1) {
//                API call
                loadData();
            }
//            mDao.deleteAll();
            return null;
        }

        void loadData() {
            Request req = new Request.Builder().url("https://restcountries.eu/rest/v2/region/asia").get().build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(req).enqueue(new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d("no internet", "Request Failed");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if(!response.isSuccessful()) {
                        Log.d("response", "error in request");
                    }
                    String respFromAPI = response.body().string();
                    Log.d("RawData", respFromAPI);
                    try {
                        extractData(respFromAPI);
                    } catch (Exception e) {
                        Log.d("Error", "Not in json format");
                    }
                }
            });
        }

        void extractData(String respFromAPI) throws Exception{
            Log.d("Mydata", respFromAPI);
            JSONArray asianCountries = new JSONArray(respFromAPI);

            for(int  i = 0; i < asianCountries.length(); i++) {
                JSONObject jsonObject = asianCountries.getJSONObject(i);
                String a = jsonObject.toString();
                Log.d("msg", a);
                String name = jsonObject.getString("name");
                String region = jsonObject.getString("region");
                String subregion = jsonObject.getString("subregion");
                String capital = jsonObject.getString("capital");
                String flag = jsonObject.getString("flag");
                JSONArray bor = jsonObject.getJSONArray("borders");
                JSONArray lang = jsonObject.getJSONArray("languages");
                String borders = "";
                String languages = "";
                for(int j = 0; j < bor.length(); j++) {
//                borders += obj.getString(bor.getString(j), "Jayesh");
                    borders += bor.getString(j);
                    if(j < bor.length() - 1) {
                        borders += ", ";
                    }
                    Log.d("border" + (j + 1), borders);
                }
                for(int j = 0; j < lang.length(); j++) {
                    JSONObject lan = lang.getJSONObject(j);
                    languages += lan.getString("name");
                    if(j < lang.length() - 1) {
                        languages += ", ";
                    }
                    Log.d("language" + (j + 1), languages);
                }
                long population = jsonObject.getLong("population");

                Data data = new Data(name, capital, flag, region, subregion, borders, languages, population);
                mDao.insert(data);
                Log.d("AllCountries", name + " " + region + " " + subregion + " " + flag + " " + capital + " " + String.valueOf(population));
            }
        }
    }

}
