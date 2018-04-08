package com.example.alvmaria.larccomputacion;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    EditText edtUsername, edtPasswd;
    private FirebaseAuth mAuth;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        edtUsername = (EditText) findViewById(R.id.editText);
        edtPasswd = (EditText) findViewById(R.id.editText2);

    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void onClickLogIn(View view) {
        email = edtUsername.getText().toString();
        password = edtPasswd.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        /*String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = "";
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("email").getValue().equals(edtUsername.getText().toString())) {
                        uid = userSnapshot.getKey();
                        break;
                    }
                }
                if(uid == null) {
                    Toast.makeText(getApplicationContext(), "Login unsuccessful, try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ON_CLICK_LOGIN", "Read failed");
            }
        });*/
        final Intent i = new Intent(getApplicationContext(), Main.class);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        i.putExtra("uid", user);
        startActivity(i);
        finish();
    }

    public void onClickCreating(View view) {
        Intent i = new Intent(this, CrearCuenta.class);
        startActivity(i);
        finish();
    }
}
