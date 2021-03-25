package com.example.countriesdetails;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;

import com.example.countriesdetails.RoomDatabase.Data;

import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    private final LayoutInflater mInflater;
    private List<Data> data; // Cached copy of words
    ICountryRVAdapter listener;

    CountryListAdapter(Context context, ICountryRVAdapter listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {
        private final TextView countryItemView;

        private CountryViewHolder(View itemView) {
            super(itemView);
            countryItemView = itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        CountryViewHolder viewHolder = new CountryViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        if (data != null) {
            Data current = data.get(position);
            holder.countryItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.countryItemView.setText("No Data");
        }
    }

    void setNames(List<Data> words){
        data = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else return 0;
    }
}

interface ICountryRVAdapter {
    public void onItemClick(Data data);
}
