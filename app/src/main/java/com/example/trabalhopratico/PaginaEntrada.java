package com.example.trabalhopratico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.encrypt.Security;

import org.json.JSONException;
import org.json.JSONObject;

public class PaginaEntrada extends AppCompatActivity {
    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;
    Security s = new Security();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_entrada);
        mDbHelper= new DB(this);
        db = mDbHelper.getReadableDatabase();
        final ContentValues cv = new ContentValues();
     sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        if(isloggedin){
            Intent main = new Intent(PaginaEntrada.this, MainActivity.class);
            startActivity(main);
        }

        final EditText username_field=(EditText)findViewById(R.id.username);
        final EditText password_field=(EditText)findViewById(R.id.password);
        Button login =(Button)findViewById(R.id.bt2);
        Button register=(Button)findViewById(R.id.bt5);
        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {


                                         final String username = username_field.getText().toString();
                                         final String password = password_field.getText().toString();
                                         String passenc = null;

                                         try {
                                             passenc = s.encrypt(password);
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }

                                         String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/user/" + username + "&" + passenc;

                                         JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                                 (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                                     @Override
                                                     public void onResponse(JSONObject response) {
                                                         try {
                                                             sharedPreferences = getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
                                                             editor = sharedPreferences.edit();
                                                             editor.putInt("IDUSER", response.getInt("id"));
                                                             editor.putString("NAME", username);
                                                             editor.putString("PASSWORD", password);
                                                             editor.commit();

                                                             Intent main = new Intent(PaginaEntrada.this, MainActivity.class);
                                                             startActivity(main);
                                                         } catch (JSONException ex) {
                                                         }
                                                     }
                                                 }, new Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {
                                                         Toast.makeText(PaginaEntrada.this, getResources().getString(R.string.erro4), Toast.LENGTH_SHORT).show();
                                                     }
                                                 });
                                         MySingleton.getInstance(PaginaEntrada.this).addToRequestQueue(jsObjRequest);
                                     }
                                 });

            register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register=new Intent(PaginaEntrada.this,Register.class);
                startActivity(register);
            }
        });

    }



}
