package com.example.reconhecimentofacial.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ConsultaPagerAdapter extends FragmentStateAdapter {

    // O construtor agora recebe o Fragmento pai (o Container)
    public ConsultaPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Decide qual fragmento mostrar com base na posição da aba
        switch (position) {
            case 0:
                return new CasosCadastradosFragment(); // Primeira aba
            case 1:
                return new CasosReaisFragment();      // Segunda aba
            default:
                // Como padrão, retorna o primeiro fragmento para evitar erros
                return new CasosCadastradosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // O número total de abas que você tem
    }
}