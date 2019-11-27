package com.example.trabalhopratico;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.entities.Contacto;

import java.io.Serializable;

public class ver extends AppCompatActivity implements Serializable {

    SQLiteDatabase db;
    DB mDbHelper;
    Cursor cursor;
    int id;
    TextView ct1,ct2,ct3,ct4,ct5,ct6,ct7,ct8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver);
        ct1   =  findViewById(R.id.nome);
        ct2   = findViewById(R.id.numero);
        ct3   = findViewById(R.id.idade);
        ct4   = findViewById(R.id.email);
        ct5   = findViewById(R.id.profissao);
        ct6   = findViewById(R.id.localidade);
        ct7   = findViewById(R.id.codpostal);
        ct8   = findViewById(R.id.gen);
        id = getIntent().getExtras().getInt("ver");
        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();
        cursor = db.query(false, Contrato.Contacto.TABLE_NAME, Contrato.Contacto.PROJECTION,
                Contrato.Contacto._ID + " = ?", new String[]{id+""},
                null, null,
                null, null);
        cursor.moveToFirst();
        ct1.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NOME)));
        ct2.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NUMERO))));
        ct3.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_IDADE))));
        ct4.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_EMAIL)));
        ct5.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_PROFISSAO)));
        ct6.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_LOCALIDADE)));
        ct7.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_CODPOSTAL)));
        ct8.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_GENERO)));
        cursor.close();
    }


    public void bt3(View view){
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cursor!=null) {
            cursor.close();
        }
        if (db.isOpen()) {
            db.close();
            db = null;
        }
    }

}
