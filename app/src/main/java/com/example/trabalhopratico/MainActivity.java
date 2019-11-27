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

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity  {
    ArrayList<Contacto> ap = new ArrayList<>();
   DB mDbHelper;
   SQLiteDatabase db;
   Cursor c, cursor;
   MyCursorAdapter madapter;
   SharedPreferences sharedPreferences;
   ListView listView;
   String user_name;
   int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    public void getCursor(){

        c = db.rawQuery("select * from " + Contrato.Contacto.TABLE_NAME + " where " + Contrato.Contacto.COLUMN_ID_USER +
                " = ?", new String[]{id_user+""});
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
                c = db.query(false, Contrato.Contacto.TABLE_NAME, Contrato.Contacto.PROJECTION,
                        Contrato.Contacto.COLUMN_ID_USER + " = ?", new String[]{id_user+""},
                        null, null,
                        Contrato.Contacto.COLUMN_NOME + " ASC", null);
                madapter = new MyCursorAdapter(MainActivity.this, c);
                listView.setAdapter(madapter);
                break;

            case R.id.orgZA:
                c = db.query(false, Contrato.Contacto.TABLE_NAME, Contrato.Contacto.PROJECTION,
                        Contrato.Contacto.COLUMN_ID_USER + " = ?", new String[]{id_user+""},
                        null, null,
                        Contrato.Contacto.COLUMN_NOME + " DESC", null);
                madapter = new MyCursorAdapter(MainActivity.this, c);
                listView.setAdapter(madapter);
                break;

            case R.id.younger:
                c = db.rawQuery("select * from " + Contrato.Contacto.TABLE_NAME + " where " + Contrato.Contacto.COLUMN_IDADE +
                        " < '21' " + " AND " + Contrato.Contacto.COLUMN_ID_USER + " = ?", new String[]{id_user+""});
                madapter = new MyCursorAdapter(MainActivity.this, c);
                listView.setAdapter(madapter);
                break;
            case R.id.older:
                c = db.rawQuery("select * from " + Contrato.Contacto.TABLE_NAME + " where " + Contrato.Contacto.COLUMN_IDADE +
                        " >= '21' " + " AND " + Contrato.Contacto.COLUMN_ID_USER + " = ?", new String[]{id_user+""});
                madapter = new MyCursorAdapter(MainActivity.this, c);
                listView.setAdapter(madapter);
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
        c.moveToPosition(index);
        int id_pessoa = c.getInt(c.getColumnIndex(Contrato.Contacto._ID));

        switch (item.getItemId()){
            case R.id.edit:
                EditBox(id_pessoa);
                return true;
            case R.id.remove:
                RemoveConfirmationBox(id_pessoa);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void RemoveConfirmationBox(final int id){

        AlertDialog.Builder a_Builder = new AlertDialog.Builder(MainActivity.this);
        a_Builder.setMessage(getResources().getString(R.string.Aviso))
            .setCancelable(false)
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.delete(Contrato.Contacto.TABLE_NAME, Contrato.Contacto._ID + " = ?", new String[]{id+""});
                    onResume();
                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.Aviso4) , Toast.LENGTH_SHORT).show();
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

    public void EditBox(final int id_pessoa){
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
        final EditText editText7=(EditText)dialog.findViewById(R.id.txtinput7);
        cursor = db.query(false, Contrato.Contacto.TABLE_NAME, Contrato.Contacto.PROJECTION,
                Contrato.Contacto._ID + " = ?", new String[]{id_pessoa+""},
                null, null,
                null, null);
        cursor.moveToFirst();
        editText.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NOME)));
        editText2.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_NUMERO))));
        editText3.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_IDADE))));
        editText4.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_EMAIL)));
        editText5.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_PROFISSAO)));
        editText6.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_LOCALIDADE)));
        editText7.setText(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Contacto.COLUMN_CODPOSTAL)));

        Button bt=(Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();


                if( editText.getText().toString().equals("") || editText2.getText().toString().equals("") || editText3.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.Aviso2), Toast.LENGTH_SHORT).show();
                }
                else {
                    cv.put(Contrato.Contacto.COLUMN_NOME, editText.getText().toString());
                    cv.put(Contrato.Contacto.COLUMN_NUMERO, Integer.parseInt(editText2.getText().toString()));
                    cv.put(Contrato.Contacto.COLUMN_IDADE, Integer.parseInt(editText3.getText().toString()));
                    cv.put(Contrato.Contacto.COLUMN_EMAIL, editText4.getText().toString());
                    cv.put(Contrato.Contacto.COLUMN_PROFISSAO, editText5.getText().toString());
                    cv.put(Contrato.Contacto.COLUMN_LOCALIDADE, editText6.getText().toString());
                    cv.put(Contrato.Contacto.COLUMN_CODPOSTAL, editText7.getText().toString());
                    db.update(Contrato.Contacto.TABLE_NAME, cv, Contrato.Contacto._ID + " = ?", new String[]{id_pessoa+""});
                    onResume();
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.Aviso3) , Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }



}
