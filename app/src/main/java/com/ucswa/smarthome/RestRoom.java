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

public class RestRoom extends AppCompatActivity {
    ToggleButton toggle;
    ImageView lampImg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restroom_layout);

        lampImg = findViewById(R.id.imgLamp_restroom);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference led = myRef.child("led_status_restroom");
        toggle = findViewById(R.id.Onbtn_restroom);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String str = (String) dataSnapshot.child("led_status_restroom").getValue();
                    if (str.equals("ON")) {
                        toggle.setChecked(true);
                        lampImg.setImageResource(R.drawable.ic_lamp_on);
                    } else {
                        toggle.setChecked(false);
                        lampImg.setImageResource(R.drawable.ic_lamp_off);
                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {
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
