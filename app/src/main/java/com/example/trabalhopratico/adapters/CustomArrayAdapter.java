package com.example.trabalhopratico.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trabalhopratico.R;
import com.example.trabalhopratico.entities.Contacto;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Contacto> {
    public CustomArrayAdapter(@NonNull Context context, ArrayList<Contacto> resource) {
        super(context, 0,resource);
    }


    public View getView(int position,View convertView, ViewGroup parent) {
        Contacto p = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.nome)).setText(p.getnome());
        ((TextView) convertView.findViewById(R.id.numero)).setText(p.getnumero());
        ((TextView) convertView.findViewById(R.id.idade)).setText(p.getidade());


        return convertView;

    }
}
