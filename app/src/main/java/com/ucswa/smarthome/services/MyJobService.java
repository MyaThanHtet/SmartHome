package com.ucswa.smarthome.services;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyJobService extends JobService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Vibrator mVibrator;

    public MyJobService() {
    }

    @Override
    public boolean onStartJob(final JobParameters job) {
        FirebaseLoad();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(getApplicationContext(), "Stop Background", Toast.LENGTH_SHORT).show();
        if (mVibrator != null) {
            mVibrator.cancel();
        }

        return false;
    }


    public void FirebaseLoad() {

        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    long fireDetector = (long) dataSnapshot.child("fire_detector").getValue();
                    long GaragefireDetector = (long) dataSnapshot.child("fire_detector_garage").getValue();
                    //  Toast.makeText(getApplicationContext(), fireDetector + "Fire Fire", Toast.LENGTH_LONG).show();
                    if (fireDetector == 0) {
                        vibrateMulti(THREE_CYCLES);
                    } else {
                        if (mVibrator != null) {
                            mVibrator.cancel();
                        }
                    }
                    if (GaragefireDetector == 0) {
                        vibrateMulti(THREE_CYCLES);
                    } else {
                        if (mVibrator != null) {
                            mVibrator.cancel();
                        }
                    }
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
    }

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

    private static final long[] THREE_CYCLES = new long[]{500, 500};
}