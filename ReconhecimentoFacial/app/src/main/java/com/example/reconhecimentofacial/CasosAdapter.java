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

public class CasosAdapter extends RecyclerView.Adapter<CasosAdapter.ViewHolder> {

    private Context context;
    private List<Caso> listaCasos; // Esta lista será atualizada
    private OnDetalhesClickListener listener;

    public interface OnDetalhesClickListener {
        void onDetalhesClick(int position);
    }

    public void setOnDetalhesClickListener(OnDetalhesClickListener listener) {
        this.listener = listener;
    }

    public CasosAdapter(Context context, List<Caso> listaCasos) {
        this.context = context;
        this.listaCasos = listaCasos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_caso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Caso caso = listaCasos.get(position);

        holder.textViewNome.setText(caso.getNomeCompleto());

        // Verifica se o status não é nulo antes de exibir
        if (caso.getEstado_desaparecimento() != null) {
            holder.textViewStatus.setText(caso.getEstado_desaparecimento());
        } else {
            holder.textViewStatus.setText("Status não informado");
        }

        // --- AQUI ESTÁ A MUDANÇA PARA FOTO REDONDA ---
        if (caso.getFotos() != null && !caso.getFotos().isEmpty()) {
            Glide.with(context)
                    .load(caso.getFotos().get(0))
                    .placeholder(R.drawable.ic_placeholder_person)
                    .error(R.drawable.ic_placeholder_person)
                    .circleCrop() // <--- ESTA LINHA FAZ O CORTE REDONDO
                    .into(holder.imageViewFoto);
        } else {
            // Carrega o placeholder redondo também para manter o padrão
            Glide.with(context)
                    .load(R.drawable.ic_placeholder_person)
                    .circleCrop()
                    .into(holder.imageViewFoto);
        }
        // ---------------------------------------------

        holder.buttonDetalhes.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetalhesClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCasos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFoto;
        TextView textViewNome;
        TextView textViewStatus;
        TextView textViewCasoId;
        Button buttonDetalhes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewCasoId = itemView.findViewById(R.id.textViewCasoId);
            buttonDetalhes = itemView.findViewById(R.id.buttonDetalhes);
        }
    }

    public void updateList(List<Caso> novaLista) {
        listaCasos.clear();
        listaCasos.addAll(novaLista);
        notifyDataSetChanged();
    }
}