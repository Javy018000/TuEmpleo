package com.example.tuempleo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {


    PinView mEditTextCode;
    TextView mTextViewResponse;
    TextView mTextPrueba;
    Button mButtonVerificar;
    ProgressBar progressBar;

    private FirebaseFirestore mFirestore;

    FirebaseAuth mAuth;
    String intenAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mFirestore = FirebaseFirestore.getInstance();

        mEditTextCode = findViewById(R.id.verification_code_entered_by_user);
        mTextViewResponse = findViewById(R.id.textViewResponse);
        mButtonVerificar = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progress_bar);
        mTextPrueba = findViewById(R.id.textPrueba);
        mAuth = FirebaseAuth.getInstance();

        intenAuth = getIntent().getStringExtra("auth");
        progressBar.setVisibility(mEditTextCode.VISIBLE);

        mButtonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codigoVerificacio = mEditTextCode.getText().toString();

                if(!codigoVerificacio.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential
                            (intenAuth, codigoVerificacio);
                    iniciarSesion(credential);
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese el codigo de verificacion", Toast.LENGTH_SHORT).show();
                }
            }

            private void iniciarSesion(PhoneAuthCredential credential) {
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            subirDatosBasicos();
                            inicioActivityHome();

                        }else{
                            Toast.makeText(getApplicationContext(), "Error de verificación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            inicioActivityHome();
        }
    }

    private void inicioActivityHome() {
        Intent intent = new Intent(OtpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    private void subirDatosBasicos() {
            String id = mAuth.getCurrentUser().getUid();
            Map<String, Object> datosBasicos = new HashMap<>();

            datosBasicos.put("Id", id);
            datosBasicos.put("Nombres", SignUpActivity.nombres);
            datosBasicos.put("Apellidos", SignUpActivity.apellidos);
            datosBasicos.put("Correo electronico", SignUpActivity.correo);
            datosBasicos.put("Contraseña", SignUpActivity.contraseña);
            datosBasicos.put("Teléfono", SignUpActivity.celular);

            mFirestore.collection("Usuario").document(SignUpActivity.correo).set(datosBasicos).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Registrado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }


}