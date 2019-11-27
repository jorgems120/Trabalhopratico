package com.example.trabalhopratico.db;

import android.provider.BaseColumns;

import java.util.concurrent.ConcurrentNavigableMap;

public class Contrato {

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String DEC_TYPE = " REAL ";

    public Contrato(){
    }

    public static abstract class Contacto implements BaseColumns {
        public static final String TABLE_NAME = "contacto";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_NUMERO = "numero";
        public static final String COLUMN_IDADE = "idade";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PROFISSAO = "profissao";
        public static final String COLUMN_LOCALIDADE = "localidade";
        public static final String COLUMN_CODPOSTAL = "codpostal";
        public static final String COLUMN_GENERO = "genero";
        public static final String COLUMN_ID_USER = "id_user";

        public static final String[] PROJECTION = {Contacto._ID, Contacto.COLUMN_NOME, Contacto.COLUMN_NUMERO, Contacto.COLUMN_IDADE, Contacto.COLUMN_EMAIL, Contacto.COLUMN_PROFISSAO, Contacto.COLUMN_LOCALIDADE, Contacto.COLUMN_CODPOSTAL, Contacto.COLUMN_GENERO, Contacto.COLUMN_ID_USER};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Contacto.TABLE_NAME + "(" +
                        Contacto._ID + INT_TYPE + " PRIMARY KEY," +
                        Contacto.COLUMN_NOME + TEXT_TYPE + "," +
                        Contacto.COLUMN_NUMERO + INT_TYPE + "," +
                        Contacto.COLUMN_IDADE + INT_TYPE + "," +
                        Contacto.COLUMN_EMAIL + TEXT_TYPE + "," +
                        Contacto.COLUMN_PROFISSAO + TEXT_TYPE + "," +
                        Contacto.COLUMN_LOCALIDADE + TEXT_TYPE + "," +
                        Contacto.COLUMN_CODPOSTAL + TEXT_TYPE + "," +
                        Contacto.COLUMN_GENERO + TEXT_TYPE + "," +
                        Contacto.COLUMN_ID_USER + INT_TYPE + " REFERENCES " +
                        Usern.TABLE_NAME+ "(" + Usern._ID + "));";


        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Contacto.TABLE_NAME + ";";
    }

    public static abstract class Usern implements BaseColumns{
        public static final String TABLE_NAME = "usern";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_PASS = "password";


        public static final String[] PROJECTION = {Usern._ID, Usern.COLUMN_NOME, Usern.COLUMN_PASS};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Usern.TABLE_NAME + "(" +
                        Usern._ID+ INT_TYPE + " PRIMARY KEY," +
                        Usern.COLUMN_NOME + TEXT_TYPE + "," +
                        Usern.COLUMN_PASS + TEXT_TYPE + ");";

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Usern.TABLE_NAME + ";";

    }

}
