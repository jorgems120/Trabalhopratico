package com.example.trabalhopratico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;

import java.util.Calendar;

public class SecondActivity extends AppCompatActivity  {

    SQLiteDatabase db;
    DB mDbHelper;
    EditText ct1,ct2,ct3,ct4,ct5,ct6,ct7;
    Spinner gendrop;
    String genero;
    SharedPreferences sharedPreferences;

    int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ct1 =(EditText)findViewById(R.id.nome);
        ct2 =(EditText)findViewById(R.id.numero);
        ct3 =(EditText)findViewById(R.id.idade);
        ct4 =(EditText)findViewById(R.id.email);
        ct5 =(EditText)findViewById(R.id.profissao);
        ct6 =(EditText)findViewById(R.id.localidade);
        ct7 =(EditText)findViewById(R.id.codpostal);

        gendrop = findViewById(R.id.gen);

        String[] items = new String[]{"Genero","Masculino","Feminino"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        gendrop.setAdapter((adapter));

        mDbHelper = new DB(this);
        db = mDbHelper.getReadableDatabase();
        sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        id_user=sharedPreferences.getInt("IDUSER",1);

        gendrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (parent.getItemAtPosition(pos).equals(getResources().getString(R.string.genero))){

                }
                else {
                    genero = (String) parent.getItemAtPosition(pos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void newContacto(){

        ContentValues cv = new ContentValues();

        cv.put(Contrato.Contacto.COLUMN_NOME, ct1.getText().toString());
        cv.put(Contrato.Contacto.COLUMN_NUMERO, Integer.parseInt(ct2.getText().toString()));
        cv.put(Contrato.Contacto.COLUMN_IDADE, Integer.parseInt(ct3.getText().toString()));
        cv.put(Contrato.Contacto.COLUMN_EMAIL, ct4.getText().toString());
        cv.put(Contrato.Contacto.COLUMN_PROFISSAO, ct5.getText().toString());
        cv.put(Contrato.Contacto.COLUMN_LOCALIDADE, ct6.getText().toString());
        cv.put(Contrato.Contacto.COLUMN_CODPOSTAL, ct7.getText().toString());
        cv.put(Contrato.Contacto.COLUMN_GENERO, genero);
        cv.put(Contrato.Contacto.COLUMN_ID_USER, id_user);
        db.insert(Contrato.Contacto.TABLE_NAME, null, cv);
    }


    public void bt1(View view){

        if( ct1.getText().toString().equals("") || ct2.getText().toString().equals("") || ct3.getText().toString().equals(""))
        {
            Toast.makeText(this, getResources().getString(R.string.Aviso2), Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent();
            newContacto();
            setResult(RESULT_OK, intent);
            Toast.makeText(SecondActivity.this, (R.string.criado), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (db.isOpen()) {
            db.close();
            db = null;
        }
    }

}



