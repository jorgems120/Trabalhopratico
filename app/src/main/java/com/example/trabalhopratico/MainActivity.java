package com.example.trabalhopratico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabalhopratico.adapters.CustomArrayAdapter;
import com.example.trabalhopratico.adapters.MyCursorAdapter;
import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.entities.Contacto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {
    ArrayList<Contacto> ap = new ArrayList<>();
   DB mDbHelper;
   SQLiteDatabase db;
   Cursor c, cursor;
   MyCursorAdapter madapter;
   SharedPreferences sharedPreferences;
   ListView listView;
   String user_name;
   int id_user;
   private SensorManager mSensorManager;
   private Sensor mProximity;
   private static final int SENSOR_SENSITIVITY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) MainActivity.this.getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mDbHelper = new DB(MainActivity.this);
        db = mDbHelper.getReadableDatabase();
        sharedPreferences=MainActivity.this.getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
        user_name = sharedPreferences.getString("NAME", "DEFAULT_NAME");
        id_user = sharedPreferences.getInt("IDUSER", 1);
        registerForContextMenu((ListView)findViewById(R.id.lista));
        FloatingActionButton fab = findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.lista);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacto vp = ap.get(position);
                int id_contacto = vp.getId();
                Intent intent = new Intent(MainActivity.this, ver.class);
                intent.putExtra("ver",id_contacto);
                startActivity(intent);

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);

        if(!ap.isEmpty()) {
            ap.clear();
        }

        String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordemins/" + id_user;


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("DATA");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                        obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                ap.add(p);
                            }
                            CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                            ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                        } catch (JSONException ex) {
                        }
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Erro", error.toString());
                    }
                });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);


    }

    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent login=new Intent(MainActivity.this, PaginaEntrada.class);
                final SharedPreferences sharedPreferences= MainActivity.this.getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).commit();
                startActivity(login);
                MainActivity.this.finish();

                break;
            case R.id.GList:
                onResume();
                break;

                case R.id.orgAZ:
                    if(!ap.isEmpty()) {
                        ap.clear();
                    }

                    String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordemaz/" + id_user;

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray arr = response.getJSONArray("DATA");
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject obj = arr.getJSONObject(i);

                                            Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                                    obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                            ap.add(p);
                                        }
                                        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                                        ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                    } catch (JSONException ex) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("Erro", error.toString());
                                }
                            });
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);

                    break;
            case R.id.orgZA:
                if(!ap.isEmpty()) {
                    ap.clear();
                }

                String url1 = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordemza/" + id_user;

                JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                        (Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arr = response.getJSONArray("DATA");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = arr.getJSONObject(i);

                                        Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                                obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                        ap.add(p);
                                    }
                                    CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                                    ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                } catch (JSONException ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Erro", error.toString());
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest1);
                break;

            case R.id.younger:
                if(!ap.isEmpty()) {
                    ap.clear();
                }

                String url2 = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordeme21/" + id_user;

                JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                        (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arr = response.getJSONArray("DATA");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = arr.getJSONObject(i);

                                        Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                                obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                        ap.add(p);
                                    }
                                    CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                                    ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                } catch (JSONException ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Erro", error.toString());
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest2);


                break;
            case R.id.older:
                if(!ap.isEmpty()) {
                    ap.clear();
                }

                String url3 = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordem21/" + id_user;

                JsonObjectRequest jsObjRequest3 = new JsonObjectRequest
                        (Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arr = response.getJSONArray("DATA");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = arr.getJSONObject(i);

                                        Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                                obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                        ap.add(p);
                                    }
                                    CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                                    ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                } catch (JSONException ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Erro", error.toString());
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest3);

                break;

                default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu,View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_const_1, menu);
    }

    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index =info.position;
        final Contacto p = ap.get(index);
        Integer id_pess = p.getId();

        switch (item.getItemId()){
            case R.id.edit:
                EditBox(id_pess);
                return true;
            case R.id.remove:
                RemoveConfirmationBox(id_pess);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void RemoveConfirmationBox(final int id_pess){

        AlertDialog.Builder a_Builder = new AlertDialog.Builder(MainActivity.this);
        a_Builder.setMessage(getResources().getString(R.string.Aviso))
            .setCancelable(false)
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/contactod/" + id_pess;

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Aviso4) , Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);

                    onResume();
                }
            })
           .setNegativeButton("Nao", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           }) ;
        AlertDialog alert = a_Builder.create();
        alert.setTitle("Remover Contacto");
        alert.show();

    }

    public void EditBox(final int id_pess){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.input_box);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        txtMessage.setText(getResources().getString(R.string.Editar));
        final EditText editText=(EditText)dialog.findViewById(R.id.txtinput);
        final EditText editText2=(EditText)dialog.findViewById(R.id.txtinput2);
        final EditText editText3=(EditText)dialog.findViewById(R.id.txtinput3);
        final EditText editText4=(EditText)dialog.findViewById(R.id.txtinput4);
        final EditText editText5=(EditText)dialog.findViewById(R.id.txtinput5);
        final EditText editText6=(EditText)dialog.findViewById(R.id.txtinput6);

        String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/contacto/" + id_pess;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            editText.setText(response.getString("nome"));
                            editText2.setText(response.getString("numero"));
                            editText3.setText(response.getString("idade"));
                            editText4.setText(response.getString("email"));
                            editText5.setText(response.getString("profissao"));
                            editText6.setText(response.getString("codpostal"));
                        } catch (JSONException ex) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro", error.toString());
                    }
                });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);

        Button bt=(Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/contactoe/" + id_pess;

                Map<String, String> jsonParams = new HashMap<String, String>();

                jsonParams.put("nome", editText.getText().toString());
                jsonParams.put("numero", editText2.getText().toString());
                jsonParams.put("idade", editText3.getText().toString());
                jsonParams.put("email", editText4.getText().toString());
                jsonParams.put("profissao", editText5.getText().toString());
                jsonParams.put("codpostal", editText6.getText().toString());


                JsonObjectRequest putRequest = new JsonObjectRequest
                        (Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if (response.getBoolean("status")) {
                                        Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                    }
                                    onResume();
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.Aviso3) , Toast.LENGTH_SHORT).show();

                                }
                                catch(JSONException ex){
                                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("Erro", error.toString());
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
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(putRequest);
            }

        });
        dialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY){
                if(!ap.isEmpty()) {
                    ap.clear();
                }

                String url = "https://trabalhopratico3.000webhostapp.com/myslim/api/ordeml10/" + id_user;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray arr = response.getJSONArray("DATA");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject obj = arr.getJSONObject(i);

                                        Contacto p = new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("numero"), obj.getString("idade"), obj.getString("email"),
                                                obj.getString("profissao"), obj.getString("codpostal"), obj.getString("genero"), obj.getString("localidade_id"));

                                        ap.add(p);
                                    }
                                    CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, ap);
                                    ((ListView) listView.findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                } catch (JSONException ex) {
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Erro", error.toString());
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
