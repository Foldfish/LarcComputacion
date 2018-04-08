package com.example.alvmaria.larccomputacion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CrearCuenta extends AppCompatActivity {
    EditText Passwd, RPasswd, Email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        Passwd = (EditText) findViewById(R.id.edtPasswdNew);
        RPasswd = (EditText) findViewById(R.id.edtPasswdRNew);
        Email = (EditText) findViewById(R.id.edtEmailNew);
    }

    public void onClickCreate(View view) {
        //Verificar datos de passwd else toast and break
        final String typedpasswd = Passwd.getText().toString();
        final String typedrpasswd = RPasswd.getText().toString();
        final String typedemail = Email.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if ((typedpasswd.equals(typedrpasswd))&&(!typedpasswd.isEmpty())&&(!typedrpasswd.isEmpty())) {
            Log.d("CREAR","A punto de Crear");
            mAuth.createUserWithEmailAndPassword(typedemail, typedpasswd)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("CREATED","Tu cuenta :v");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d("UID BEFORE AddUser",user.getUid());
                                addUsers(user);
                                Log.d("ADD USERS","Ya ha sido llamado");
                                //timer

                            } else {
                                Log.d("TASK.FAILURE","User creation failure" );
                                try{
                                    throw task.getException();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    FirebaseAuthException ex = (FirebaseAuthException)task.getException();
                                    Log.d("Exception",""+ex.getMessage());
                                    Toast.makeText(getApplicationContext(), "User creation failure"+ex.getMessage(), Toast.LENGTH_SHORT);

                                    return;
                                }
                                //Toast.makeText(CrearCuenta.this, "User creation failure", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Passwords don't match, try again", Toast.LENGTH_SHORT);
        }
    }

    public void addUsers(FirebaseUser user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = mDatabase.child("Users");
        //final FirebaseUser AddUseruser = user.getUid();
        final String UID = user.getUid();
        Log.d("UID", UID);
        final EditText edtName = (EditText) findViewById(R.id.edtNameNew);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String userString = dataSnapshot.getValue(String.class);
                //Log.d("user string", userString);
                Map<String, Object> userData = new HashMap<>();

                userData.put("Email", Email.getText().toString());
                userData.put("Name", edtName.getText().toString());

                mDatabase.child("Users").child(UID).updateChildren(userData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed", databaseError.getMessage());
            }
        };

        myRef.addListenerForSingleValueEvent(listener);

        Toast.makeText(this, "Tu cuenta fue creada exitosamente", Toast.LENGTH_LONG);

        Intent i = new Intent(getApplicationContext(), LogIn.class);
        i.putExtra("task", "Successful");  //Send to login so that a toast informs about creation
        startActivity(i);
    }
}
