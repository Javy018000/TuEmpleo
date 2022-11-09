package com.example.tuempleo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LogInActivity extends AppCompatActivity {
    private EditText email;
    private EditText passw;
    private TextView mTextViewResponse;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Button mButtonSignUp;
    static DocumentSnapshot documentSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email=findViewById(R.id.li_userName);
        passw=findViewById(R.id.li_passw);
        mAuth = FirebaseAuth.getInstance();
        mButtonSignUp = findViewById(R.id.signUpBtn);
        mTextViewResponse = findViewById(R.id.text_view_response);
        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mAuth.setLanguageCode("es");

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();
            }

        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //Iniciamos secion
                logIn(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //Falló el inicio de sesion
                mTextViewResponse.setText(e.getMessage());
                mTextViewResponse.setTextColor(Color.RED);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mTextViewResponse.setText("El codigo de verificacion fue enviado");
                mTextViewResponse.setTextColor(Color.BLACK);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogInActivity.this, OtpLoginActivity.class);
                        intent.putExtra("auth", s);
                        startActivity(intent);
                    }
                },1000);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            inicioActivityLogin();
        }
        else{

        }
    }
    private void logIn(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    inicioActivityLogin();
                }else{
                    mTextViewResponse.setText(task.getException().getMessage());
                    mTextViewResponse.setTextColor(Color.RED);
                }
            }

        });
    }
    private void inicioActivityLogin() {
        Intent intent = new Intent(LogInActivity.this, IsLoggingActivity.class);
        startActivity(intent);
        finish();
    }

    public void obtenerDatos(){


        mFirestore.collection("Usuario").document(email.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        if(documentSnapshot.getString("Correo electronico").isEmpty() && documentSnapshot.getString("Contraseña").isEmpty()){
                            Toast.makeText(getApplicationContext(), "Llene los campos correspondientes", Toast.LENGTH_SHORT).show();
                        }else{
                            if(documentSnapshot.getString("Contraseña").equals(passw.getText().toString())){
                                Intent intent = new Intent(LogInActivity.this, ContinuePhoneActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                //mTextViewResponse.setText("Bases de datos:" + documentSnapshot.getString("Contraseña") + "\nEditText:" + contraseña);
                            }


                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Correo electronico no registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
