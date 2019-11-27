package com.example.trabalhopratico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.encrypt.Security;

public class PaginaEntrada extends AppCompatActivity {
    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;
    Security s = new Security();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_entrada);
        mDbHelper= new DB(this);
        db = mDbHelper.getReadableDatabase();
        final ContentValues cv = new ContentValues();
        final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        if(isloggedin){
            Intent main = new Intent(PaginaEntrada.this, MainActivity.class);
            startActivity(main);
        }

        final EditText name_field=(EditText)findViewById(R.id.username);
        final EditText password_field=(EditText)findViewById(R.id.password);
        Button login =(Button)findViewById(R.id.bt2);
        Button register=(Button)findViewById(R.id.bt5);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=name_field.getText().toString();
                String password=password_field.getText().toString();

                try {
                    password=s.encrypt(password);
                } catch (Exception e){
                    e.printStackTrace();
                }
                c = db.rawQuery("select * from " + Contrato.Usern.TABLE_NAME + " where " + Contrato.Usern.COLUMN_NOME +
                        " = ?", new String[]{name});
                String db_name = null;
                String db_password = null;

                if(c != null && c.getCount() > 0)
                {
                    c.moveToFirst();
                    db_name = c.getString(c.getColumnIndexOrThrow(Contrato.Usern.COLUMN_NOME));
                    db_password = c.getString(c.getColumnIndexOrThrow(Contrato.Usern.COLUMN_PASS));
                }


                if(name.equals(db_name)&&password.equals(db_password)) {
                    SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    int idu = c.getInt(c.getColumnIndexOrThrow(Contrato.Usern._ID));
                    editor.putInt("IDUSER",idu);
                    editor.putString("NAME",name);
                    editor.putString("PASSWORD",password);
                    editor.putBoolean("ISLOGGEDIN",true);
                    editor.commit();
                    Intent main = new Intent(PaginaEntrada.this, MainActivity.class);

                    startActivity(main);
                }
                else
                {
                    Toast.makeText(PaginaEntrada.this,"Name or password is incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register=new Intent(PaginaEntrada.this,Register.class);
                startActivity(register);
                finish();
            }
        });

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
