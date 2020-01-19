package com.ucswa.smarthome.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class DustbinFragment extends Fragment {
    private ImageView DImageView;
    private WaveLoadingView mWaveLoadingView;
    private TextView DustBinDegreeTv;
    private TextView Temp_for_dustbin;
    private TextView Hum_for_dustbin;
    private int constant_dustbin_level = 39;
    private int dust_level = 0;
    private int dust_percentage = 0;

    public DustbinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dust_bin, container, false);
        DImageView = view.findViewById(R.id.dustbin);
        DustBinDegreeTv = view.findViewById(R.id.dustbin_degree);
        Temp_for_dustbin = view.findViewById(R.id.temp_dustbin_tv);
        Hum_for_dustbin = view.findViewById(R.id.hum_dustbin_tv);
        DustBinDegreeTv.setText("Dustbin");
        mWaveLoadingView = view.findViewById(R.id.waveLoadingView);
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


        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    float dust_level_from_top = dataSnapshot.child("dust_level").getValue(float.class);
                    float dust_rotation_degree = dataSnapshot.child("dustbin_rotation_degree").getValue(float.class);
                    float temperature = dataSnapshot.child("temp_for_dustbin").getValue(float.class);
                    float humidity = dataSnapshot.child("hum_for_dustbin").getValue(float.class);
                    Temp_for_dustbin.setText((int) temperature + "\u00B0C");
                    Hum_for_dustbin.setText((int) humidity + "%");

                    dust_level = constant_dustbin_level - (int) dust_level_from_top;
                    dust_percentage = dust_level * 100 / 39;

                    mWaveLoadingView.setProgressValue((int) dust_percentage);
                    mWaveLoadingView.setCenterTitle((int) dust_percentage + "%");

                    if (dust_rotation_degree != 1) {
                        DImageView.setRotation(45);
                        DustBinDegreeTv.setText("Your Dustbin is down!");
                    } else {
                        DImageView.setRotation(0);
                        DustBinDegreeTv.setText("Dustbin");
                    }

                } catch (Exception e) {

                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }

        });

        // Inflate the layout for this fragment
        return view;
    }

}
