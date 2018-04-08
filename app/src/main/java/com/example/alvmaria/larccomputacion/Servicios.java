package com.example.alvmaria.larccomputacion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Servicios extends AppCompatActivity{
    EditText edtFecha, edtHora;
    Spinner spnservicios;
    Calendar CurrentDate, CurrentTime;
    private DatabaseReference mDatabase;
    private DatabaseReference user;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.child("LatestServiceID");

        edtFecha = (EditText)findViewById(R.id.edtFecha);
        edtHora = (EditText) findViewById(R.id.edtHora);
        spnservicios = (Spinner)findViewById(R.id.spnServicios);

        Spinner ListServices = (Spinner)findViewById(R.id.spnServicios);

        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Servicios,
                android.R.layout.simple_list_item_1);

        ListServices.setAdapter(adapter);

        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDate = Calendar.getInstance();
                int year = CurrentDate.get(Calendar.YEAR);
                int month = CurrentDate.get(Calendar.MONTH);
                int day = CurrentDate.get(Calendar.DAY_OF_MONTH);

               DatePickerDialog mdatePicker = new DatePickerDialog(Servicios.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {
                       edtFecha.setText(selectedday+"-"+selectedmonth+"-"+selectedyear);
                       CurrentDate.set(selectedyear, selectedmonth, selectedday);
                   }
               }, year, month, day);
               mdatePicker.show();
            }
        });

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentTime = Calendar.getInstance();
                int hour = CurrentTime.get(Calendar.HOUR);
                int minutes  = CurrentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Servicios.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedhour, int selectedminutes) {
                        edtHora.setText(selectedhour+":"+selectedminutes);
                        CurrentTime.set(selectedhour,selectedminutes);
                    }
                }, hour, minutes, true);
                 timePickerDialog.show();
            }

        });
    }

    public void onClickFinalizar(View view){
        final String SelectedHora = edtHora.getText().toString();
        final String SelectedFecha = edtFecha.getText().toString();
        final String SelectedServicio = spnservicios.getSelectedItem().toString();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String userString = dataSnapshot.getValue(String.class);
                //Log.d("user string", userString);

                Map<String, Object> userData = new HashMap<>();

                userData.put("Time", SelectedHora);
                userData.put("Date", SelectedFecha);
                userData.put("Service", SelectedServicio);

                mDatabase.child("Services").child(getIntent().getStringExtra("uid")).child(Long.toString(dataSnapshot.getValue(Long.class))).updateChildren(userData);

                mDatabase.child("LatestServiceID").setValue(dataSnapshot.getValue(Long.class)+1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed", databaseError.getMessage());
            }
        };

        myRef.addListenerForSingleValueEvent(listener);

        Intent i = new Intent(this, Pedidos.class);
        i.putExtra("uid", getIntent().getStringExtra("uid"));
        startActivity(i);
    }

}
