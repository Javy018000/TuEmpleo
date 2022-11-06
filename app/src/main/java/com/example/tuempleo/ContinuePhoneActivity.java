package com.example.tuempleo;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ContinuePhoneActivity extends AppCompatActivity {

    EditText mEditContinue;
    Button mButtonContinue;
    TextView mTextResponse;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    String celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_phone);
        mButtonContinue = findViewById(R.id.btnContinuar);
        mEditContinue = findViewById(R.id.edit_continuar);
        mTextResponse = findViewById(R.id.textContinuar);

        mAuth = FirebaseAuth.getInstance();

        mAuth.setLanguageCode("es");



        mButtonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                celular = mEditContinue.getText().toString();
                mTextResponse.setTextColor(Color.BLACK);
                if(celular.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Llene el campo correspondiente",Toast.LENGTH_SHORT).show();

                }
                else{

                    String phoneNumber = "+57" + mEditContinue.getText().toString();
                    if(!phoneNumber.isEmpty()){
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(ContinuePhoneActivity.this)                 // Activity (for callback binding)
                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);


                    }else{
                        mTextResponse.setText("Ingrese el numero de telefono con su respectivo codigo de Pais");
                        mTextResponse.setTextColor(Color.RED);
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
                mTextResponse.setText(e.getMessage());
                mTextResponse.setTextColor(Color.RED);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mTextResponse.setText("El codigo de verificacion fue enviado");
                mTextResponse.setTextColor(Color.BLACK);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ContinuePhoneActivity.this, OtpLoginActivity.class);
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
                    mTextResponse.setText(task.getException().getMessage());
                    mTextResponse.setTextColor(Color.RED);
                }
            }

        });
    }
    private void inicioActivityLogin() {
        Intent intent = new Intent(ContinuePhoneActivity.this, IsLoggingActivity.class);
        startActivity(intent);
        finish();
    }
}