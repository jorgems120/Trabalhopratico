package com.example.trabalhopratico;

import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.entities.Contacto;

import org.json.JSONException;
import org.json.JSONObject;

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
        String url ="https://trabalhopratico3.000webhostapp.com/myslim/api/contacto/" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ct1.setText(response.getString("nome"));
                            ct2.setText(response.getString("numero"));
                            ct3.setText(response.getString("idade"));
                            ct4.setText(response.getString("email"));
                            ct5.setText(response.getString("profissao"));
                            ct7.setText(response.getString("codpostal"));
                            ct8.setText(response.getString("genero"));
                            joinIdLocalidade(id);
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro", error.toString());
                    }
                });
        MySingleton.getInstance(ver.this).addToRequestQueue(jsObjRequest);
    }

    private void joinIdLocalidade(int idl){
        String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/localidade/" + idl;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String result = response.replace("\"", " ");
                ct6.setText(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        queue.add(request);
    }


    public void bt3(View view){
        finish();
    }



}
