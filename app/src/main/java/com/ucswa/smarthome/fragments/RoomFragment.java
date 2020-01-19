package com.ucswa.smarthome.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ucswa.smarthome.BathRoom;
import com.ucswa.smarthome.BedRoom;
import com.ucswa.smarthome.Garage;
import com.ucswa.smarthome.Kitchen;
import com.ucswa.smarthome.LivingRoom;
import com.ucswa.smarthome.R;
import com.ucswa.smarthome.RestRoom;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {
    private CardView bedRoomCV;
    private CardView bathRoomCV;
    private CardView livingRoomCV;
    private CardView kitchenCV;
    private CardView restRoomCV;
    private CardView garageCV;

    public RoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_room, container, false);
        bedRoomCV = view.findViewById(R.id.bedroom_card);
        bathRoomCV = view.findViewById(R.id.bathroom_card);
        livingRoomCV = view.findViewById(R.id.livingroom_card);
        kitchenCV = view.findViewById(R.id.kitchen_card);
        restRoomCV = view.findViewById(R.id.restroom_card);
        garageCV = view.findViewById(R.id.garage);
        bedRoomCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), BedRoom.class);
                view.getContext().startActivity(intent);
                //   getActivity().finish();
            }
        });
        bathRoomCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), BathRoom.class);
                view.getContext().startActivity(intent);
            }
        });
        livingRoomCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LivingRoom.class);
                view.getContext().startActivity(intent);
            }
        });
        kitchenCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Kitchen.class);
                view.getContext().startActivity(intent);
            }
        });
        restRoomCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), RestRoom.class);
                view.getContext().startActivity(intent);
            }
        });
        garageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Garage.class);
                view.getContext().startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("Frontales","resume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.e("Frontales","Pause");
//
//    }
}
