package com.example.alvmaria.larccomputacion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private TextView txtName;
    private DatabaseReference mDatabase;
    private DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.txtName);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mDatabase.child("Users").child(getIntent().getStringExtra("uid")).child("Name");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userString = dataSnapshot.getValue(String.class);
                Log.d("user string", userString);
                txtName.setText(userString + "!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed", databaseError.getMessage());
            }
        };

        user.addListenerForSingleValueEvent(listener);

        //https://stackoverflow.com/questions/43842840/firebase-android-how-to-get-current-user-information-from-database
        //https://www.learnhowtoprogram.com/android/data-persistence/firebase-reading-data-and-event-listeners
    }

    public void onClickMyServices(View view){
        Intent i = new Intent (this, Pedidos.class);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        i.putExtra("uid", uid);
        startActivity(i);
    }

    public void onClickNew(View view){
        Intent i = new Intent (this, Servicios.class);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        i.putExtra("uid", uid);
        startActivity(i);
    }



    public void onClickProfile(View view){
        String uid = getIntent().getStringExtra("uid").toString();
        Intent i = new Intent(this, Perfil.class);
        i.putExtra("uid", uid);
        startActivity(i);
    }
}
