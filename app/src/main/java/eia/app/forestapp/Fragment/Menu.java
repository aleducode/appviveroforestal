package eia.app.forestapp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import eia.app.forestapp.RegistroEventos;
import eia.app.forestapp.WebViewEventos;
import eia.app.forestapp.R;
import eia.app.forestapp.WebViewGastoHidrico;


public class Menu extends Fragment implements View.OnClickListener{

    LinearLayout lyconsulta;
    LinearLayout lyregistro;
    LinearLayout lygasto;


    private void gotoUrl(String url){Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        lyconsulta=(LinearLayout)view.findViewById(R.id.lyconsulta);
        lyregistro=(LinearLayout)view.findViewById(R.id.lyregistro);
        lygasto=(LinearLayout)view.findViewById(R.id.lygasto);
        lygasto.setOnClickListener(this);
        lyconsulta.setOnClickListener(this);
        lyregistro.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lyconsulta:
               // gotoUrl("http://10.2.11.229/Eventosapp.aspx");
                Intent intent1 = new Intent(getContext(),WebViewEventos.class); //abre Actividad de Detalles
                startActivity(intent1);
                break;
            case R.id.lyregistro:
                Intent intent = new Intent(getContext(), RegistroEventos.class); //abre Actividad de Detalles
                startActivity(intent);
                break;
            case R.id.lygasto:
                Intent intent2 = new Intent(getContext(), WebViewGastoHidrico.class); //abre Actividad de Detalles
                startActivity(intent2);
                break;

            default:
                break;


        }
    }
}

