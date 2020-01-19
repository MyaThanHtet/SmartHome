package com.ucswa.smarthome;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shinelw.library.ColorArcProgressBar;

public class BathRoom extends AppCompatActivity {
    ToggleButton toggle;
    ImageView lampImg;
    private ColorArcProgressBar bar1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bathroom_layout);
       // bar1 = findViewById(R.id.bar1);
        lampImg = findViewById(R.id.imgLamp_bathroom);
//        final ArcProgress arcProgressTemp = findViewById(R.id.temp_arc_progress);
//        final ArcProgress arcProgressHum = findViewById(R.id.hum_arc_progress);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference led = myRef.child("led_status_bathroom");
        toggle = findViewById(R.id.Onbtn_bathroom);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
//                    float temperature = dataSnapshot.child("temp").getValue(float.class);
//                    float humidity = dataSnapshot.child("hum").getValue(float.class);
//                    arcProgressTemp.setProgress((int) temperature);
//                    arcProgressHum.setProgress((int) humidity);
                    String str = (String) dataSnapshot.child("led_status_bathroom").getValue();
                    if (str.equals("ON")) {
                        toggle.setChecked(true);
                        lampImg.setImageResource(R.drawable.ic_lamp_on);
                    } else {
                        toggle.setChecked(false);
                        lampImg.setImageResource(R.drawable.ic_lamp_off);
                    }

//                    bar1.setCurrentValues(temperature);
//                    bar1.setUnit("Hum " + humidity + "%");
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {

// Failed to read value

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }

        });


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    led.setValue("ON");

                    lampImg.setImageResource(R.drawable.ic_lamp_on);
                    // The toggle is enabled
                } else {
                    led.setValue("OFF");
                    lampImg.setImageResource(R.drawable.ic_lamp_off);
                    // The toggle is disabled
                }
            }
        });

    }
}
