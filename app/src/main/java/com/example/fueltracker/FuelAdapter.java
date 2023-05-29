package com.example.fueltracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.ViewHolder> {
    private List<MainActivity.Fuel> fuelList;

    public FuelAdapter(List<MainActivity.Fuel> fuelList) {
        this.fuelList = fuelList;
    }

    public void setFuelList(List<MainActivity.Fuel> fuelList) {
        this.fuelList = fuelList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fuel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity.Fuel fuel = fuelList.get(position);

        holder.textFuelName.setText(fuel.getFuelName());

        LinearLayout gasStationsLayout = holder.gasStationsLayout;
        gasStationsLayout.removeAllViews();

        List<MainActivity.GasStation> gasStations = fuel.getGasStations();
        for (MainActivity.GasStation gasStation : gasStations) {
            View gasStationItem = LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_gas_station, gasStationsLayout, false);
            TextView textGasStationName = gasStationItem.findViewById(R.id.text_gas_station_name);
            TextView textGasStationPrice = gasStationItem.findViewById(R.id.text_gas_station_price);

            textGasStationName.setText(gasStation.getName());
            textGasStationPrice.setText(gasStation.getPrice());

            gasStationsLayout.addView(gasStationItem);
        }
    }

    @Override
    public int getItemCount() {
        return fuelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFuelName;
        LinearLayout gasStationsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textFuelName = itemView.findViewById(R.id.text_fuel_name);
            gasStationsLayout = itemView.findViewById(R.id.table_gas_stations);

        }
    }
}
