package com.example.trabalhopratico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity  {

    SQLiteDatabase db;
    DB mDbHelper;
    EditText ct1,ct2,ct3,ct4,ct5,ct6,ct7;
    Spinner gendrop, localidadedrop;
    String genero, localidade;
    int localidade_id;
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
        ct7 =(EditText)findViewById(R.id.codpostal);

        gendrop = findViewById(R.id.gen);
        String[] items = new String[]{"Genero","Masculino","Feminino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        gendrop.setAdapter((adapter));

        localidadedrop = findViewById(R.id.localidade);
        String[] items2 = new String[]{getResources().getString(R.string.Localidade1),getResources().getString(R.string.Lisboa),getResources().getString(R.string.Porto),getResources().getString(R.string.Viana),getResources().getString(R.string.Braga)};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        localidadedrop.setAdapter(adapter2);

        sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        id_user=sharedPreferences.getInt("IDUSER",1);

        gendrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (parent.getItemAtPosition(pos).equals(getResources().getString(R.string.genero))){
                    genero = "";
                }
                else {
                    genero = (String) parent.getItemAtPosition(pos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        localidadedrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                localidade = (String) parent.getItemAtPosition(pos);
                if (localidade == getResources().getString(R.string.Lisboa)){
                    localidade_id = 1;
                }
                else if (localidade == getResources().getString(R.string.Porto)){
                    localidade_id = 2;
                }else if (localidade == getResources().getString(R.string.Viana)){
                    localidade_id = 3;
                }else if (localidade == getResources().getString(R.string.Braga)){
                    localidade_id = 4;
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void newContacto(){

        String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/contacto";

        Map<String, String> jsonParams = new HashMap<String, String>();


        jsonParams.put("nome", ct1.getText().toString());
        jsonParams.put("numero", ct2.getText().toString());
        jsonParams.put("idade", ct3.getText().toString());
        jsonParams.put("email", ct4.getText().toString());
        jsonParams.put("profissao", ct5.getText().toString());
        jsonParams.put("codpostal", ct7.getText().toString());
        jsonParams.put("user_id", id_user+"");
        jsonParams.put("localidade_id", localidade_id+"");
        jsonParams.put("genero", genero);

        // Formulate the request and handle the response.
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getBoolean("status")) {
                                Toast.makeText(SecondActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(SecondActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch(JSONException ex){
                            Toast.makeText(SecondActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Erro", error.toString());
                        Toast.makeText(SecondActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getInstance(SecondActivity.this).addToRequestQueue(postRequest);

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



}



