package com.example.tuempleo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rvMensajes;
    private EditText etNombre;
    private EditText etMensaje;
    private ImageButton btnSend;

    private List<MensajeVO> lstMensajes;
    private AdapterRVMensajes mAdapterRVMensajes;
    private void setComponents(){
        rvMensajes =findViewById(R.id.rvMensajes);
        etNombre=findViewById(R.id.etName);
        etMensaje =findViewById(R.id.etMensaje);
        btnSend = findViewById(R.id.btnSend);

        lstMensajes = new ArrayList<>();
        mAdapterRVMensajes= new AdapterRVMensajes(lstMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMensajes.setAdapter(mAdapterRVMensajes);
        rvMensajes.setHasFixedSize(true);

        FirebaseFirestore.getInstance().collection("chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshot, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange mDocumentChange: queryDocumentSnapshot.getDocumentChanges()) {
                            if(mDocumentChange.getType()==DocumentChange.Type.ADDED) {
                                lstMensajes.add(mDocumentChange.getDocument().toObject(MensajeVO.class));
                                mAdapterRVMensajes.notifyDataSetChanged();
                                rvMensajes.smoothScrollToPosition(lstMensajes.size());
                            }
                        }
                    }
                });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNombre.length()==0 || etMensaje.length()==0)return;
                MensajeVO mMnesajeVO= new MensajeVO();
                mMnesajeVO.setMessage(etMensaje.getText().toString());
                mMnesajeVO.setName(etNombre.getText().toString());
                FirebaseFirestore.getInstance().collection("chat").add(mMnesajeVO);
                etMensaje.setText("");
                etNombre.setText("");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setComponents();
    }
}