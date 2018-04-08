package com.example.alvmaria.larccomputacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {
    EditText edtTelefono, edtDireccion;
    String uid;
    private DatabaseReference mDatabase, myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        uid = getIntent().getStringExtra("uid");
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.child("Users").child(uid);
    }

    public void onClickSave(View view)
    {
        Log.d("CURRENT UID", uid);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String userString = dataSnapshot.getValue(String.class);
                //Log.d("user string", userString);

                Map<String, Object> userData = new HashMap<>();

                //userData.put("UID", uid);
                userData.put("Phone", edtTelefono.getText().toString());
                userData.put("Address", edtDireccion.getText().toString());

                Log.d("BEFORE PUSHED: ", uid);

               // mDatabase.child("Users").child(uid).updateChildren(userData);
                myRef.updateChildren(userData);
                Toast.makeText(getApplicationContext(), "Data saved succesfully", Toast.LENGTH_SHORT);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Data update failed", databaseError.getMessage());
            }
        };

        Log.d("AFTER PUSHED: ", myRef.toString());
        myRef.addListenerForSingleValueEvent(listener);
    }

}
