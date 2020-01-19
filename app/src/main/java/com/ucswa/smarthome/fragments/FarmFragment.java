package com.ucswa.smarthome.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ucswa.smarthome.R;

import me.itangqi.waveloadingview.WaveLoadingView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FarmFragment extends Fragment {

    private TextView TempFarm_TV, HumFarm_Tv;
    private ToggleButton Mwatering;
    private WaveLoadingView mWaveLoadingView;
    private TextView Co2_in_Air;
    private TextView Soil_water_Level;
    private ImageView Weather_State;
    private TextView Weather_State_Tv;

    public FarmFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_farm, container, false);
        Co2_in_Air = view.findViewById(R.id.co2_in_air);
        Soil_water_Level = view.findViewById(R.id.soil_water_level);
        Weather_State = view.findViewById(R.id.weather_state);
        Weather_State_Tv = view.findViewById(R.id.weather_state_tv);
        TempFarm_TV = view.findViewById(R.id.farm_temp_tv);
        HumFarm_Tv = view.findViewById(R.id.farm_hum_tv);
        Mwatering = view.findViewById(R.id.manual_watering_btn);
        mWaveLoadingView = view.findViewById(R.id.waveLoadingView_for_soil);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.RECTANGLE);
        mWaveLoadingView.setCenterTitleColor(Color.WHITE);
        mWaveLoadingView.setBorderWidth(20);
        mWaveLoadingView.setAmplitudeRatio(20);
        mWaveLoadingView.setAnimDuration(3000);
        mWaveLoadingView.setWaterLevelRatio(20);
        mWaveLoadingView.pauseAnimation();
        mWaveLoadingView.resumeAnimation();
        mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference ManualWatering = myRef.child("manual_watering");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    int weather_state = dataSnapshot.child("weather_state").getValue(int.class);
                    String ManualWateringState = (String) dataSnapshot.child("manual_watering").getValue();
                    float temperature = dataSnapshot.child("temp_for_farm").getValue(float.class);
                    float humidity = dataSnapshot.child("hum_for_farm").getValue(float.class);
                    float soil_water = dataSnapshot.child("soil_water_level").getValue(float.class);
                    float co2_in_air = dataSnapshot.child("co2_in_air").getValue(float.class);
                    float tank_water_level = dataSnapshot.child("tank_water_level").getValue(float.class);


                    Co2_in_Air.setText(co2_in_air + "");
                    Soil_water_Level.setText((int) soil_water + "%");
                    mWaveLoadingView.setProgressValue((int) tank_water_level);
                    mWaveLoadingView.setCenterTitle((int) tank_water_level + "%");
                    if (weather_state == 0) {
                        Weather_State_Tv.setText("Raining");
                        Weather_State.setImageResource(R.drawable.ic_rain);
                    } else {
                        Weather_State_Tv.setText("Sunny");
                        Weather_State.setImageResource(R.drawable.ic_weather);
                    }

                    if (ManualWateringState.equals("ON")) {
                        Mwatering.setChecked(true);
                    } else {
                        Mwatering.setChecked(false);

                    }

                    TempFarm_TV.setText((int) temperature + "\u00B0C");
                    HumFarm_Tv.setText((int) humidity + "%");
                } catch (Exception e) {

                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }

        });
        Mwatering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ManualWatering.setValue("ON");

                    // The toggle is enabled
                } else {
                    ManualWatering.setValue("OFF");
                    // The toggle is disabled
                }
            }
        });
        return view;
    }

}
