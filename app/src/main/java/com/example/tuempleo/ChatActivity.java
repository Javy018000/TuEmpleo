package com.example.tuempleo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rvMensajes;
    private EditText etName;
    private EditText etMensaje;
    private ImageButton btnSend;

    private List<MensajeVO> lstMensajes;
    private AdapterRVMensajes mAdapterRVMensajes;
    private String invited = "";
    FirebaseAuth mAuth;
    DocumentReference reference;

    private void setComponents(){
        rvMensajes = findViewById(R.id.rvMensajes);
        etName = findViewById(R.id.etName);
        etMensaje = findViewById(R.id.etMensaje);
        btnSend = findViewById(R.id.btnSend);

        lstMensajes = new ArrayList<>();
        mAdapterRVMensajes = new AdapterRVMensajes(lstMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMensajes.setAdapter(mAdapterRVMensajes);
        rvMensajes.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore.getInstance().collection("Chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange mDocumentChange : queryDocumentSnapshots.getDocumentChanges()){
                            if(mDocumentChange.getType() == DocumentChange.Type.ADDED){
                                lstMensajes.add(mDocumentChange.getDocument().toObject(MensajeVO.class));
                                mAdapterRVMensajes.notifyDataSetChanged();
                                rvMensajes.smoothScrollToPosition(lstMensajes.size());
                            }
                        }

                    }
                });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.length() == 0 || etMensaje.length() == 0)
                    return;

                    MensajeVO mMensajeVO = new MensajeVO();
                    mMensajeVO.setMessage(etMensaje.getText().toString());
                    mMensajeVO.setName(invited + etName.getText().toString());
                    FirebaseFirestore.getInstance().collection("Chat").add(mMensajeVO);
                    etMensaje.setText("");
                    //etName.setText("");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseFirestore.getInstance().collection("Usuario")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshot = task.getResult();
                        for (DocumentSnapshot documentSnapshot :
                                queryDocumentSnapshot.getDocuments()) {
                            if(documentSnapshot.exists() && mAuth.getCurrentUser() != null){
                                String phone = "+57" + documentSnapshot.getString("Teléfono");
                                if(phone.equals(mAuth.getCurrentUser().getPhoneNumber())){
                                    etName.setText(documentSnapshot.getString("Nombres"));
                                    etName.setEnabled(false);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        setComponents();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            invited = "(Invitado) ";
        }
    }
}