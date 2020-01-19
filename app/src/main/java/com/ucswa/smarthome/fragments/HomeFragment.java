package com.ucswa.smarthome.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ucswa.smarthome.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView greetingTv;
    private TextView AvgTemp;
    private TextView AvgHumi;
    private CardView emergency_OFF;
    private ImageView Day_Night_img;
    private TextView Device_nun;
    private TextView USER_NAME;
    private int i = 4;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        USER_NAME = view.findViewById(R.id.user_name);
        emergency_OFF = view.findViewById(R.id.emergency_off_btn);
        greetingTv = view.findViewById(R.id.greeting_tv);
        AvgTemp = view.findViewById(R.id.avg_temp);
        AvgHumi = view.findViewById(R.id.avg_humidity);
        Day_Night_img = view.findViewById(R.id.day_night_view);
        Device_nun = view.findViewById(R.id.no_devices);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingTv.setText("Good Morning");
            Day_Night_img.setImageResource(R.drawable.ic_morning);

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingTv.setText("Good Afternoon");
            Day_Night_img.setImageResource(R.drawable.ic_afternoon);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingTv.setText("Good Evening");
            Day_Night_img.setImageResource(R.drawable.ic_sunset);

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetingTv.setText("Good Night");
            Day_Night_img.setImageResource(R.drawable.ic_night);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference bedroomLED = myRef.child("led_status_bedroom");
        final DatabaseReference bathroomLED = myRef.child("led_status_bathroom");
        final DatabaseReference livingroomLED = myRef.child("led_status_livingroom");
        final DatabaseReference kitchenLED = myRef.child("led_status_kitchen");
        final DatabaseReference restroomLED = myRef.child("led_status_restroom");
        final DatabaseReference garageLED = myRef.child("led_status_garage");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    float temperature = dataSnapshot.child("temp_for_livingroom").getValue(float.class);
                    float humidity = dataSnapshot.child("hum_for_livingroom").getValue(float.class);
                    String USER = (String) dataSnapshot.child("database").child("user_name").getValue();
                    USER_NAME.setText(USER);
                    Device_nun.setText(i + "");
                    AvgTemp.setText((int) temperature + "\u00B0C");
                    AvgHumi.setText((int) humidity + "%");
                } catch (Exception e) {

                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {

// Failed to read value

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }

        });
        emergency_OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bedroomLED.setValue("OFF");
                bathroomLED.setValue("OFF");
                livingroomLED.setValue("OFF");
                kitchenLED.setValue("OFF");
                garageLED.setValue("OFF");
                restroomLED.setValue("OFF");
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}


