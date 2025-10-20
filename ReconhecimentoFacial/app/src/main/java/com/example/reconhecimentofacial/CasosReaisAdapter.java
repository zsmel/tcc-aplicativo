package com.example.reconhecimentofacial;

import android.content.Context;
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

public class CasosReaisAdapter extends RecyclerView.Adapter<CasosReaisAdapter.ViewHolder> {

    private Context context;
    private List<CasoReal> listaCasos;
    private OnFonteClickListener listener;

    // Interface para o clique no botão "Ver Fonte"
    public interface OnFonteClickListener {
        void onFonteClick(int position);
    }

    public void setOnFonteClickListener(OnFonteClickListener listener) {
        this.listener = listener;
    }

    public CasosReaisAdapter(Context context, List<CasoReal> listaCasos) {
        this.context = context;
        this.listaCasos = listaCasos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_caso_real, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CasoReal caso = listaCasos.get(position);

        holder.tvNome.setText(caso.getNome());
        holder.tvLocalData.setText(caso.getCidadeEstado() + " - " + caso.getDataDesaparecimento());

        Glide.with(context)
                .load(caso.getFotoUrl())
                .placeholder(R.drawable.ic_placeholder_person)
                .error(R.drawable.ic_placeholder_person) // Imagem para caso de erro no link
                .into(holder.ivFoto);

        holder.btnVerFonte.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFonteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCasos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNome, tvLocalData;
        Button btnVerFonte;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFotoReal);
            tvNome = itemView.findViewById(R.id.tvNomeReal);
            tvLocalData = itemView.findViewById(R.id.tvLocalDataReal);
            btnVerFonte = itemView.findViewById(R.id.btnVerFonte);
        }
    }
}