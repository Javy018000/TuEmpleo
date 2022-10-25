package com.example.tuempleo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private EditText email;
    private EditText passw;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email=findViewById(R.id.li_userName);
        passw=findViewById(R.id.li_passw);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }



    public void logIn(View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),passw.getText().toString().trim())
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Log.d(TAG,"Correcto");
                            Toast.makeText(getApplicationContext(),"Acceso Concedido",Toast.LENGTH_SHORT).show();
                            FirebaseUser user=mAuth.getCurrentUser();

                            Intent intent= new Intent(getApplicationContext(),TrActivity.class);
                            startActivity(intent);
                            //updateUI(user);
                        }else{
                            //Log.w(TAG,"",task.getExeption());
                            Toast.makeText(getApplicationContext(),"Autotentificacion no se pudo",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


        }

    }