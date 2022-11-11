package com.example.tuempleo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRVMensajes extends RecyclerView.Adapter <AdapterRVMensajes.MensajeHolder> {
    private List<MensajeVO>lstMensaje;

    public AdapterRVMensajes(List<MensajeVO> lstMensaje) {
        this.lstMensaje = lstMensaje;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
        return new MensajeHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder mensajeHolder, int i) {
        mensajeHolder.tvName.setText(lstMensaje.get(i).getName());
        mensajeHolder.tvMenssage.setText(lstMensaje.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
        return lstMensaje.size();
    }

    class MensajeHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private  TextView tvMenssage;

        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.etName);
            tvMenssage=itemView.findViewById(R.id.etMensaje);
        }
    }
}
