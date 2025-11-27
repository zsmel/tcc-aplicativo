package com.example.reconhecimentofacial;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AlertasAdapter extends RecyclerView.Adapter<AlertasAdapter.ViewHolder> {

    private final Context context;
    private final List<Alerta> listaAlertas;

    public AlertasAdapter(Context context, List<Alerta> listaAlertas) {
        this.context = context;
        this.listaAlertas = listaAlertas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alerta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alerta alerta = listaAlertas.get(position);

        String porc = alerta.getPorcentagem() != null ? alerta.getPorcentagem() : "?";
        if (!porc.contains("%")) porc += "%";

        String header = porc + " | " + (alerta.getLocalizacao() != null ? alerta.getLocalizacao() : "Local Desconhecido");

        holder.tvHeader.setText(header);
        holder.tvData.setText("Emitido em: " + alerta.getDataEmissao());
        holder.tvVitima.setText("Vítima: " + alerta.getVitimaNome() + ", " + alerta.getVitimaIdade() + " anos");
        holder.tvStatus.setText("Status: " + alerta.getStatus());

        String urlFoto = alerta.getFotoDesaparecido();
        if (urlFoto != null && !urlFoto.isEmpty()) {
            Glide.with(context)
                    .load(urlFoto)
                    .placeholder(R.drawable.ic_placeholder_person)
                    .circleCrop()
                    .into(holder.ivFoto);
        } else {
            holder.ivFoto.setImageResource(R.drawable.ic_placeholder_person);
        }

        holder.btnDetalhes.setOnClickListener(v -> {
            String idOriginal = alerta.getDesaparecidoID();
            if (idOriginal != null && !idOriginal.isEmpty()) {
                Intent intent = new Intent(context, CaseDetailActivity.class);
                intent.putExtra("CASO_ID", idOriginal);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAlertas != null ? listaAlertas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader, tvData, tvVitima, tvStatus;
        ImageView ivFoto;
        Button btnDetalhes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvAlertaHeader);
            tvData = itemView.findViewById(R.id.tvAlertaData);
            tvVitima = itemView.findViewById(R.id.tvAlertaVitima);
            tvStatus = itemView.findViewById(R.id.tvAlertaStatus);
            ivFoto = itemView.findViewById(R.id.ivAlertaFoto);
            btnDetalhes = itemView.findViewById(R.id.btnAlertaDetalhes);
        }
    }
}
