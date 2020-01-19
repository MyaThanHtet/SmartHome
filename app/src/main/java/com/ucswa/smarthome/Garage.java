package com.ucswa.smarthome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Garage extends AppCompatActivity {
    private ToggleButton toggle;
    private ImageView lampImg;
    private Button Emergency_Call_btn;
    private Vibrator mVibrator;
    private Switch fireAlarmSwitch;
    public static TextView fire_detectTv;
    private ConstraintLayout cst;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_layout);
        fire_detectTv = findViewById(R.id.garage_fire_detectTv);
        Emergency_Call_btn = findViewById(R.id.garage_fire_emergency);
        lampImg = findViewById(R.id.imgLamp_garage);
        toggle = findViewById(R.id.Onbtn_garage);

        cst = findViewById(R.id.garage_fire_layout);
        fireAlarmSwitch = findViewById(R.id.garagr_fire_alarm_switch);
        Boolean flag = fireAlarmSwitch.isChecked();
        Toast.makeText(this, flag + "Hello", Toast.LENGTH_SHORT).show();
        Emergency_Call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference Fire_alarm_switch_state = myRef.child("fire_alarm_switch_state");
        final DatabaseReference led = myRef.child("led_status_garage");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    long fireDetector = (long) dataSnapshot.child("fire_detector_garage").getValue();
                    String str = (String) dataSnapshot.child("led_status_garage").getValue();
                    String fireState = (String) dataSnapshot.child("fire_alarm_switch_state").getValue();
                    if (str.equals("ON")) {
                        toggle.setChecked(true);
                        lampImg.setImageResource(R.drawable.ic_lamp_on);
                    } else {
                        toggle.setChecked(false);
                        lampImg.setImageResource(R.drawable.ic_lamp_off);
                    }
                    if (fireDetector == 1) {
                        fire_detectTv.setText("Fire....Fire!");
                        cst.setBackgroundResource(R.drawable.fire_alert_bg);
                        vibrateMulti(THREE_CYCLES);
                    } else {
                        fire_detectTv.setText("Auto Fire Detecting...");
                        cst.setBackgroundResource(R.drawable.gradient_background);
                        if (mVibrator != null) {
                            mVibrator.cancel();
                        }
                    }
                    if (fireState.equals("true")) {
                        fireAlarmSwitch.setChecked(true);
                    } else {
                        fireAlarmSwitch.setChecked(false);
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
        fireAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Fire_alarm_switch_state.setValue("true");
                } else {
                    Fire_alarm_switch_state.setValue("false");
                }
            }
        });
    }

    private void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Garage.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 191));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 191));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final long[] THREE_CYCLES = new long[]{500, 500};


    private void vibrateMulti(long[] THREE_CYCLES) {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 and above
            mVibrator.vibrate(VibrationEffect.createWaveform(THREE_CYCLES, 0));
        } else {
            // Below API 26
            mVibrator.vibrate(THREE_CYCLES, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVibrator != null) {
            mVibrator.cancel();
        }
    }

}