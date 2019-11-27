package com.example.trabalhopratico;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.encrypt.Security;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Intent next_activity;
    Cursor c;
    DB mDbHelper;
    SQLiteDatabase db;
    Security s = new Security();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mDbHelper = new DB(Register.this);
        db = mDbHelper.getReadableDatabase();
        final ContentValues cv = new ContentValues();
        next_activity = new Intent(this, PaginaEntrada.class);
        final EditText name_field = (EditText) findViewById(R.id.username);
        final EditText password_field = (EditText) findViewById(R.id.password);
        final EditText confirm_password_field = (EditText) findViewById(R.id.cpassword);
        Button registerbutton = (Button) findViewById(R.id.register);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = name_field.getText().toString();
                final String password = password_field.getText().toString();
                if ((username.length() < 6) || password.length() < 6) {

                    Toast.makeText(Register.this, getResources().getString(R.string.erro2), Toast.LENGTH_SHORT).show();
                } else {

                    String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/user/" + username;

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                public void onResponse(JSONObject response) {
                                    Toast.makeText(Register.this, getResources().getString(R.string.erro1), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {

                                public void onErrorResponse(VolleyError error) {
                                    String cpassword = confirm_password_field.getText().toString();
                                    String password_enc = null;
                                    if (password.equals(cpassword)) {
                                        try {
                                            password_enc = s.encrypt(password);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        postUser(username, password_enc);
                                        Toast.makeText(Register.this, "deu", Toast.LENGTH_SHORT).show();
                                        startActivity(next_activity);
                                    } else {
                                        Toast.makeText(Register.this, getResources().getString(R.string.erro3), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    MySingleton.getInstance(Register.this).addToRequestQueue(jsObjRequest);

                }
            }


    public void postUser(String username, String password_enc) {
        String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/user";

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("username", username);
        jsonParams.put("password", password_enc);


        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(Register.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(Register.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro", error.toString());
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getInstance(Register.this).addToRequestQueue(postRequest);
    }
        });

    }
}
