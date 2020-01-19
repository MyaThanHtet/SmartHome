package com.ucswa.smarthome;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ucswa.smarthome.Connection.ConnectionState;

public class Login_Activity extends AppCompatActivity {
    private ProgressBar pg_Bar;
    private TextInputEditText UserName_Edt;
    private TextInputEditText Password_Edt;
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private Button login_btn;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        UserName_Edt = findViewById(R.id.user_name_edt);
        Password_Edt = findViewById(R.id.password_edt);
        pg_Bar = findViewById(R.id.pgbar);
        login_btn = findViewById(R.id.login_btn);
        textInputLayout1 = findViewById(R.id.user);
        textInputLayout2 = findViewById(R.id.pswd);
        pg_Bar.setVisibility(View.GONE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Password_Edt.getText().toString().equals("ucsmwa")) {
                    final DatabaseReference username = myRef.child("database").child("user_name");
                    username.setValue(UserName_Edt.getText().toString());
                    checkConnection();
                    pg_Bar.setVisibility(View.VISIBLE);
                    login_btn.setVisibility(View.GONE);
                    textInputLayout1.setVisibility(View.GONE);
                    textInputLayout2.setVisibility(View.GONE);
                } else {
                    textInputLayout2.setBoxStrokeColor(Color.RED);
                }


            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        myRef.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String USERNAME = (String) dataSnapshot.child("database").child("user_name").getValue();
                    UserName_Edt.setText(USERNAME);
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }

            @Override

            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkConnection() {
        if (ConnectionState.getInstance(Login_Activity.this).isOnline()) {


            Thread td = new Thread(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        Intent i = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(i);

                        finish();
                    }
                }
            });
            td.start();


        } else {
            Toast.makeText(Login_Activity.this, "No internet connection!!", Toast.LENGTH_SHORT).show();
        }
    }
}


