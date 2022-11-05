package com.example.tuempleo;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {


    PinView mEditTextCode;
    TextView mTextViewResponse;
    Button mButtonVerificar;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    String intenAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mEditTextCode = findViewById(R.id.verification_code_entered_by_user);
        mTextViewResponse = findViewById(R.id.textViewResponse);
        mButtonVerificar = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progress_bar);
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
                    Toast.makeText(OtpActivity.this, "Ingrese el codigo de verificacion", Toast.LENGTH_SHORT).show();
                }
            }

            private void iniciarSesion(PhoneAuthCredential credential) {
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SignUpActivity objSignUp = new SignUpActivity();
                            objSignUp.RealizarAutenticacionCorreo();
                            inicioActivityHome();
                        }else{
                            Toast.makeText(OtpActivity.this, "Error de verifición", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(OtpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}