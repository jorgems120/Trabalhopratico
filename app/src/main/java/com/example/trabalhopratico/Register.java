package com.example.trabalhopratico;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabalhopratico.db.Contrato;
import com.example.trabalhopratico.db.DB;
import com.example.trabalhopratico.encrypt.Security;

public class Register extends AppCompatActivity {
    Intent next_activity;
    Cursor c;
    DB mDbHelper;
    SQLiteDatabase db;
    String nameBD;
    Security s = new Security();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mDbHelper = new DB( Register.this);
        db = mDbHelper.getReadableDatabase();
        final ContentValues cv = new ContentValues();
        next_activity=new Intent(this, PaginaEntrada.class);
        final EditText name_field=(EditText)findViewById(R.id.username);
        final EditText password_field=(EditText)findViewById(R.id.password);
        final EditText confirm_password_field=(EditText)findViewById(R.id.cpassword);
        Button registerbutton=(Button)findViewById(R.id.register);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_field.getText().toString();
                c = db.rawQuery("select * from " + Contrato.Usern.TABLE_NAME + " where " + Contrato.Usern.COLUMN_NOME +
                        " = ?", new String[]{name});
                    if((name.length() < 6) || password_field.getText().toString().length()<6){

                        Toast.makeText(Register.this,getResources().getString(R.string.erro2), Toast.LENGTH_SHORT).show();
                    }
                    else{
                    if (c != null && c.getCount() > 0) {
                        Toast.makeText(Register.this, getResources().getString(R.string.erro1), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String password_1 = password_field.getText().toString();
                        String password_2 = confirm_password_field.getText().toString();

                        if (password_1.equals(password_2)) {
                            try {
                                password_1 = s.encrypt(password_1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            cv.put(Contrato.Usern.COLUMN_NOME, name);
                            cv.put(Contrato.Usern.COLUMN_PASS, password_1);
                            db.insert(Contrato.Usern.TABLE_NAME, null, cv);
                            startActivity(next_activity);
                        } else {
                            Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                        }

                        }
                    }
                }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (c!=null) {
            c.close();
        }
        if (db.isOpen()) {
            db.close();
            db = null;
        }
    }
}
