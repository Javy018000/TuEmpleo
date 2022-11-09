package com.example.tuempleo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    private EditText email;
    private EditText passw;
    private EditText comPassw;
    private EditText mEditTextNumberPhone;
    private EditText name;
    private EditText lastName;

    static String nombres, apellidos;
    static String correo;
    static String contraseña;
    static String confContraseña;
    static String celular;



    Button mButtonSend;
    TextView mTextViewResponse;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email= findViewById(R.id.userName);
        passw= findViewById(R.id.passw);
        comPassw= findViewById(R.id.confirPassw);
        mEditTextNumberPhone = findViewById(R.id.Phone);
        mButtonSend = findViewById(R.id.signUpBtn);
        mTextViewResponse = findViewById(R.id.textViewResponse);
        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastName);
        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        mAuth.setLanguageCode("es");



        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = name.getText().toString(); apellidos = lastName.getText().toString();
                correo = email.getText().toString().trim();contraseña = passw.getText().toString().trim();
                celular = mEditTextNumberPhone.getText().toString();
                confContraseña = comPassw.getText().toString();
                mTextViewResponse.setTextColor(Color.BLACK);
                if(nombres.isEmpty() && apellidos.isEmpty() && correo.isEmpty() && contraseña.isEmpty() && celular.isEmpty() && confContraseña.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Llene todos los campos",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(confContraseña.equals(contraseña)){
                        mFirestore.collection("Usuario").document(correo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    Toast.makeText(getApplicationContext(), "Este correo está en uso", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                        String phoneNumber = "+57" + mEditTextNumberPhone.getText().toString();
                                        if(!phoneNumber.isEmpty()){
                                            PhoneAuthOptions options =
                                                    PhoneAuthOptions.newBuilder(mAuth)
                                                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                            .setActivity(SignUpActivity.this)                 // Activity (for callback binding)
                                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                            .build();
                                            PhoneAuthProvider.verifyPhoneNumber(options);


                                        }else{
                                            mTextViewResponse.setText("Ingrese el numero de telefono con su respectivo codigo de Pais");
                                            mTextViewResponse.setTextColor(Color.RED);
                                        }

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });;


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
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
                        Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);
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
        if (user != null){
            inicioActivityLogin();
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
        Intent intent = new Intent(SignUpActivity.this, IsLoggingActivity.class);
        startActivity(intent);
        finish();
    }
    /*public boolean comprobarSiCorreoExiste(String corr){
        final boolean[] prueba = new boolean[1];

        mTextViewResponse.setText(mTextViewResponse.getText()+ "" + prueba[0]);
        return prueba[0];

    }*/




}