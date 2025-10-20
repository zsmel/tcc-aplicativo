package com.example.reconhecimentofacial.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.reconhecimentofacial.R;
// Importe o seu novo adapter aqui. A linha abaixo é a correção!
import com.example.reconhecimentofacial.ui.ConsultaPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ConsultaContainerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Usa o layout que contém as abas
        return inflater.inflate(R.layout.fragment_consulta_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutConsulta);
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerConsulta);

        // Esta linha agora funcionará por causa do 'import'
        ConsultaPagerAdapter adapter = new ConsultaPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Conecta as abas ao ViewPager para exibir os títulos
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("CASOS CADASTRADOS");
                    } else {
                        tab.setText("CASOS REAIS");
                    }
                }
        ).attach();
    }
}