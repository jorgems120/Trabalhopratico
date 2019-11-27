package com.example.trabalhopratico.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.trabalhopratico.R;
import com.example.trabalhopratico.db.Contrato;

public class MyCursorAdapter extends CursorAdapter {

    private Context mContext;
    private int id;
    private Cursor mCursor;

    public MyCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
        mContext = context;
        mCursor = c;
    }

    public void changeCursor(Cursor cursor){
        super.changeCursor(cursor);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_linha, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView text1 = (TextView) view.findViewById(R.id.nome);
        TextView text2 = (TextView) view.findViewById(R.id.numero);
        TextView text3 = (TextView) view.findViewById(R.id.idade);
        TextView text4 = (TextView) view.findViewById(R.id.gen);

        text1.setText((mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NOME))));
        text2.setText(String.valueOf(mCursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NUMERO))));
        text3.setText(String.valueOf(mCursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_IDADE))));
        text4.setText((mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_GENERO))));

    }
}
