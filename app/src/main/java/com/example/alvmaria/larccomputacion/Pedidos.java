package com.example.alvmaria.larccomputacion;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvmaria.larccomputacion.POJO.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pedidos extends AppCompatActivity {
    String uid;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    List <Service> dataServices;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        dataServices = new ArrayList<Service>();
        listView = (ListView)findViewById(R.id.listView);
        uid = getIntent().getStringExtra("uid").toString();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.child("Services").child(uid);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Service service = dataSnapshot.getValue(Service.class);
                //Log.d("user string", service.date);
                ArrayAdapter <Service> adapter = new ListAdapter(Pedidos.this, R.layout.layout);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    dataServices.add(new Service((String) snapshot.child("Date").getValue(),
                                                 (String) snapshot.child("Service").getValue(),
                                                 (String) snapshot.child("Time").getValue()));
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed", databaseError.getMessage());
            }
        };

        myRef.addListenerForSingleValueEvent(listener);
    }

    private class ListAdapter extends ArrayAdapter<Service>{
        public ListAdapter(@NonNull Context context, int resource) {
            super(Pedidos.this, R.layout.layout, dataServices);
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent){
            View itemView = converView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.layout, parent, false);
            }
            Service currentService = dataServices.get(position);
            //Time
            TextView txtHora = (TextView)itemView.findViewById(R.id.txtHora);
            txtHora.setText(currentService.hour.toString());
            //Date
            TextView txtFecha = (TextView)itemView.findViewById(R.id.txtFecha);
            txtFecha.setText(currentService.date.toString());
            //Type
            TextView txtType = (TextView)itemView.findViewById(R.id.txtType);
            txtType.setText(currentService.type.toString());


            //return super.getView(position, converView, parent);
            return  itemView;
        }
    }


}
